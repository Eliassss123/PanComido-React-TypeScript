import React from "react";
import { Container, Button, Card, Row, Col } from "react-bootstrap";
import type { ImagePanType } from "../types/ImagePanType";


// Props para el componente Carro
interface CarritoPropiedades {
  carrito: ImagePanType[];
  onRemove: (id: number | string) => void;
}


// Componente que muestra los ítems en el carrito de compras
export const Carro: React.FC<CarritoPropiedades> = ({ carrito, onRemove }) => {
  if (carrito.length === 0) {
    return (
      <Container className="text-center my-5">
        <img 
          src="https://img.freepik.com/premium-vector/cartoon-chef-holding-spoon-knife_697880-25429.jpg?w=1060" 
          alt="Carrito vacío" 
          className="img-fluid mb-3"
          style={{ maxWidth: "300px" }}
        />
        <h5> Tu carrito está vacío</h5>
      </Container>
    );
  }

  // Muestra los ítems en el carrito
  return (
    <Row className="g-3">
      {carrito.map((item, index) => (
        <Col key={`${item.id}-${index}`} xs={12} md={6} lg={4}>
          <Card>
            {/* Imagen del producto */}
            <Card.Img variant="top" src={item.url} alt={item.titulo} />
            <Card.Body>
              <Card.Title>{item.titulo}</Card.Title>
              <Card.Text>Precio: ${item.precio}</Card.Text>
              <Button
                variant="danger"
                onClick={() => onRemove(item.id)}
              >
                Eliminar
              </Button>
            </Card.Body>
          </Card>
        </Col>
      ))}
    </Row>
  );
};
