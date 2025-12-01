// ComponentesCompartidos/CategoriasDropdown.tsx
import { Dropdown, DropdownButton } from "react-bootstrap";



// Props para el componente de dropdown de categorías
interface Props {
  categorias: string[];
  categoriaSeleccionada: string | null;
  onSelect: (categoria: string | null) => void;
}

// Componente de dropdown para seleccionar categorias
export const CategoriasDropdown: React.FC<Props> = ({ categorias, categoriaSeleccionada, onSelect }) => (
  <DropdownButton
    id="dropdown-categorias"
    title={categoriaSeleccionada || "Categorías"}
    variant="secondary"
  >
    {/* Mapea las categorías a elementos del dropdown */}
    {categorias.map(cat => (
      <Dropdown.Item key={cat} onClick={() => onSelect(cat)}>
        {cat}
      </Dropdown.Item>
    ))}
    <Dropdown.Item onClick={() => onSelect(null)}>Todas</Dropdown.Item>
  </DropdownButton>
);
