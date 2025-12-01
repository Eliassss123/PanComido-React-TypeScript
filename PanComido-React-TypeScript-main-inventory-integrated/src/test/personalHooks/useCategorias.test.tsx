// src/test/hooks/useCategorias.test.ts
import { describe, it, expect } from "vitest";
import { renderHook, act } from "@testing-library/react";
import { useCategorias } from "../../personalHooks"; 
// 🔧 ajusta la ruta si tu hook está en otro lado:
// ej: "../../hooks/useCategorias" o similar

describe("useCategorias", () => {
  const itemsMock = [
    {
      id: 1,
      titulo: "Marraqueta",
      precio: 1000,
      url: "https://example.com/marraqueta.jpg",
      categoria: "Blanco",
    },
    {
      id: 2,
      titulo: "Hallulla",
      precio: 900,
      url: "https://example.com/hallulla.jpg",
      categoria: "Blanco",
    },
    {
      id: 3,
      titulo: "Pan integral",
      precio: 1200,
      url: "https://example.com/integral.jpg",
      categoria: "Integral",
    },
  ] as any[];

  const categoriasMock = ["Blanco", "Integral", "Salado"];

  it("inicia sin categoría seleccionada y muestra todos los items", () => {
    const { result } = renderHook(() =>
      useCategorias(itemsMock, categoriasMock)
    );

    expect(result.current.categoriaSeleccionada).toBeNull();
    expect(result.current.itemsFiltrados).toEqual(itemsMock);
    expect(result.current.categorias).toEqual(categoriasMock);
  });

  it("filtra los items por la categoría seleccionada", () => {
    const { result } = renderHook(() =>
      useCategorias(itemsMock, categoriasMock)
    );

    act(() => {
      result.current.setCategoriaSeleccionada("Blanco");
    });

    const filtrados = result.current.itemsFiltrados;
    expect(filtrados).toHaveLength(2);
    expect(filtrados.every((i) => i.categoria === "Blanco")).toBe(true);
  });

  it("si se selecciona 'Integral', solo muestra los de esa categoría", () => {
    const { result } = renderHook(() =>
      useCategorias(itemsMock, categoriasMock)
    );

    act(() => {
      result.current.setCategoriaSeleccionada("Integral");
    });

    const filtrados = result.current.itemsFiltrados;
    expect(filtrados).toHaveLength(1);
    expect(filtrados[0].titulo).toBe("Pan integral");
  });

  it("al volver a categoriaSeleccionada = null muestra todos los items otra vez", () => {
    const { result } = renderHook(() =>
      useCategorias(itemsMock, categoriasMock)
    );

    act(() => {
      result.current.setCategoriaSeleccionada("Blanco");
    });

    expect(result.current.itemsFiltrados).toHaveLength(2);

    act(() => {
      result.current.setCategoriaSeleccionada(null);
    });

    expect(result.current.itemsFiltrados).toEqual(itemsMock);
  });
});
