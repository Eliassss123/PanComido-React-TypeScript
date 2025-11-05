import type { FC } from "react";

interface Propiedades {
  busquedas: string[];
  onLabelClicked?: (term: string) => void;
}

export const BusquedasPrevias: FC<Propiedades> = ({ busquedas, onLabelClicked }) => {
  return (
    <div className="mb-3">
      <h5> </h5>
      <ul className="list-inline">
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
