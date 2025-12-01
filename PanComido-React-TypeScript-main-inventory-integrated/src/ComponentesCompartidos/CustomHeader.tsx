
interface Props {
    title:string;
    text?:string;
}



// Componente de encabezado personalizado
export const CustomHeader = ({ title , text = 'Tienda de pan' }:Props) => {
  return (
    <div className="content-center">
        <h1>{ title }</h1>
        <p>{ text }</p>
    </div>
  )
}
