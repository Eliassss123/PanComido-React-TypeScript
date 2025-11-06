import 'bootstrap/dist/css/bootstrap.min.css';
import { useState } from 'react';
import { Navbar, Nav, Container, Modal, Button } from "react-bootstrap";
import { BusquedasPrevias } from "./imagesComponents/BusquedasPrevias";
import { CustomHeader, SearchBar } from './ComponentesCompartidos';
import { ImagesList } from './imagesComponents/ImagesList';
import type { ImagePanType } from './types/ImagePanType';
import { panes } from './mock-data/panesMocks'; 
import { Blog } from "./paginas/blog";
import { Nosotros } from "./paginas/Nosotros";
import { Footer } from "./ComponentesCompartidos/Footer";
import { Carro } from './ComponentesCompartidos/CarritoPropiedades';
import { Recetas } from "./paginas/recetas";



export const ImagesApp: React.FC = () => {
  const [activeTab, setActiveTab] = useState<"recetas"|"blog" |"nosotros"| "panes" | "carrito">("panes");
  const [carro, setCart] = useState<ImagePanType[]>([]);
  const [panSeleccionado, setPanSeleccionado] = useState<ImagePanType | null>(null);
  const [mostrarVentanilla, setMostrarVentanilla] = useState(false);
  const [busquedasPrevias, setBusquedasPrevias] = useState<string[]>([]);



  const funcEliminarItem = (id: number | string) => {
  setCart(prev => prev.filter(item => item.id !== id));
};


  const funcComprarItem = (item: ImagePanType) => {
    setCart(prev => [...prev, item]);
  };

  const funcAbrirModal = (product: ImagePanType) => {
    setPanSeleccionado(product);
    setMostrarVentanilla(true);
  };

  const funcCerrarModal = () => {
    setPanSeleccionado(null);
    setMostrarVentanilla(false);
  };

  const images: ImagePanType[] = panes.map((pan) => ({
    id: pan.id,
    titulo: pan.name,
    url: pan.avatar,
    precio: Math.floor(Math.random() * 4000) + 1000,
  }));

  const funcOrdenHistorial = (query: string) => {
    if (!query.trim()) return;
    setBusquedasPrevias(prev => [query, ...prev.filter(t => t !== query)].slice(0, 5));
  };

return (
  <div>
  {/* Navbar */}
  <Navbar bg="dark" variant="dark" expand="lg">
    <Container>
      <Navbar.Brand>Pan Comido</Navbar.Brand>
      <Nav className="me-auto">
        <Nav.Link onClick={() => setActiveTab("panes")}>Galería</Nav.Link>
        <Nav.Link onClick={() => setActiveTab("blog")}>Blog</Nav.Link>
        <Nav.Link onClick={() => setActiveTab("carrito")}>Carrito</Nav.Link>
        <Nav.Link onClick={() => setActiveTab("nosotros")}>Historia</Nav.Link>
        <Nav.Link onClick={() => setActiveTab("recetas")}>Recetas</Nav.Link>
      </Nav>
    </Container>
  </Navbar>

  {/* Contenido */}
  <Container className="text-center mt-4">
    {activeTab === "blog" && <Blog />}
    {activeTab === "recetas" && <Recetas />}
    {activeTab === "nosotros" && <Nosotros />}
    {activeTab === "panes" && (
      <>
        <CustomHeader title="Pan Comido" text="Bienvenidos a la panadería" />
        <SearchBar textoDifuminado="Busque su producto" onQuery={funcOrdenHistorial} />
        <BusquedasPrevias busquedas={busquedasPrevias} onLabelClicked={funcOrdenHistorial} />
        <ImagesList images={images} onBuy={funcComprarItem} onView={funcAbrirModal} />
      </>
    )}



{activeTab === "carrito" && (
  <Container style={{ marginTop: "20px" }}>
    <h3>🛍️ Carrito: {carro.length} producto(s)</h3>
    <Carro
      carrito={carro} 
      onRemove={(id: number | string) => setCart(prev => prev.filter(item => item.id !== id))}

    />
  </Container>
)}





    <Modal show={mostrarVentanilla} onHide={funcCerrarModal} centered>
      <Modal.Header closeButton>
        <Modal.Title>{panSeleccionado?.titulo}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <img src={panSeleccionado?.url} alt={panSeleccionado?.titulo} className="img-fluid rounded" />
        <p>Precio: ${panSeleccionado?.precio}</p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={funcCerrarModal}>Cerrar</Button>
        <Button variant="primary" onClick={() => { if (panSeleccionado) funcComprarItem(panSeleccionado); funcCerrarModal(); }}>
          Comprar
        </Button>
      </Modal.Footer>
    </Modal>
  </Container>

  {/* Footer normal al final */}
  <Footer />
</div>
);
};