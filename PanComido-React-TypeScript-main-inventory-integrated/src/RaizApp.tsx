import 'bootstrap/dist/css/bootstrap.min.css';
import { useState, useEffect } from 'react';
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
import { SERVICE_URLS } from "./config/services";



// Componente principal
export const PanaderiaApp: React.FC = () => {
  const [activeTab, setActiveTab] = useState<"recetas" | "blog" | "nosotros" | "panes" | "carrito">("panes");
  const { carro, agregar, eliminar, vaciar } = useCarrito();
  const [panSeleccionado, setPanSeleccionado] = useState<ImagePanType | null>(null);
  const [mostrarVentanilla, setMostrarVentanilla] = useState(false);



  const { isAuthenticated, correoUsuario, rol, login: handleLogin, logout: handleLogout } = useAuth();

  const [mostrarModalReporte, setMostrarModalReporte] = useState(false);
  const [reporteData, setReporteData] = useState<any | null>(null);
  const [cargandoReporte, setCargandoReporte] = useState(false);


  // Alerta para mensaje de carrito
  const [mostrarAlerta, setMostrarAlerta] = useState(false);
  const [mensajeAlerta, setMensajeAlerta] = useState("");

  // Datos de panes
const [images, setImages] = useState<ImagePanType[]>(() =>
  panes.map((pan) => ({
    id: pan.id,
    titulo: pan.name,
    url: pan.avatar,
    precio: Math.floor(Math.random() * 4000) + 1000,
    categoria: pan.categoria || "Blanco",
  }))
);

useEffect(() => {
  const cargarDesdeApi = async () => {
    try {
      const resp = await fetch(`${SERVICE_URLS.inventory}/api/productos/front/listar`);

      console.log("Status backend:", resp.status);

      if (!resp.ok) {
        throw new Error(`HTTP error! status: ${resp.status}`);
      }

      const data: any = await resp.json();
      console.log("JSON que viene del backend:", data);

      // Soportar distintas formas de respuesta
      let lista: any[] = [];

      if (Array.isArray(data)) {
        // Caso típico: [ {...}, {...} ]
        lista = data;
      } else if (Array.isArray(data.content)) {
        // Caso Spring Data Page: { content: [ {...}, {...} ], ... }
        lista = data.content;
      } else if (Array.isArray(data.productos)) {
        // Caso: { productos: [ {...}, {...} ] }
        lista = data.productos;
      }

      if (lista.length > 0) {
        const adaptados: ImagePanType[] = lista.map((p: any) => ({
          id: p.id,
          // usa nombre si viene del backend, o titulo si ya viene así
          titulo: p.nombre || p.titulo || "Sin título",
          // intenta usar el campo de imagen del backend, sino cae a url, sino placeholder
          url:
            p.imagenUrl ||
            p.url ||
            "https://via.placeholder.com/300x200.png?text=Sin+Imagen",
          // si no trae precio numérico, le ponemos uno por defecto
          precio: typeof p.precio === "number"
            ? p.precio
            : Math.floor(Math.random() * 4000) + 1000,
          categoria: p.categoria || "Blanco",
        }));

        setImages(adaptados);
      } else {
        console.warn(
          "El backend respondió, pero no encontré lista de productos; usando mocks. Respuesta:",
          data
        );
        // Fallback a los mocks
        setImages(
          panes.map((pan) => ({
            id: pan.id,
            titulo: pan.name,
            url: pan.avatar,
            precio: Math.floor(Math.random() * 4000) + 1000,
            categoria: pan.categoria || "Blanco",
          }))
        );
      }
    } catch (error) {
      console.error("Error al llamar al backend, usando mocks:", error);
      // Fallback a los mocks si hay error (CORS, backend caído, etc.)
      setImages(
        panes.map((pan) => ({
          id: pan.id,
          titulo: pan.name,
          url: pan.avatar,
          precio: Math.floor(Math.random() * 4000) + 1000,
          categoria: pan.categoria || "Blanco",
        }))
      );
    }
  };

  cargarDesdeApi();
}, []);

  
  // Categorias
  const categorias = ["Integral", "Blanco", "Dulce", "Salado"];

  // Hook personalizado para categorías
  const { categoriaSeleccionada, setCategoriaSeleccionada,
         itemsFiltrados } = useCategorias(images, categorias);

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


  const handleReporte = async () => {
  console.log("handleReporte rol:", rol); // para ver qué llega

  if (rol !== "ADMIN") {
    alert("No tienes permiso para ver el reporte general.");
    return;
  }


    try {
      setCargandoReporte(true);
      const resp = await fetch(
        `${SERVICE_URLS.reports}/api/reportes/front/general`
      ); 
      if (!resp.ok) {
        alert("Error al obtener el reporte.");
        return;
      }

      const data = await resp.json();
      setReporteData(data);
      setMostrarModalReporte(true);
    } catch (error) {
      console.error("Error obteniendo reporte:", error);
      alert("Error de red al obtener el reporte.");
    } finally {
      setCargandoReporte(false);
    }
  };







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

      {/* 🔹 Nuevo botón de reporte */}
      <Nav.Link onClick={handleReporte}>Reporte</Nav.Link>
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



        {activeTab === "carrito" && (
          <Container style={{ marginTop: "20px" }}>
            <h3>Carrito: {carro.length} producto(s)</h3>
            <Carro carrito={carro} onRemove={eliminar} />
            <BotonCompra
              carrito={carro}
              onVaciar={vaciar}
              correoUsuario={correoUsuario || null}
            />
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




        {/* Alerta de confirmación */}

        <div
          aria-live="polite"
          aria-atomic="true"
          className="position-fixed bottom-0 end-0 p-3"
          style={{ zIndex: 11 }}
        >
          
          <div
            className={`toast align-items-center text-bg-success border-0 ${mostrarAlerta ? "show" : "hide"}`}
            role="alert"
            aria-live="assertive"
            aria-atomic="true"
          >
            {/* Cuerpo de la alerta */}
            <div className="d-flex">
              <div className="toast-body">
                {mensajeAlerta}
              </div>
              <button
                type="button"
                className="btn-close btn-close-white me-2 m-auto"
                data-bs-dismiss="toast"
                aria-label="Close"
                onClick={() => setMostrarAlerta(false)}
              ></button>
            </div>
          </div>
        </div>
      </Container>



      {/* Modal de reporte general */}
      <Modal
        show={mostrarModalReporte}
        onHide={() => setMostrarModalReporte(false)}
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title>Reporte general</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {cargandoReporte && <p>Cargando reporte...</p>}
          {!cargandoReporte && reporteData && (
            <div>
              <p>
                <strong>Fecha generación:</strong>{" "}
                {reporteData.fechaGeneracion
                  ? new Date(reporteData.fechaGeneracion).toLocaleString()
                  : ""}
              </p>
              <p>
                <strong>Total clientes:</strong> {reporteData.totalClientes}
              </p>
              <p>
                <strong>Total tiendas:</strong> {reporteData.totalTiendas}
              </p>
              <p>
                <strong>Total productos:</strong> {reporteData.totalProductos}
              </p>
            </div>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button
            variant="secondary"
            onClick={() => setMostrarModalReporte(false)}
          >
            Cerrar
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Footer */}
      <Footer />
    </div>
  );
};
