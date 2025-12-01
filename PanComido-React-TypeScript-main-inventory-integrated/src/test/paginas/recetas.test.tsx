// src/test/paginas/Recetas.test.tsx
import React from "react";
import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import { Recetas } from "../../paginas"; // 🔧 ajusta la ruta si es distinta

describe("Recetas", () => {
  it("renderiza un heading h2 (aunque el título esté vacío)", () => {
    render(<Recetas />);

    const heading = screen.getByRole("heading", { level: 2 });
    expect(heading).toBeInTheDocument();
  });

  it("renderiza la imagen del recetario con la URL correcta", () => {
    render(<Recetas />);

    const img = screen.getByRole("img");
    expect(img).toHaveAttribute("src", "/images/Recetario.jpg");
  });

  it("la imagen está dentro de un enlace que abre en otra pestaña con noopener noreferrer", () => {
    render(<Recetas />);

    const link = screen.getByRole("link");
    expect(link).toHaveAttribute("href", "/images/Recetario.jpg");
    expect(link).toHaveAttribute("target", "_blank");
    expect(link).toHaveAttribute("rel", "noopener noreferrer");
  });
});
