// src/test/ComponentesCompartidos/NavBar.test.tsx
import React from "react";
import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import { Navbar } from "../../ComponentesCompartidos";

describe("Navbar", () => {
  const itemsMock = [
    { URL: "/panes", cadenasVisibles: "Panes" },
    { URL: "/tortas", cadenasVisibles: "Tortas" },
    { URL: "/contacto", cadenasVisibles: "Contacto" },
  ];

  it('muestra el enlace "Inicio" apuntando a "/"', () => {
    render(<Navbar items={itemsMock} />);

    const linkInicio = screen.getByRole("link", { name: /inicio/i });
    expect(linkInicio).toBeInTheDocument();
    expect(linkInicio).toHaveAttribute("href", "/");
  });

  it("renderiza todos los ítems de navegación con su URL correcta", () => {
    render(<Navbar items={itemsMock} />);

    itemsMock.forEach((item) => {
      const link = screen.getByRole("link", {
        name: item.cadenasVisibles,
      });
      expect(link).toBeInTheDocument();
      expect(link).toHaveAttribute("href", item.URL);
    });
  });
});
