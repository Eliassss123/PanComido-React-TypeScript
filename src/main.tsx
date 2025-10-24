import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { ImagesApp } from './RaizApp'


createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <ImagesApp />
  </StrictMode>,
)
