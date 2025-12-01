import type { panesPropiedades } from '../mock-data/panesJSON';
import React from "react";
import type { ImagePanType } from "../types/ImagePanType";


// Props para los panes
interface ListaPanesPropiedades {
  panes: panesPropiedades[];
}

// Props para imagenes
interface ListaImagenesPropiedades {
  images: ImagePanType[];
  onBuy: (item: ImagePanType) => void;
  onView: (item: ImagePanType) => void; 
}
// Componente de lista de imagenes
export const ImagesList: React.FC<ListaImagenesPropiedades> = ({ images, onBuy, onView }) => {
  return (
    <div className="d-flex flex-wrap gap-3 justify-content-center">
      {images.map(img => (
        <div key={img.id} style={{ position: "relative", width: 200, height: 200, overflow: "hidden", borderRadius: 10 }}>
          {/* Imagen del producto */}
          <img
            src={img.url}
            alt={img.titulo}
            className="img-fluid w-100 h-100 rounded"
            style={{ objectFit: "cover", cursor: "pointer" }}
            onClick={() => onView(img)} // abrir modal
          />

          {/* Botón de comprar */}
          <button
            type="button"
            className="btn btn-dark position-absolute start-50 translate-middle-x bottom-0 mb-2"
            onClick={e => {
              e.stopPropagation(); // evita abrir modal al comprar
              onBuy(img);
            }}
          >
            Comprar
          </button>
        </div>
      ))}
    </div>
  );
};


