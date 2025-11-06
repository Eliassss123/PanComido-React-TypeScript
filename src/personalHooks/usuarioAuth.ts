import { useState } from "react";



export const useAuth = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(
    localStorage.getItem("auth") === "true"
  );

// Simula el estado de autenticación del usuario
  const login = () => {
    setIsAuthenticated(true);
    localStorage.setItem("auth", "true");
  };
// Función para cerrar sesión
  const logout = () => {
    setIsAuthenticated(false);
    localStorage.removeItem("auth");
  };
  
  // Retorna el estado y las funciones de login/logout
  return { isAuthenticated, login, logout };
};
