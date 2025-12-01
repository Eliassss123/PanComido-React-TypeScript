// src/test/paginas/BusquedasPrevias.test.tsx
import React from "react";
import { describe, it, expect, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { BusquedasPrevias } from "../../imagesComponents"; 
// 🔧 ajusta la ruta según dónde tengas el componente

describe("BusquedasPrevias", () => {
  const busquedasMock = ["marraqueta", "hallulla", "brioche"];

  it("renderiza un botón por cada término de búsqueda", () => {
    render(
      <BusquedasPrevias
        busquedas={busquedasMock}
        onLabelClicked={vi.fn()}
      />
    );

    busquedasMock.forEach((termino) => {
      const boton = screen.getByRole("button", { name: termino });
      expect(boton).toBeInTheDocument();
    });
  });

  it("llama a onLabelClicked con el término correcto al hacer click", async () => {
    const user = userEvent.setup();
    const onLabelClicked = vi.fn();

    render(
      <BusquedasPrevias
        busquedas={busquedasMock}
        onLabelClicked={onLabelClicked}
      />
    );

    const botonHallulla = screen.getByRole("button", { name: "hallulla" });

    await user.click(botonHallulla);

    expect(onLabelClicked).toHaveBeenCalledTimes(1);
    expect(onLabelClicked).toHaveBeenCalledWith("hallulla");
  });

  it("no revienta si no se pasa onLabelClicked y se hace click en los botones", async () => {
    const user = userEvent.setup();

    render(<BusquedasPrevias busquedas={busquedasMock} />);

    const boton = screen.getByRole("button", { name: "marraqueta" });

    // Si esto no lanza error, el test pasa
    await user.click(boton);

    // No hay expect necesario, solo verificar que no crashea
  });

  it("no muestra botones si la lista de búsquedas está vacía", () => {
    render(<BusquedasPrevias busquedas={[]} />);

    // No deberíamos encontrar ningún botón de búsqueda
    const botones = screen.queryAllByRole("button");
    expect(botones.length).toBe(0);
  });
});
