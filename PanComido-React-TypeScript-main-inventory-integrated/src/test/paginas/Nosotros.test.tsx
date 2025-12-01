// src/test/paginas/Nosotros.test.tsx
import React from "react";
import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import { Nosotros } from "../../paginas/Nosotros"; // 🔧 ajusta la ruta si está en otro lado

describe("Nosotros", () => {
  it('muestra el título "Nuestra Historia"', () => {
    render(<Nosotros />);

    const heading = screen.getByRole("heading", {
      name: /nuestra historia/i,
      level: 2,
    });

    expect(heading).toBeInTheDocument();
  });

  it("muestra el texto descriptivo de la panadería", () => {
    render(<Nosotros />);

    // Buscamos una parte relevante del texto
    expect(
      screen.getByText(/pan artesanal/i)
    ).toBeInTheDocument();

    expect(
      screen.getByText(/Pan Comido/i)
    ).toBeInTheDocument();
  });

  it("renderiza la imagen principal con src y alt correctos", () => {
    render(<Nosotros />);

    const img = screen.getByRole("img", {
      name: /panadería pan comido/i,
    });

    expect(img).toBeInTheDocument();
    expect(img).toHaveAttribute(
      "src",
      "https://tse1.mm.bing.net/th/id/OIP._JX21ulVynsX-fgSMwd3NwAAAA?rs=1&pid=ImgDetMain&o=7&rm=3"
    );
  });
});
