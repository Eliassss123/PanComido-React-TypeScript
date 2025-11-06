import { useState } from "react";
import type { ImagePanType } from "../types/ImagePanType";









{/*  Hook personalizado para gestionar el historial de búsquedas y abrir modales */}
interface UseOrdenHistorialReturn {
  busquedasPrevias: string[];
  funcOrdenHistorial: (query: string) => void;
}





export const useOrdenHistorial = (
  images: ImagePanType[],
  funcAbrirModal: (item: ImagePanType) => void
): UseOrdenHistorialReturn => {
  const [busquedasPrevias, setBusquedasPrevias] = useState<string[]>([]);


  // Función para manejar la búsqueda y actualizar el historial
  const funcOrdenHistorial = (query: string) => {
    if (!query.trim()) return;

    // Actualiza historial
    setBusquedasPrevias(prev => [query, ...prev.filter(t => t !== query)].slice(0, 5));

    // Busca coincidencias exactas o parciales
    const textoIngresado = query.trim().toLowerCase();
    let encontrado = images.find(img => img.titulo.toLowerCase() === textoIngresado);
    if (!encontrado) {
      encontrado = images.find(img => img.titulo.toLowerCase().includes(textoIngresado));
    }

    // Si encontro un producto, abre el modal
    if (encontrado) {
      funcAbrirModal(encontrado);
    }
  };

  // Retorna el historial y la función de búsqueda
  return { busquedasPrevias, funcOrdenHistorial };
};
