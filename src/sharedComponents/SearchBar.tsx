
import { useState , useEffect , type KeyboardEvent } from "react";

interface Props {
    placeHolder:string;
    onQuery: (query:string) => void;
}


export const SearchBar = ({ placeHolder , onQuery }:Props) => {

  const [ query , setQuery ] = useState('');

  //Nuestro efecto es realizado apenas nuestro 
  //componente es montado
  useEffect(()=> {

    const timeOutId = setTimeout(()=> {
      onQuery(query);
    },700)

    return () => {
      clearTimeout(timeOutId);
    }
    
  },[query , onQuery])

  const handleSearch = () => {
    onQuery(query);
  }

  const handleKeyDown = (event:KeyboardEvent<HTMLInputElement>) => {
      if(event.key === 'Enter'){
          handleSearch();
      }
  }


  return (    
    <div className="search-container">        
        <input 
          type="text" 
          placeholder={ placeHolder } 
          value = { query }
          onChange={(event) => {
            setQuery(event.target.value)
          }} 
          onKeyDown={ handleKeyDown }
          />
        <button          
          onClick={ handleSearch }
        >
          Buscar
        </button>
    </div>
  )
}
