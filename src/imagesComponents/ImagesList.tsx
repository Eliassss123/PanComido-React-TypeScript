import type { panesPropiedades } from '../mock-data/panesMocks';
import React from "react";
import type { ImagePanType } from "../types/ImagePanType";
import {panes} from '../mock-data/panesMocks';





// Props para robots
interface ListaPanesPropiedades {
  panes: panesPropiedades[];
}

// Props para imágenes
interface ListaImagenesPropiedades {
  images: ImagePanType[];
  onBuy: (item: ImagePanType) => void;
  onView: (item: ImagePanType) => void; // nueva prop
}

// Componente de lista de imágenes
export const ImagesList: React.FC<ListaImagenesPropiedades> = ({ images, onBuy, onView }) => {
  return (
    <div className="d-flex flex-wrap gap-3 justify-content-center">
      {images.map(img => (
        <div key={img.id} style={{ position: "relative", width: 200, height: 200, overflow: "hidden", borderRadius: 10 }}>
          <img
            src={img.url}
            alt={img.title}
            className="img-fluid w-100 h-100 rounded"
            style={{ objectFit: "cover", cursor: "pointer" }}
            onClick={() => onView(img)} // 🔹 abrir modal
          />
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


// Componente de lista de robots
export const ListaPanes: React.FC<ListaPanesPropiedades> = ({ panes }) => {
  return (
    <div className="gifs-container">
      {panes.map((pan) => (
        <div key={pan.id} className="gif-card">
          <img src={pan.avatar} alt={pan.name} />
          <h3>{pan.name}</h3>
        </div>
      ))}
    </div>
  );
};
