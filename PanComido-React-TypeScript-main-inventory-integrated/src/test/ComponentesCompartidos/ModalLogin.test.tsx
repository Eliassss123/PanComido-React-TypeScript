// src/test/ComponentesCompartidos/ModalLogin.test.tsx
import React from "react";
import { describe, it, expect, vi, afterEach } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { ModalLogin } from "../../ComponentesCompartidos/ModalLogin"; // ajusta si tu ruta es distinta

describe("ModalLogin", () => {
  const originalFetch = globalThis.fetch;

  afterEach(() => {
    vi.restoreAllMocks();
    globalThis.fetch = originalFetch!;
  });

  it("renderiza título e inputs de correo y contraseña", () => {
    const onLogin = vi.fn();

    render(<ModalLogin onLogin={onLogin} />);

    // Título del modal
    expect(screen.getByText(/iniciar sesión/i)).toBeInTheDocument();

    // Inputs
    expect(screen.getByLabelText(/usuario \(correo\)/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/contraseña/i)).toBeInTheDocument();

    // Botones
    expect(
      screen.getByRole("button", { name: /limpiar/i })
    ).toBeInTheDocument();
    expect(
      screen.getByRole("button", { name: /ingresar/i })
    ).toBeInTheDocument();
  });

  it("limpia los campos al hacer clic en 'Limpiar'", async () => {
    const user = userEvent.setup();
    const onLogin = vi.fn();

    render(<ModalLogin onLogin={onLogin} />);

    const inputUsuario = screen.getByLabelText(/usuario \(correo\)/i);
    const inputPassword = screen.getByLabelText(/contraseña/i);
    const botonLimpiar = screen.getByRole("button", { name: /limpiar/i });

    await user.type(inputUsuario, "admin@pancomido.cl");
    await user.type(inputPassword, "secreto123");

    expect(inputUsuario).toHaveValue("admin@pancomido.cl");
    expect(inputPassword).toHaveValue("secreto123");

    await user.click(botonLimpiar);

    expect(inputUsuario).toHaveValue("");
    expect(inputPassword).toHaveValue("");
  });

  it("muestra alerta si no se ingresan usuario y contraseña", async () => {
    const user = userEvent.setup();
    const onLogin = vi.fn();

    // mock manual de alert
    const alertMock = vi.fn();
    const originalAlert = window.alert;

    window.alert = alertMock;

    // mock de fetch para asegurarnos de que NO se llama
    globalThis.fetch = vi.fn() as any;

    render(<ModalLogin onLogin={onLogin} />);

    const botonIngresar = screen.getByRole("button", { name: /ingresar/i });
    await user.click(botonIngresar);

    expect(alertMock).toHaveBeenCalledWith(
      "Debes ingresar usuario y contraseña"
    );
    expect(globalThis.fetch).not.toHaveBeenCalled();
    expect(onLogin).not.toHaveBeenCalled();

    // restaurar alert
    window.alert = originalAlert;
  });

  it("realiza login exitoso y llama a onLogin con el correo", async () => {
    const user = userEvent.setup();
    const onLogin = vi.fn();

    // mock de fetch con respuesta OK
    globalThis.fetch = vi.fn().mockResolvedValue({
      ok: true,
      status: 200,
      text: vi.fn().mockResolvedValue("OK"),
    } as any);

    render(<ModalLogin onLogin={onLogin} />);

    const inputUsuario = screen.getByLabelText(/usuario \(correo\)/i);
    const inputPassword = screen.getByLabelText(/contraseña/i);
    const botonIngresar = screen.getByRole("button", { name: /ingresar/i });

    await user.type(inputUsuario, "admin@pancomido.cl");
    await user.type(inputPassword, "secreto123");
    await user.click(botonIngresar);

    await waitFor(() => {
      expect(globalThis.fetch).toHaveBeenCalledTimes(1);
      expect(onLogin).toHaveBeenCalledWith("admin@pancomido.cl");
    });
  });

  it("muestra alerta si el servicio responde 401 (credenciales inválidas)", async () => {
    const user = userEvent.setup();
    const onLogin = vi.fn();

    // mock manual de alert
    const alertMock = vi.fn();
    const originalAlert = window.alert;

    window.alert = alertMock;

    // mock de fetch con 401
    globalThis.fetch = vi.fn().mockResolvedValue({
      ok: false,
      status: 401,
      text: vi.fn().mockResolvedValue("Unauthorized"),
    } as any);

    render(<ModalLogin onLogin={onLogin} />);

    const inputUsuario = screen.getByLabelText(/usuario \(correo\)/i);
    const inputPassword = screen.getByLabelText(/contraseña/i);
    const botonIngresar = screen.getByRole("button", { name: /ingresar/i });

    await user.type(inputUsuario, "admin@pancomido.cl");
    await user.type(inputPassword, "malapass");
    await user.click(botonIngresar);

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalledWith(
        "Usuario o contraseña incorrectos"
      );
      expect(onLogin).not.toHaveBeenCalled();
    });

    // restaurar alert
    window.alert = originalAlert;
  });
});
