import { useState } from "react";
import type { ImagePanType } from "../types/ImagePanType";



// Hook personalizado para gestionar el carrito de compras
export const useCarrito = () => {
    // Estado para los ítems en el carrito
  const [carro, setCart] = useState<ImagePanType[]>([]);


    // Funciones para agregar, eliminar y vaciar el carrito
  const agregar = (item: ImagePanType) => setCart(prev => [...prev, item]);

  // Elimina un ítem por su id
  const eliminar = (id: number | string) => setCart(prev => prev.filter(i => i.id !== id));

  // Vacía el carrito
  const vaciar = () => setCart([]);


    // Retorna el estado del carrito y las funciones para manipularlo
  return { carro, agregar, eliminar, vaciar };
};
