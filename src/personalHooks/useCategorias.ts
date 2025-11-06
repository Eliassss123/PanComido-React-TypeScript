
import { useState } from "react";
import type { ImagePanType } from "../types";



// Hook personalizado para gestionar la selección de categorías y filtrar ítems
export const useCategorias = (items: ImagePanType[], categorias: string[]) => {
    // Estado para la categoría seleccionada
  const [categoriaSeleccionada, setCategoriaSeleccionada] = useState<string | null>(null);

  // Filtra los ítems según la categoría seleccionada
  const itemsFiltrados = categoriaSeleccionada
    ? items.filter(item => item.categoria === categoriaSeleccionada)
    : items;
    // Retorna la categoría seleccionada, función para cambiarla y los ítems filtrados
  return { categoriaSeleccionada, setCategoriaSeleccionada, itemsFiltrados, categorias };
};
