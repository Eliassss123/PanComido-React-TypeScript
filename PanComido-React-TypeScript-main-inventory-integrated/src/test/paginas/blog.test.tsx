// src/test/paginas/Blog.test.tsx
import React from "react";
import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import { Blog } from "../../paginas"; // ajusta la ruta si tu archivo está en otro sitio

describe("Blog", () => {
  it("renderiza un heading h2 (aunque el título esté vacío)", () => {
    render(<Blog />);

    const heading = screen.getByRole("heading", { level: 2 });
    expect(heading).toBeInTheDocument();
  });

  it("renderiza la imagen del blog con la URL correcta", () => {
    render(<Blog />);

    const img = screen.getByRole("img");
    // En el componente, la url es "/images/Blog1.jpg"
    expect(img).toHaveAttribute("src", "/images/Blog1.jpg");
  });

  it("la imagen está dentro de un enlace que abre en otra pestaña con noopener noreferrer", () => {
    render(<Blog />);

    const link = screen.getByRole("link");
    expect(link).toHaveAttribute("href", "/images/Blog1.jpg");
    expect(link).toHaveAttribute("target", "_blank");
    expect(link).toHaveAttribute("rel", "noopener noreferrer");
  });
});
