import { Container, Row, Col } from "react-bootstrap";

export const Footer: React.FC = () => {
  return (
    <footer className="bg-dark text-white py-4 mt-auto">
      <Container>
        <Row className="align-items-center">

          {/* Columna central: contacto */}
          <Col md={4} className="text-start">
            <p>☎ Teléfono: +56 9 7291 1389</p>
            <p>Direccion: Av.San Ignacio 3318</p>
            <p>Comuna: Quilicura</p>
          </Col>

          {/* Columna izquierda: Título */}
          <Col md={4} className="text-center">
            <h3>Pan Comido</h3>
            <p>La mejor panadería de la ciudad</p>
            <small>Todos los derechos reservados</small>
          </Col>



          {/* Columna derecha: correo */}
          <Col md={4} className="text-end">
            <p>🌐 Facebook </p>
            <p>🌐 Instagram </p>
            <p>🌐 Twitter  </p>
          </Col>
        </Row>
      </Container>
    </footer>
  );
};
