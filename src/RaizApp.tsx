import 'bootstrap/dist/css/bootstrap.min.css';
import { useState } from 'react';
import { Navbar, Nav, Container, Modal, Button, DropdownButton, Dropdown } from "react-bootstrap";
import { BusquedasPrevias } from "./imagesComponents/BusquedasPrevias";
import { ImagesList } from './imagesComponents/ImagesList';
import { panes } from './mock-data/panesJSON';
import { Blog } from "./paginas/blog";
import { Nosotros } from "./paginas/Nosotros";
import { Recetas } from "./paginas/recetas";
import type { ImagePanType } from './types';
import { ModalLogin, BotonCompra, Carro, Footer, CustomHeader, SearchBar } from './ComponentesCompartidos'; 
import { useCarrito, useOrdenHistorial, useAuth, useCategorias } from "./personalHooks";



// Componente principal
export const PanaderiaApp: React.FC = () => {
  const [activeTab, setActiveTab] = useState<"recetas" | "blog" | "nosotros" | "panes" | "carrito">("panes");
  const { carro, agregar, eliminar, vaciar } = useCarrito();
  const [panSeleccionado, setPanSeleccionado] = useState<ImagePanType | null>(null);
  const [mostrarVentanilla, setMostrarVentanilla] = useState(false);
  const { isAuthenticated, login: handleLogin, logout: handleLogout } = useAuth();

  // Alerta para mensaje de carrito
  const [mostrarAlerta, setMostrarAlerta] = useState(false);
  const [mensajeAlerta, setMensajeAlerta] = useState("");

  // Datos de panes
  const images: ImagePanType[] = panes.map((pan) => ({
    id: pan.id,
    titulo: pan.name,
    url: pan.avatar,
    precio: Math.floor(Math.random() * 4000) + 1000,
    categoria: pan.categoria || "Blanco",
  }));

  // Categorias
  const categorias = ["Integral", "Blanco", "Dulce", "Salado"];
  const { categoriaSeleccionada, setCategoriaSeleccionada, itemsFiltrados } = useCategorias(images, categorias);

  // Hook personalizado para la búsqueda
  const { busquedasPrevias, funcOrdenHistorial } = useOrdenHistorial(images, funcAbrirModal);

  // Funciones de modal
  function funcAbrirModal(product: ImagePanType) {
    setPanSeleccionado(product);
    setMostrarVentanilla(true);
  }


// Función para cerrar el modal
  function funcCerrarModal() {
    setPanSeleccionado(null);
    setMostrarVentanilla(false);
  }



  // Función para agregar producto al carrito con una alerta
  const handleAddToCart = (item: ImagePanType) => {
    agregar(item);
    setMensajeAlerta(`${item.titulo} añadido al carrito`);
    setMostrarAlerta(true);
    setTimeout(() => setMostrarAlerta(false), 2000);
  };



  // Si no esta logueado, mostrar modal de login
  if (!isAuthenticated) {
    return <ModalLogin onLogin={handleLogin} />;
  }

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
          <Button variant="outline-light" size="sm" onClick={handleLogout}>
            Cerrar sesión
          </Button>
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

            {/* Buscador + Categorías */}
            <div className="d-flex justify-content-center align-items-center gap-2 mb-3">
              <SearchBar textoDifuminado="Busque su producto" onQuery={funcOrdenHistorial} />


            {/* Desplegable*/}
              <DropdownButton
                id="dropdown-categorias"
                title={categoriaSeleccionada || "Categorías"}
                variant="secondary"
              >
                {categorias.map(cat => (
                  <Dropdown.Item key={cat} onClick={() => setCategoriaSeleccionada(cat)}>
                    {cat}
                  </Dropdown.Item>
                ))}
                <Dropdown.Item onClick={() => setCategoriaSeleccionada(null)}>Todas</Dropdown.Item>
              </DropdownButton>
            </div>

                {/* Etiquetas de búsquedas previas */}
            <BusquedasPrevias busquedas={busquedasPrevias} onLabelClicked={funcOrdenHistorial} />
            <ImagesList images={itemsFiltrados} onBuy={handleAddToCart} onView={funcAbrirModal} />
          </>
        )}


        {/* Carrito */}
        {activeTab === "carrito" && (
          <Container style={{ marginTop: "20px" }}>
            <h3>Carrito: {carro.length} producto(s)</h3>
            <Carro carrito={carro} onRemove={eliminar} />
            <BotonCompra carrito={carro} onVaciar={vaciar} />
          </Container>
        )}




        {/* Modal de producto */}
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
            <Button
              variant="primary"
              onClick={() => {
                if (panSeleccionado) handleAddToCart(panSeleccionado);
                funcCerrarModal();
              }}
            >
              Comprar
            </Button>
          </Modal.Footer>
        </Modal>




        {/* Alerta de Bootstrap */}
        <div
          className={`alerta position-fixed bottom-0 end-0 m-3 ${mostrarAlerta ? "show" : ""}`}
          role="alert"
          aria-live="assertive"
          aria-atomic="true"
          style={{ minWidth: "200px" }}
        >
          <div className="alerta-header">
            <strong className="me-auto">Carrito</strong>
            <button type="button" className="btn-cerrar" onClick={() => setMostrarAlerta(false)}></button>
          </div>
          <div className="alerta-body">{mensajeAlerta}</div>
        </div>
      </Container>

      {/* Footer */}
      <Footer />
    </div>
  );
};
