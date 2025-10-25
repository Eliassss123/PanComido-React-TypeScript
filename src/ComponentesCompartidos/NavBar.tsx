import React from 'react';
import type { NavItemType } from '../NavBarType'; // para el archivo aparte

interface NavbarProps {
  items: NavItemType[];
}

export const Navbar: React.FC<NavbarProps> = ({ items }) => {
  return (
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
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav ms-auto">
            {items.map((item, index) => (
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