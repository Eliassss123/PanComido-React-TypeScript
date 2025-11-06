import React from "react";
import { Container, Row, Col, Image } from "react-bootstrap";

export const Nosotros: React.FC = () => {
return ( <Container className="py-5 text-black"> <Row className="align-items-center"> <Col md={6} className="mb-4 mb-md-0"> <Image
         src="https://tse1.mm.bing.net/th/id/OIP._JX21ulVynsX-fgSMwd3NwAAAA?rs=1&pid=ImgDetMain&o=7&rm=3"
         alt="Panadería Pan Comido"
         fluid
         rounded
       /> </Col> <Col md={6}> <h2 className="mb-3">Nuestra Historia</h2>
<p style={{ fontSize: "1.1rem", lineHeight: "1.7" }}>
En <strong>Pan Comido</strong> comenzamos hace más de 20 años con la
pasión por el pan artesanal. Cada día horneamos con dedicación,
mezclando recetas tradicionales con un toque moderno. Nuestra
misión es ofrecer pan fresco, natural y con el auténtico sabor de
casa. Gracias por ser parte de esta historia crujiente y deliciosa. </p> </Col> </Row> </Container>
);
};
