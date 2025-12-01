// src/test/ComponentesCompartidos/CategoriasDropdown.test.tsx
import React from "react";
import { describe, it, expect, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { CategoriasDropdown } from "../../ComponentesCompartidos/CategoriasDropdown"; 
// 🔺 ajusta la ruta según dónde pongas el test

describe("CategoriasDropdown", () => {
  const categoriasMock = ["Pan", "Tortas", "Pasteles"];

  it('muestra el título "Categorías" cuando no hay categoría seleccionada', () => {
    const onSelect = vi.fn();

    render(
      <CategoriasDropdown
        categorias={categoriasMock}
        categoriaSeleccionada={null}
        onSelect={onSelect}
      />
    );

    // El botón del dropdown debería mostrar "Categorías"
    const button = screen.getByRole("button", { name: /categorías/i });
    expect(button).toBeInTheDocument();
  });

  it("muestra la categoría seleccionada en el título", () => {
    const onSelect = vi.fn();

    render(
      <CategoriasDropdown
        categorias={categoriasMock}
        categoriaSeleccionada="Tortas"
        onSelect={onSelect}
      />
    );

    const button = screen.getByRole("button", { name: /tortas/i });
    expect(button).toBeInTheDocument();
  });

  it("llama a onSelect con la categoría clickeada", async () => {
    const user = userEvent.setup();
    const onSelect = vi.fn();

    render(
      <CategoriasDropdown
        categorias={categoriasMock}
        categoriaSeleccionada={null}
        onSelect={onSelect}
      />
    );

    // Abrimos el dropdown
    const button = screen.getByRole("button", { name: /categorías/i });
    await user.click(button);

    // Clic en "Pan"
    const opcionPan = await screen.findByText("Pan");
    await user.click(opcionPan);

    expect(onSelect).toHaveBeenCalledTimes(1);
    expect(onSelect).toHaveBeenCalledWith("Pan");
  });

  it('llama a onSelect(null) al elegir "Todas"', async () => {
    const user = userEvent.setup();
    const onSelect = vi.fn();

    render(
      <CategoriasDropdown
        categorias={categoriasMock}
        categoriaSeleccionada={null}
        onSelect={onSelect}
      />
    );

    const button = screen.getByRole("button", { name: /categorías/i });
    await user.click(button);

    const opcionTodas = await screen.findByText("Todas");
    await user.click(opcionTodas);

    expect(onSelect).toHaveBeenCalledTimes(1);
    expect(onSelect).toHaveBeenCalledWith(null);
  });
});
