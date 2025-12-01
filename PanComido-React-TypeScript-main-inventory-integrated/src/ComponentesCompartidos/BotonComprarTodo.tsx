import React, { useState } from "react";
import { Button, Modal } from "react-bootstrap";
import type { ImagePanType } from "../types/ImagePanType";
import { SERVICE_URLS } from "../config/services";

interface BotonCompraProps {
  carrito: ImagePanType[];
  onVaciar: () => void;         // función para vaciar el carrito
  correoUsuario: string | null; // correo del usuario logueado (desde auth)
}

export function BotonCompra({
  carrito,
  onVaciar,
  correoUsuario,
}: BotonCompraProps) {
  const [mostrarModal, setMostrarModal] = useState(false);
  const [resumenBoleta, setResumenBoleta] = useState<any | null>(null);

  // 🔹 Ya NO hacemos "if (carrito.length === 0) return null;"
  const hayProductos = carrito.length > 0;

  const mapearCarritoAItemsDTO = () =>
    carrito.map((item) => ({
      // Id de producto que guarda el microservicio de productos
      productoId: (item as any).id,
      // Nombre del producto al momento de la compra
      nombreProducto:
        (item as any).nombre ?? (item as any).titulo ?? "Producto",
      // si no manejas cantidades en el front, dejamos 1
      cantidad: (item as any).cantidad ?? 1,
      // precio unitario del producto
      precioUnitario: (item as any).precio,
    }));

  const handleCompra = async () => {
    if (!correoUsuario) {
      alert("Debes iniciar sesión antes de realizar la compra.");
      return;
    }

    if (carrito.length === 0) {
      alert("No tienes productos en el carrito.");
      return;
    }

    try {
      // 1) Construir el DTO que espera el microservicio de pedidos
      const pedidoDTO = {
        correoUsuario,
        items: mapearCarritoAItemsDTO(),
      };

      // 2) Crear el pedido en el microservicio (OrderServices - puerto 8084)
      const respPedido = await fetch(
        `${SERVICE_URLS.orders}/api/pedidos/front`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(pedidoDTO),
        }
      );

      if (!respPedido.ok) {
        console.error("Error al crear el pedido:", await respPedido.text());
        alert("Hubo un problema al crear el pedido 😥");
        return;
      }

      const pedidoCreado = await respPedido.json();
      const pedidoId = pedidoCreado.id;

      // 3) Pedir el resumen de boleta al microservicio de boleta
      const respBoleta = await fetch(
        `${SERVICE_URLS.orders}/api/boletas/resumen/${pedidoId}`
      );

      if (!respBoleta.ok) {
        console.error("Error al obtener la boleta:", await respBoleta.text());
        alert(
          "El pedido se creó, pero hubo un problema al generar la boleta 😥"
        );
        return;
      }

      const resumen = await respBoleta.json();

      // 4) Guardar el resumen en estado y mostrar modal
      const total = resumen.total ?? 0;
      const cantidadTotal = resumen.cantidadTotal ?? carrito.length;

      setResumenBoleta({
        ...resumen,
        total,
        cantidadTotal,
      });
      setMostrarModal(true);

      // 5) Vaciar el carrito en el front
      onVaciar();
    } catch (error) {
      console.error("Error en la compra:", error);
      alert("Ocurrió un error al procesar la compra. Intenta nuevamente.");
    }
  };

  return (
    <>
      {/* Botón solo si hay productos */}
      {hayProductos && (
        <div className="text-center mt-4">
          <Button variant="success" size="lg" onClick={handleCompra}>
            Realizar Compra
          </Button>
        </div>
      )}

      {/* Modal de boleta SIEMPRE montado, controlado por mostrarModal */}
      <Modal
        show={mostrarModal}
        onHide={() => setMostrarModal(false)}
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>Boleta de compra</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {resumenBoleta ? (
            <div>
              <p>
                <strong>N° Pedido:</strong> {resumenBoleta.pedidoId}
              </p>
              <p>
                <strong>Correo:</strong> {resumenBoleta.correoUsuario}
              </p>
              <p>
                <strong>Fecha:</strong>{" "}
                {resumenBoleta.fecha
                  ? new Date(resumenBoleta.fecha).toLocaleString()
                  : ""}
              </p>
              <p>
                <strong>Total productos:</strong>{" "}
                {resumenBoleta.cantidadTotal}
              </p>
              <p>
                <strong>Total a pagar:</strong>{" "}
                {"$" +
                  (resumenBoleta.total?.toLocaleString?.() ??
                    resumenBoleta.total)}
              </p>
            </div>
          ) : (
            <p>Cargando boleta...</p>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setMostrarModal(false)}>
            Cerrar
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};
