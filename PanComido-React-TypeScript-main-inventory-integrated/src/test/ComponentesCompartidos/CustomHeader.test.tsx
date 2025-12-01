// src/test/ComponentesCompartidos/CustomHeader.test.tsx
import React from "react";
import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import { CustomHeader } from "../../ComponentesCompartidos/CustomHeader"; 
// 🔺 ajusta la ruta según tu estructura real

describe("CustomHeader", () => {
  it("muestra el título enviado por props", () => {
    const titulo = "Pan Comido";

    render(<CustomHeader title={titulo} />);

    const heading = screen.getByRole("heading", { name: titulo });
    expect(heading).toBeInTheDocument();
  });

  it('usa el texto por defecto "Tienda de pan" cuando no se pasa text', () => {
    const titulo = "Panadería online";

    render(<CustomHeader title={titulo} />);

    expect(screen.getByText("Tienda de pan")).toBeInTheDocument();
  });

  it("muestra el texto personalizado cuando se pasa text", () => {
    const titulo = "Ofertas de hoy";
    const textoPersonalizado = "Los mejores panes de Santiago";

    render(<CustomHeader title={titulo} text={textoPersonalizado} />);

    expect(screen.getByText(textoPersonalizado)).toBeInTheDocument();
    // y opcionalmente verificas que NO esté el texto por defecto
    expect(screen.queryByText("Tienda de pan")).not.toBeInTheDocument();
  });
});
