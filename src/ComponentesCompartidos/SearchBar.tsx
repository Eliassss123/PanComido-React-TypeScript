import { useState, type KeyboardEvent } from "react";

interface Props {
  textoDifuminado: string;
  onQuery: (query: string) => void;
}

export const SearchBar = ({ textoDifuminado, onQuery }: Props) => {
  const [query, setQuery] = useState('');

  const handleSearch = () => {
    if (!query.trim()) return; // evita búsquedas vacías
    onQuery(query);
  }

  const handleKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
    if(event.key === 'Enter'){
      handleSearch();
    }
  }

  return (
    <div className="search-container">
      <input
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
