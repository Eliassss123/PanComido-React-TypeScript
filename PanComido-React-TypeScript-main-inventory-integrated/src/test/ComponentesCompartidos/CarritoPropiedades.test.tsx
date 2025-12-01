import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { Carro } from '../../ComponentesCompartidos/CarritoPropiedades';

describe('Carro (CarritoPropiedades)', () => {
  it('muestra el mensaje de carrito vacío cuando no hay productos', () => {
    render(
      <Carro
        carrito={[]}
        onRemove={() => {}}
      />
    );

    // Texto que sale en tu componente cuando está vacío
    const mensaje = screen.getByText(/tu carrito está vacío/i);
    expect(mensaje).toBeInTheDocument();

    // Y la imagen con alt="Carrito vacío"
    const imagen = screen.getByAltText(/carrito vacío/i);
    expect(imagen).toBeInTheDocument();
  });

  it('renderiza los productos del carrito', () => {
    const carrito = [
      {
        id: 1,
        titulo: 'Marraqueta',
        precio: 500,
        url: 'https://ejemplo.com/marraqueta.jpg',
      } as any,
      {
        id: 2,
        titulo: 'Hallulla',
        precio: 700,
        url: 'https://ejemplo.com/hallulla.jpg',
      } as any,
    ];

    render(
      <Carro
        carrito={carrito}
        onRemove={() => {}}
      />
    );

    expect(screen.getByText(/marraqueta/i)).toBeInTheDocument();
    expect(screen.getByText(/hallulla/i)).toBeInTheDocument();
    expect(screen.getByText(/precio: \$500/i)).toBeInTheDocument();
  });

  it('llama a onRemove con el id correcto al hacer clic en "Eliminar"', () => {
    const carrito = [
      {
        id: 123,
        titulo: 'Marraqueta',
        precio: 500,
        url: 'https://ejemplo.com/marraqueta.jpg',
      } as any,
    ];

    const onRemoveMock = vi.fn();

    render(
      <Carro
        carrito={carrito}
        onRemove={onRemoveMock}
      />
    );

    const botonEliminar = screen.getByRole('button', { name: /eliminar/i });
    fireEvent.click(botonEliminar);

    expect(onRemoveMock).toHaveBeenCalledTimes(1);
    expect(onRemoveMock).toHaveBeenCalledWith(123);
  });
});
