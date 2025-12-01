// src/test/paginas/ImagesList.test.tsx
import React from "react";
import { describe, it, expect, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { ImagesList } from "../../imagesComponents"; // 🔧 ajusta la ruta según tu proyecto

describe("ImagesList", () => {
  // No importamos ImagePanType para que TS no nos moleste con campos extra,
  // solo usamos los que el componente realmente ocupa (id, url, titulo)
  const imagesMock = [
    {
      id: 1,
      url: "https://example.com/marraqueta.jpg",
      titulo: "Marraqueta",
    },
    {
      id: 2,
      url: "https://example.com/hallulla.jpg",
      titulo: "Hallulla",
    },
  ] as any[];

  it("renderiza una imagen por cada item", () => {
    const onBuy = vi.fn();
    const onView = vi.fn();

    render(
      <ImagesList
        images={imagesMock}
        onBuy={onBuy}
        onView={onView}
      />
    );

    // Hay una imagen por cada item
    const imagenes = screen.getAllByRole("img");
    expect(imagenes).toHaveLength(imagesMock.length);

    // Además podemos verificar por alt text
    expect(
      screen.getByAltText("Marraqueta")
    ).toBeInTheDocument();
    expect(
      screen.getByAltText("Hallulla")
    ).toBeInTheDocument();
  });

  it("llama a onView al hacer click en la imagen", async () => {
    const user = userEvent.setup();
    const onBuy = vi.fn();
    const onView = vi.fn();

    render(
      <ImagesList
        images={imagesMock}
        onBuy={onBuy}
        onView={onView}
      />
    );

    const imgMarraqueta = screen.getByAltText("Marraqueta");

    await user.click(imgMarraqueta);

    expect(onView).toHaveBeenCalledTimes(1);
    expect(onView).toHaveBeenCalledWith(
      expect.objectContaining({ titulo: "Marraqueta" })
    );
    expect(onBuy).not.toHaveBeenCalled();
  });

  it('llama a onBuy al hacer click en "Comprar" y no llama a onView', async () => {
    const user = userEvent.setup();
    const onBuy = vi.fn();
    const onView = vi.fn();

    render(
      <ImagesList
        images={imagesMock}
        onBuy={onBuy}
        onView={onView}
      />
    );

    const botonesComprar = screen.getAllByRole("button", {
      name: /comprar/i,
    });

    // Hacemos click en el botón de la primera tarjeta
    await user.click(botonesComprar[0]);

    expect(onBuy).toHaveBeenCalledTimes(1);
    expect(onBuy).toHaveBeenCalledWith(
      expect.objectContaining({ titulo: "Marraqueta" })
    );
    expect(onView).not.toHaveBeenCalled();
  });

  it("no muestra nada raro si la lista de imágenes está vacía", () => {
    const onBuy = vi.fn();
    const onView = vi.fn();

    render(
      <ImagesList
        images={[]}
        onBuy={onBuy}
        onView={onView}
      />
    );

    // No debería haber imágenes
    const imagenes = screen.queryAllByRole("img");
    expect(imagenes).toHaveLength(0);

    // Ni botones "Comprar"
    const botonesComprar = screen.queryAllByRole("button", {
      name: /comprar/i,
    });
    expect(botonesComprar).toHaveLength(0);
  });
});
