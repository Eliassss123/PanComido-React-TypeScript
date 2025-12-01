// src/test/hooks/useAuth.test.ts
import { describe, it, expect, beforeEach } from "vitest";
import { renderHook, act } from "@testing-library/react";
import { useAuth } from "../../personalHooks"; 
// 🔧 si el hook está en otra ruta, por ejemplo:
// "../../hooks/useAuth"

describe("useAuth", () => {
  beforeEach(() => {
    localStorage.clear();
  });

  it("inicia desautenticado si no hay datos en localStorage", () => {
    const { result } = renderHook(() => useAuth());

    expect(result.current.isAuthenticated).toBe(false);
    expect(result.current.correoUsuario).toBeNull();
    expect(result.current.rol).toBeNull();
  });

  it("carga el estado inicial desde localStorage si ya había sesión", () => {
    localStorage.setItem("auth", "true");
    localStorage.setItem("correoUsuario", "user@pancomido.cl");
    localStorage.setItem("rol", "ADMIN");

    const { result } = renderHook(() => useAuth());

    expect(result.current.isAuthenticated).toBe(true);
    expect(result.current.correoUsuario).toBe("user@pancomido.cl");
    expect(result.current.rol).toBe("ADMIN");
  });

  it("login con correo normal asigna rol USER y guarda en localStorage", () => {
    const { result } = renderHook(() => useAuth());

    act(() => {
      result.current.login("cliente@pancomido.cl");
    });

    expect(result.current.isAuthenticated).toBe(true);
    expect(result.current.correoUsuario).toBe("cliente@pancomido.cl");
    expect(result.current.rol).toBe("USER");

    expect(localStorage.getItem("auth")).toBe("true");
    expect(localStorage.getItem("correoUsuario")).toBe("cliente@pancomido.cl");
    expect(localStorage.getItem("rol")).toBe("USER");
  });

  it("login con correo de admin asigna rol ADMIN (case-insensitive)", () => {
    const { result } = renderHook(() => useAuth());

    act(() => {
      result.current.login("ADMIN@PANCOMIDO.CL"); // mayúsculas para probar normalización
    });

    expect(result.current.isAuthenticated).toBe(true);
    expect(result.current.correoUsuario).toBe("ADMIN@PANCOMIDO.CL");
    expect(result.current.rol).toBe("ADMIN");

    expect(localStorage.getItem("rol")).toBe("ADMIN");
  });

  it("logout limpia estado y localStorage", () => {
    const { result } = renderHook(() => useAuth());

    // primero logueamos
    act(() => {
      result.current.login("cliente@pancomido.cl");
    });

    expect(result.current.isAuthenticated).toBe(true);

    // luego hacemos logout
    act(() => {
      result.current.logout();
    });

    expect(result.current.isAuthenticated).toBe(false);
    expect(result.current.correoUsuario).toBeNull();
    expect(result.current.rol).toBeNull();

    expect(localStorage.getItem("auth")).toBeNull();
    expect(localStorage.getItem("correoUsuario")).toBeNull();
    expect(localStorage.getItem("rol")).toBeNull();
  });
});
