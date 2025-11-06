import React from "react";
import type { ImagePanType } from "../types/ImagePanType";

export const Recetas: React.FC = () => {


  // Datos de la imagen del recetario
  const recetasImage: ImagePanType = {
    id: 1,
    titulo: "  ",
    precio: 0,
    url: "/images/Recetario.jpg",
    categoria:""
  };


  // Renderiza la imagen del recetario con un enlace para descargarla
  return (
    <div style={{ textAlign: "center", marginTop: "20px" }}>
      <h2>{recetasImage.titulo}</h2>
      <a href={recetasImage.url} target="_blank" rel="noopener noreferrer">
        <img src={recetasImage.url} alt={recetasImage.titulo} style={{ maxWidth: "100%", height: "auto" }} />
      </a>

    </div>
  );
};