
import 'bootstrap/dist/css/bootstrap.min.css';

import { useState } from 'react';
import { Navbar } from "./ComponentesCompartidos/Navbar";
import { BusquedasPrevias } from "./imagesComponents/BusquedasPrevias";
import { CustomHeader, SearchBar } from './ComponentesCompartidos';
import { ImagesList } from './imagesComponents/ImagesList';
import { mockGifs } from './mock-data';
import type { ImagePanType } from './types/ImagePanType';
import { Modal, Button } from "react-bootstrap";


import './index.css';

import { panes } from './mock-data/panesMocks'; 
import type{ panesPropiedades } from './mock-data/panesMocks'; // opcional si necesitas el tipo



export const ImagesApp: React.FC = () => {
  const [cart, setCart] = useState<ImagePanType[]>([]);
  const [panSeleccionado, setPanSeleccionado] = useState<ImagePanType | null>(null);
  const [mostrarVentanilla, setMostrarVentanilla] = useState(false);


  const [busquedasPrevias, setBusquedasPrevias] = useState<string[]>([]);
    const handleBuy = (item: ImagePanType) => {
      setCart((prev) => {
        const newCart = [...prev, item];
        console.log("🛒 Carrito actual:", newCart);
        return newCart;
      });
    };


      // Función para abrir el modal con el producto seleccionado
        const funcAbrirModal = (product: ImagePanType) => {
          setPanSeleccionado(product);
          setMostrarVentanilla(true);
        };

        // Función para cerrar el modal
        const funcCerrarModal = () => {
          setPanSeleccionado(null);
          setMostrarVentanilla(false);
        };




    const images: ImagePanType[] = panes.map((pan) => ({
      id: pan.id,
      title: pan.name,
      url: pan.avatar,
      price: Math.floor(Math.random() * 4000) + 1000, // 💸 Precio entre 1000 y 5000
    }));


  const handleTermClicked = (term: string) => {
    console.log({ term });
  };

  const funcOrdenHistorial = (query: string) => {
  if (!query.trim()) return; // evitar strings vacíos

  setBusquedasPrevias(prev => {
    // Si ya existe, moverlo al inicio
    const filtrado = prev.filter(term => term !== query);
    // Agregar al inicio
    const nuevo = [query, ...filtrado];
    // Limitar a máximo 5 elementos
    return nuevo.slice(0, 5);
  });

    console.log(query);
  };



  return (

    <div style={{ textAlign: "center", padding: "20px" }}>
          <Navbar
        items={[
          { cadenasVisibles: "Blog", URL: "/blog" },
          { cadenasVisibles: "Galería", URL: "/panes" },
          { cadenasVisibles: "Carrito", URL: "/carrito" },
        ]}
      />
      <CustomHeader
        title="'"
        text="Bienvenidos a la panaderia Pan Comido"
      />

      <SearchBar
        textoDifuminado="Busque su producto"
        onQuery={funcOrdenHistorial}
      />

      <BusquedasPrevias
        busquedas={busquedasPrevias}
        onLabelClicked={funcOrdenHistorial}
      />

      <ImagesList images={images} onBuy={handleBuy} onView={funcAbrirModal} />

      <div style={{ marginTop: "30px" }}>
        <h3>🛍️ Carrito: {cart.length} producto(s)</h3>
      </div>
              <Modal show={mostrarVentanilla} onHide={funcCerrarModal} centered>
          <Modal.Header closeButton>
            <Modal.Title>{panSeleccionado?.title}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <img 
              src={panSeleccionado?.url} 
              alt={panSeleccionado?.title} 
              className="img-fluid rounded" 
            />
            <p>Precio: ${panSeleccionado?.price}</p>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={funcCerrarModal}>
              Cerrar
            </Button>
            <Button variant="primary" onClick={() => {
              if(panSeleccionado) handleBuy(panSeleccionado);
              funcCerrarModal();
            }}>
              Comprar
            </Button>
          </Modal.Footer>
        </Modal>
    </div>



    
  );
};
