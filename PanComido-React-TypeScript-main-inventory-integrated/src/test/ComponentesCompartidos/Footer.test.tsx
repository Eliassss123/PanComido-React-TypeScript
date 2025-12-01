// src/test/ComponentesCompartidos/Footer.test.tsx
import React from "react";
import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import { Footer } from "../../ComponentesCompartidos/Footer"; 
// 🔺 ajusta la ruta según tu estructura real

describe("Footer", () => {
  it("se renderiza el elemento footer", () => {
    render(<Footer />);

    const footer = screen.getByRole("contentinfo");
    expect(footer).toBeInTheDocument();
  });

  it("muestra la información de contacto", () => {
    render(<Footer />);

    // Buscar por partes del texto (para no pelear con emojis/espacios)
    expect(screen.getByText(/Teléfono:/i)).toBeInTheDocument();
    expect(screen.getByText(/Av\.San Ignacio 3318/i)).toBeInTheDocument();
    expect(screen.getByText(/Quilicura/i)).toBeInTheDocument();
  });

  it("muestra el título y descripción de la panadería", () => {
    render(<Footer />);

    expect(screen.getByText("Pan Comido")).toBeInTheDocument();
    expect(
      screen.getByText(/La mejor panadería de la ciudad/i)
    ).toBeInTheDocument();
    expect(
      screen.getByText(/Todos los derechos reservados/i)
    ).toBeInTheDocument();
  });

  it("muestra las redes sociales", () => {
    render(<Footer />);

    expect(screen.getByText(/Facebook/i)).toBeInTheDocument();
    expect(screen.getByText(/Instagram/i)).toBeInTheDocument();
    expect(screen.getByText(/Twitter/i)).toBeInTheDocument();
  });
});
