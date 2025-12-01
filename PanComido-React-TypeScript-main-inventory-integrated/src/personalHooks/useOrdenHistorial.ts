import { useState } from "react";
import type { ImagePanType } from "../types/ImagePanType";
import { SERVICE_URLS } from "../config/services";

//Hook personalizado para gestionar el historial de búsquedas y abrir modales 
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


      async function cargarHistorial(correoUsuario: string) {
      const resp = await fetch(
        `${SERVICE_URLS.orders}/api/pedidos/usuario/${encodeURIComponent(
          correoUsuario
        )}`
      );
      if (!resp.ok) throw new Error("Error al obtener historial de pedidos");
      const pedidos = await resp.json();
      return pedidos;
    }
};
