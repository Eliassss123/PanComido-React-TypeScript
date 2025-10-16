import { CustomHeader , SearchBar } from './sharedComponents'
import { PreviousSearches , ImagesList } from './imagesComponents'

import { robots , mockGifs } from './mock-data'

import './index.css'
import { useState } from 'react'


export const ImagesApp = () => {

  const [ previousSearches , setPreviosSearches ]  = useState(['gemini man']);


  //Comunicación entre componentes
  const handleTermClicked = ( term:string ) => {
    console.log({term});    
  }
  
  //Query a la consulta que la persona escriba
  const handleSearch = ( query:string ) => {
    console.log(query);
  }

  return (
    <>        
        <CustomHeader 
            title="Buscador de imagenes"
            text="Bienvenidos a nuestra aplicacion de búsqueda de imagenes"
            />

        <SearchBar 
            placeHolder="Ingrese nombre de la imagen" 
            onQuery = { handleSearch }
            />

        <PreviousSearches 
            searches={['megaman','brightman','sparkman','shadowman']}
            onLabelClicked = { handleTermClicked }
            />
        
        <ImagesList robots={robots} />
        
        
    </>
  )
}
