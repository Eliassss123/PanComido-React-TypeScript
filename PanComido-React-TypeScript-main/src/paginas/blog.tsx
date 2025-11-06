import React from "react";
import type { ImagePanType } from "../types/ImagePanType";

export const Blog: React.FC = () => {
  const blogImage: ImagePanType = {
    id: 1,
    titulo: "  ",
    precio: 0,
    url: "/images/Blog1.jpg"
  };

  return (
    <div style={{ textAlign: "center", marginTop: "20px" }}>
      <h2>{blogImage.titulo}</h2>
      <a href={blogImage.url} target="_blank" rel="noopener noreferrer">
        <img src={blogImage.url} alt={blogImage.titulo} style={{ maxWidth: "100%", height: "auto" }} />
      </a>

    </div>
  );
};