import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { PanaderiaApp } from './RaizApp'


createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <PanaderiaApp />
  </StrictMode>,
)
