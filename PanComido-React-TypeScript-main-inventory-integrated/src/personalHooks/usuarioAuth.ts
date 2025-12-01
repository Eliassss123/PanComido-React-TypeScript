import { useState } from "react";

// Lista de correos que serán tratados como ADMIN en el front
const ADMIN_EMAILS = [
  "admin@pancomido.cl",
  // agrega aquí el correo real de tu admin:
  // "miadmin@midominio.cl",
];

export const useAuth = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(
    localStorage.getItem("auth") === "true"
  );
  const [correoUsuario, setCorreoUsuario] = useState<string | null>(
    localStorage.getItem("correoUsuario")
  );
  const [rol, setRol] = useState<string | null>(
    localStorage.getItem("rol")
  );

  const login = (correo: string) => {
    const normalizado = correo.trim().toLowerCase();

    // 🔹 Aquí definimos si este correo es ADMIN o USER
    const rolDetectado = ADMIN_EMAILS.includes(normalizado) ? "ADMIN" : "USER";

    setIsAuthenticated(true);
    setCorreoUsuario(correo);
    setRol(rolDetectado);

    localStorage.setItem("auth", "true");
    localStorage.setItem("correoUsuario", correo);
    localStorage.setItem("rol", rolDetectado);

    console.log("Login en useAuth:", { correo, rolDetectado });
  };

  const logout = () => {
    setIsAuthenticated(false);
    setCorreoUsuario(null);
    setRol(null);
    localStorage.removeItem("auth");
    localStorage.removeItem("correoUsuario");
    localStorage.removeItem("rol");
  };

  return { isAuthenticated, correoUsuario, rol, login, logout };
};
