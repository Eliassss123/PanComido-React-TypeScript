// src/test/hooks/useCarrito.test.ts
import { describe, it, expect } from "vitest";
import { renderHook, act } from "@testing-library/react";
import { useCarrito } from "../../personalHooks"; 
// 🔧 ajusta la ruta:
// si el hook está en src/hooks/useCarrito.ts → "../../hooks/useCarrito"
// si está en src/personalHooks.ts → "../../personalHooks"

describe("useCarrito", () => {
  const itemMock = {
    id: 1,
    titulo: "Marraqueta",
    precio: 1000,
    url: "https://example.com/marraqueta.jpg",
    categoria: "Blanco",
  };

  it("inicia con el carro vacío", () => {
    const { result } = renderHook(() => useCarrito());

    expect(result.current.carro).toEqual([]);
  });

  it("agrega items al carrito con agregar()", () => {
    const { result } = renderHook(() => useCarrito());

    act(() => {
      result.current.agregar(itemMock as any);
    });

    expect(result.current.carro).toHaveLength(1);
    expect(result.current.carro[0]).toEqual(itemMock);
  });

  it("elimina un item del carrito con eliminar(id)", () => {
    const { result } = renderHook(() => useCarrito());

    act(() => {
      result.current.agregar(itemMock as any);
      result.current.agregar({ ...itemMock, id: 2, titulo: "Hallulla" } as any);
    });

    expect(result.current.carro).toHaveLength(2);

    act(() => {
      result.current.eliminar(1); // elimina el de id 1
    });

    expect(result.current.carro).toHaveLength(1);
    expect(result.current.carro[0].id).toBe(2);
  });

  it("vacía el carrito con vaciar()", () => {
    const { result } = renderHook(() => useCarrito());

    act(() => {
      result.current.agregar(itemMock as any);
      result.current.agregar({ ...itemMock, id: 2 } as any);
    });

    expect(result.current.carro).toHaveLength(2);

    act(() => {
      result.current.vaciar();
    });

    expect(result.current.carro).toEqual([]);
  });
});
