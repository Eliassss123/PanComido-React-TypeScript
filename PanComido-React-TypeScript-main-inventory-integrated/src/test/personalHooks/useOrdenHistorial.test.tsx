// src/test/hooks/useOrdenHistorial.test.ts
import { describe, it, expect, vi } from "vitest";
import { renderHook, act } from "@testing-library/react";
import { useOrdenHistorial } from "../../personalHooks"; 
// ⬆️ ajusta a donde tengas el hook, por ejemplo:
// "../../hooks/useOrdenHistorial"

describe("useOrdenHistorial", () => {
  const imagesMock = [
    {
      id: 1,
      titulo: "Marraqueta",
      precio: 1000,
      url: "https://example.com/marraqueta.jpg",
      categoria: "Blanco",
    },
    {
      id: 2,
      titulo: "Pan de Ajo",
      precio: 1500,
      url: "https://example.com/pandeajo.jpg",
      categoria: "Salado",
    },
    {
      id: 3,
      titulo: "Pan Integral",
      precio: 1200,
      url: "https://example.com/integral.jpg",
      categoria: "Integral",
    },
  ] as any[];

  it("inicia con historial vacío", () => {
    const funcAbrirModal = vi.fn();

    const { result } = renderHook(() =>
      useOrdenHistorial(imagesMock, funcAbrirModal)
    );

    expect(result.current.busquedasPrevias).toEqual([]);
  });

  it("no actualiza historial ni abre modal si el query está vacío o con solo espacios", () => {
    const funcAbrirModal = vi.fn();

    const { result } = renderHook(() =>
      useOrdenHistorial(imagesMock, funcAbrirModal)
    );

    act(() => {
      result.current.funcOrdenHistorial("");
      result.current.funcOrdenHistorial("   ");
    });

    expect(result.current.busquedasPrevias).toEqual([]);
    expect(funcAbrirModal).not.toHaveBeenCalled();
  });

  it("agrega el término al historial y abre modal cuando hay coincidencia exacta", () => {
    const funcAbrirModal = vi.fn();

    const { result } = renderHook(() =>
      useOrdenHistorial(imagesMock, funcAbrirModal)
    );

    act(() => {
      result.current.funcOrdenHistorial("Marraqueta");
    });

    expect(result.current.busquedasPrevias).toEqual(["Marraqueta"]);
    expect(funcAbrirModal).toHaveBeenCalledTimes(1);
    expect(funcAbrirModal).toHaveBeenCalledWith(
      expect.objectContaining({ titulo: "Marraqueta" })
    );
  });

  it("si no hay coincidencia exacta, busca coincidencia parcial y abre modal", () => {
    const funcAbrirModal = vi.fn();

    const { result } = renderHook(() =>
      useOrdenHistorial(imagesMock, funcAbrirModal)
    );

    act(() => {
      result.current.funcOrdenHistorial("ajo"); // debería machacar con "Pan de Ajo"
    });

    expect(result.current.busquedasPrevias).toEqual(["ajo"]);
    expect(funcAbrirModal).toHaveBeenCalledTimes(1);
    expect(funcAbrirModal).toHaveBeenCalledWith(
      expect.objectContaining({ titulo: "Pan de Ajo" })
    );
  });

  it("mantiene el historial sin duplicados y con un máximo de 5 términos", () => {
    const funcAbrirModal = vi.fn();

    const { result } = renderHook(() =>
      useOrdenHistorial(imagesMock, funcAbrirModal)
    );

    act(() => {
      result.current.funcOrdenHistorial("a");
      result.current.funcOrdenHistorial("b");
      result.current.funcOrdenHistorial("c");
      result.current.funcOrdenHistorial("d");
      result.current.funcOrdenHistorial("e");
      result.current.funcOrdenHistorial("f"); // aquí ya debería cortar a 5
      result.current.funcOrdenHistorial("c"); // mover "c" al frente, sin duplicar
    });

    // Se guardan solo los últimos 5 distintos, más reciente primero
    expect(result.current.busquedasPrevias).toEqual(["c", "f", "e", "d", "b"]);
  });
});
