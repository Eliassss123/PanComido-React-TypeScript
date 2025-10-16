import type { FC } from "react"


interface Props{
    searches: string[],
    onLabelClicked?: (term:string) => void;
}

export const PreviousSearches:FC<Props> = ({searches , onLabelClicked}) => {
  return (
    <div className="previous-searches">
        <h2>Búsquedas previas</h2>
        <ul className="previous-searches-list">
            {
                searches.map((termino)=> (                    
                    <li 
                        key={termino}                        
                        onClick={ ()=> {
                            onLabelClicked && onLabelClicked(termino)
                        } }
                        >{termino}</li>
                ))
            }
        </ul>
    </div>
  )
}
