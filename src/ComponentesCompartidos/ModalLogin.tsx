// ComponentesCompartidos/ModalLogin.tsx
import { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";

interface ModalLoginProps {
  onLogin: () => void; // función para activar isAuthenticated
}

export const ModalLogin: React.FC<ModalLoginProps> = ({ onLogin }) => {
  const [usuario, setUsuario] = useState("");
  const [password, setPassword] = useState("");

  // Lista de usuarios permitidos
  const usuariosPermitidos = [
    { usuario: "admin", password: "1234" },
    { usuario: "elias", password: "1234" },
    { usuario: "benjamin", password: "1234" },
  ];

  // Maneja el intento de login
  const handleLogin = () => {
    const valido = usuariosPermitidos.some(
      (u) => u.usuario === usuario && u.password === password
    );

    // Si las credenciales son válidas, llama a onLogin
    if (valido) {
      onLogin(); // activa isAuthenticated en la app
    } else {
      alert("Usuario o contraseña incorrectos");
    }
  };

  // Limpia los campos de usuario y contraseña
  const handleLimpiar = () => {
    setUsuario("");
    setPassword("");
  };


  // Renderiza el modal de login
  return (
    <Modal show centered>
      <Modal.Header>
        <Modal.Title>Iniciar Sesión</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Form.Group className="mb-3" controlId="inputUsuario">
            <Form.Label>Usuario</Form.Label>
            <Form.Control
              type="text"
              placeholder="Ingresa tu usuario"
              value={usuario}
              onChange={(e) => setUsuario(e.target.value)}
              required
            />
          </Form.Group>

{/*  Campo de contraseña  */}
          <Form.Group className="mb-3" controlId="inputPassword">
            <Form.Label>Contraseña</Form.Label>
            <Form.Control
              type="password"
              placeholder="Ingresa tu contraseña"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" type="button" onClick={handleLimpiar}>
          Limpiar
        </Button>
        <Button variant="primary" type="button" onClick={handleLogin}>
          Ingresar
        </Button>
      </Modal.Footer>
    </Modal>
  );
};
