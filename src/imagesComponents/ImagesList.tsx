import type { robotsProps } from '../mock-data/robots.mocks'

interface Props {
    robots:robotsProps[]
}


export const ImagesList = ({ robots }:Props) => {
  return (
    <div className="gifs-container">
        {
            robots.map( ( robot )=> {
                return (
                    <div key={ robot.id } className="gif-card">
                        <img src={robot.avatar} alt={ robot.name } />
                        <h3>{ robot.name }</h3>
                    </div>
                )
            } )
        }
    </div>
  )
}
