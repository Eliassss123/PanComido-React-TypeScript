import React from 'react';
import type { NavItemType } from '../NavBarType'; // para el archivo aparte


// Props para el componente Navbar
interface NavbarProps {
  items: NavItemType[];
}


// Componente Navbar que recibe una lista de ítems de navegación
export const Navbar: React.FC<NavbarProps> = ({ items }) => {
  return (
    // Barra de navegación fija en la parte superior
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
      <div className="container-fluid">
        <a className="navbar-brand" href="/">Inicio</a>
        <button
                className="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent"
                aria-expanded="false"
                aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icono"></span>
        </button>
        {/* Contenedor colapsable para los items de navegacion */}
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav ms-auto">
            {items.map((item, index) => (
              // Mapea cada item a un enlace de navegación
              <li className="nav-item" key={index}>
                <a className="nav-link" href={item.URL}>{item.cadenasVisibles}</a>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </nav>
  );
};