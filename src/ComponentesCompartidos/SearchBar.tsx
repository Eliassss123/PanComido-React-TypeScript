import { useState, type KeyboardEvent } from "react";


// Props para la barra de búsqueda
interface Props {
  textoDifuminado: string;
  onQuery: (query: string) => void;
}


// Componente de barra de búsqueda
export const SearchBar = ({ textoDifuminado, onQuery }: Props) => {
  const [query, setQuery] = useState('');


  {/* Función para manejar la búsqueda */}
  const handleSearch = () => {
    if (!query.trim()) return; // evita búsquedas vacías
    onQuery(query);
  }


  {/* Maneja la tecla Enter para iniciar la búsqueda */}
  const handleKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
    if(event.key === 'Enter'){
      handleSearch();
    }
  }


  // Renderiza la barra de búsqueda
  return (
    <div className="search-container">
      <label htmlFor="searchInput" className="visually-hidden">
        Buscar producto
      </label>
      <input
        id="searchInput"
        name="search"
        type="text"
        placeholder={textoDifuminado}
        value={query}
        onChange={(event) => setQuery(event.target.value)}
        onKeyDown={handleKeyDown}
      />
      <button onClick={handleSearch}>Buscar</button>
    </div>
  );
}
