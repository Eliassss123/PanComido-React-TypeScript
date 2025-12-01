// src/test/RaizApp.test.tsx
import React from "react";
import { describe, it, expect, beforeEach, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import { PanaderiaApp } from "../RaizApp";

describe("PanaderiaApp (Raiz)", () => {
  beforeEach(() => {
    localStorage.clear();

    // mock de fetch para el useEffect
    // @ts-expect-error: forzamos el mock
    global.fetch = vi.fn().mockResolvedValue({
      ok: false,
      status: 500,
      json: vi.fn().mockResolvedValue({}),
    });
  });

  it("cuando NO está autenticado muestra el ModalLogin", () => {
    render(<PanaderiaApp />);

    expect(
      screen.getByText(/iniciar sesión/i)
    ).toBeInTheDocument();
  });

  it("cuando está autenticado muestra el navbar y la pestaña de panes por defecto", async () => {
    // Simulamos sesión ya guardada
    localStorage.setItem("auth", "true");
    localStorage.setItem("correoUsuario", "cliente@pancomido.cl");
    localStorage.setItem("rol", "USER");

    render(<PanaderiaApp />);

    // Esperamos a que la UI haya terminado de pintar (evita el warning de act)
    const botonCerrarSesion = await screen.findByRole("button", {
      name: /cerrar sesión/i,
    });
    expect(botonCerrarSesion).toBeInTheDocument();

    // Hay varios "Pan Comido", así que usamos getAllByText
    const coincidencias = screen.getAllByText(/pan comido/i);
    expect(coincidencias.length).toBeGreaterThanOrEqual(1);

    // Texto del header de la galería (pestaña panes por defecto)
    const bienvenida = await screen.findByText(/bienvenidos a la panadería/i);
    expect(bienvenida).toBeInTheDocument();

    // El buscador también debería estar
    expect(
      screen.getByPlaceholderText(/busque su producto/i)
    ).toBeInTheDocument();
  });
});
