import { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";

interface ModalLoginProps {
  onLogin: (correo: string) => void;
}

export const ModalLogin: React.FC<ModalLoginProps> = ({ onLogin }) => {
  const [usuario, setUsuario] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLimpiar = () => {
    setUsuario("");
    setPassword("");
  };


  const API_BASE_URL = "http://10.0.2.15:8085/api/auth";
  const handleLogin = async () => {
    if (!usuario || !password) {
      alert("Debes ingresar usuario y contraseña");
      return;
    }

    try {
      setLoading(true);

      // Ajusta los nombres de parámetros según tu microservicio de auth
      const params = new URLSearchParams({
        correo: usuario,
        contrasena: password,
      });

      const resp = await fetch(
        `http://localgost/8085/api/auth/login?${params.toString()}`,
        { method: "POST" }
      );

      if (resp.ok) {
        console.log("Login OK");
        onLogin(usuario);        // <- acá avisamos al hook con el correo
        handleLimpiar();
        return;
      }

      if (resp.status === 401) {
        alert("Usuario o contraseña incorrectos");
        return;
      }

      const msg = await resp.text();
      alert(`Error en login (HTTP ${resp.status}): ${msg}`);
    } catch (err) {
      console.error("Error de red en login:", err);
      alert("No se pudo conectar con el servicio de autenticación.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal show centered>
      <Modal.Header>
        <Modal.Title>Iniciar Sesión</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Form.Group className="mb-3" controlId="loginUsuario">
            <Form.Label>Usuario (correo)</Form.Label>
            <Form.Control
              type="email"
              placeholder="admin@pancomido.cl"
              value={usuario}
              onChange={(e) => setUsuario(e.target.value)}
            />
          </Form.Group>

          <Form.Group className="mb-3" controlId="loginPassword">
            <Form.Label>Contraseña</Form.Label>
            <Form.Control
              type="password"
              placeholder="Ingresa tu contraseña"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button
          variant="secondary"
          onClick={handleLimpiar}
          disabled={loading}
        >
          Limpiar
        </Button>
        <Button
          variant="primary"
          onClick={handleLogin}
          disabled={loading}
        >
          {loading ? "Ingresando..." : "Ingresar"}
        </Button>
      </Modal.Footer>
    </Modal>
  );
};
