
interface Props {
    title:string;
    text?:string;
}


export const CustomHeader = ({ title , text = 'Buscador de imagenes' }:Props) => {
  return (
    <div className="content-center">
        <h1>{ title }</h1>
        <p>{ text }</p>
    </div>
  )
}
