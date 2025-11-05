import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";

interface ModalLoginProps {
  onLogin: (username: string) => void;
}

export const ModalLogin: React.FC<ModalLoginProps> = ({ onLogin }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  // Lista de usuarios válidos
  const usuariosValidos = [
    { usuario: "admin", password: "1234" },
    { usuario: "elias", password: "abcd" },
    { usuario: "cliente", password: "0000" },
  ];

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const user = usuariosValidos.find(
      (u) => u.usuario === username && u.password === password
    );
    if (user) {
      onLogin(username);
      localStorage.setItem("auth", "true");
      setError("");
    } else {
      setError("Usuario o contraseña incorrectos.");
    }
  };

  return (
    <Modal show={true} backdrop="static" centered>
      <Modal.Header>
        <Modal.Title>Iniciar Sesión</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Usuario</Form.Label>
            <Form.Control
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Ingresa tu usuario"
              required
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Contraseña</Form.Label>
            <Form.Control
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Ingresa tu contraseña"
              required
            />
          </Form.Group>

          {error && <p style={{ color: "red" }}>{error}</p>}

          <div className="d-grid">
            <Button variant="primary" type="submit">
              Entrar
            </Button>
          </div>
        </Form>
      </Modal.Body>
    </Modal>
  );
};
