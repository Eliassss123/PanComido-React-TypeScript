// src/test/ComponentesCompartidos/SearchBar.test.tsx
import React from "react";
import { describe, it, expect, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { SearchBar } from "../../ComponentesCompartidos/SearchBar"; // ajusta la ruta si está en otra carpeta

describe("SearchBar", () => {
  it("muestra el placeholder recibido por props", () => {
    const onQuery = vi.fn();

    render(
      <SearchBar
        textoDifuminado="Busca tu pan favorito"
        onQuery={onQuery}
      />
    );

    const input = screen.getByRole("textbox", {
      name: /buscar producto/i, // por el label oculto
    });

    expect(input).toBeInTheDocument();
    expect(input).toHaveAttribute("placeholder", "Busca tu pan favorito");
  });

  it("llama a onQuery al hacer click en 'Buscar' con texto válido", async () => {
    const user = userEvent.setup();
    const onQuery = vi.fn();

    render(
      <SearchBar
        textoDifuminado="Buscar..."
        onQuery={onQuery}
      />
    );

    const input = screen.getByRole("textbox", {
      name: /buscar producto/i,
    });
    const boton = screen.getByRole("button", { name: /buscar/i });

    await user.type(input, "marraqueta");
    await user.click(boton);

    expect(onQuery).toHaveBeenCalledTimes(1);
    expect(onQuery).toHaveBeenCalledWith("marraqueta");
  });

  it("no llama a onQuery si el texto está vacío o solo espacios", async () => {
    const user = userEvent.setup();
    const onQuery = vi.fn();

    render(
      <SearchBar
        textoDifuminado="Buscar..."
        onQuery={onQuery}
      />
    );

    const input = screen.getByRole("textbox", {
      name: /buscar producto/i,
    });
    const boton = screen.getByRole("button", { name: /buscar/i });

    // caso vacío
    await user.clear(input);
    await user.click(boton);

    // caso solo espacios
    await user.type(input, "   ");
    await user.click(boton);

    expect(onQuery).not.toHaveBeenCalled();
  });

  it("llama a onQuery al presionar Enter", async () => {
    const user = userEvent.setup();
    const onQuery = vi.fn();

    render(
      <SearchBar
        textoDifuminado="Buscar..."
        onQuery={onQuery}
      />
    );

    const input = screen.getByRole("textbox", {
      name: /buscar producto/i,
    });

    await user.type(input, "hallulla{enter}");

    expect(onQuery).toHaveBeenCalledTimes(1);
    expect(onQuery).toHaveBeenCalledWith("hallulla");
  });
});
