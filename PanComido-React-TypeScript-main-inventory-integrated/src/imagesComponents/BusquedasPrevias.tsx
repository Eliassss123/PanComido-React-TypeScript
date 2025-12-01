import type { FC } from "react";

interface Propiedades {
  busquedas: string[];
  onLabelClicked?: (term: string) => void;
}


// Componente para mostrar búsquedas previas como etiquetas clicables
export const BusquedasPrevias: FC<Propiedades> = ({ busquedas, onLabelClicked }) => {
  return (
    <div className="mb-3">
      <h5> </h5>
      {/* Lista de etiquetas de búsquedas previas */}
      <ul className="list-inline">

        {/* Mapea cada término de búsqueda a un botón */}
        {busquedas.map((termino) => (
          <li key={termino} className="list-inline-item">
            <button
              type="button"
              className="btn btn-sm btn-outline-primary"
              onClick={() => onLabelClicked && onLabelClicked(termino)}
            >
              {termino}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};
