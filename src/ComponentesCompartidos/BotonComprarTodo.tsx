import React from "react";
import { Button } from "react-bootstrap";
import type { ImagePanType } from "../types/ImagePanType";


// Props para el botón de compra
interface BotonCompraProps {
  carrito: ImagePanType[];
  onVaciar: () => void; // función para vaciar el carrito
}


// Componente de botón para comprar todos los ítems en el carrito
export const BotonCompra: React.FC<BotonCompraProps> = ({ carrito, onVaciar }) => {
  if (carrito.length === 0) return null; // no mostrar nada si no hay productos

  // Maneja la acción de compra
  const handleCompra = () => {
    const total = carrito.reduce((sum, item) => sum + item.precio, 0);
    alert(` ¡Has comprado ${carrito.length} producto(s) por un total de $${total.toLocaleString()} pesos!`);
    onVaciar(); // vacía el carrito
  };

  // Renderiza el botón de compra
  return (
    <div className="text-center mt-4">
        {/* Botón para realizar la compra */}
      <Button variant="success" size="lg" onClick={handleCompra}>
        Realizar Compra
      </Button>
    </div>
  );
};
