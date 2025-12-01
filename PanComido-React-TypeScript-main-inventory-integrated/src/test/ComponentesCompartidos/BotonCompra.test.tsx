import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { BotonCompra } from '../../ComponentesCompartidos/BotonComprarTodo';

describe('BotonCompra', () => {
  const carritoMock: any[] = [
    { id: 1, nombre: 'Marraqueta', precio: 500 },
    { id: 2, nombre: 'Hallulla', precio: 700 },
  ];

  const onVaciarMock = vi.fn();
  const originalFetch = globalThis.fetch;

  beforeEach(() => {
    // mock alert para que no bloquee el test
    vi.spyOn(window, 'alert').mockImplementation(() => {});
    onVaciarMock.mockClear();

    // mock fetch:
    // 1) crear pedido
    // 2) obtener boleta
    globalThis.fetch = vi
      .fn()
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({ id: 123 }),
      } as any)
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({
          pedidoId: 123,
          correoUsuario: 'cliente@test.cl',
          fecha: new Date().toISOString(),
          total: 1200,
          cantidadTotal: 2,
        }),
      } as any);
  });

  afterEach(() => {
    (window.alert as any).mockRestore?.();
    globalThis.fetch = originalFetch;
  });

  it('no muestra el botón si el carrito está vacío', () => {
    render(
      <BotonCompra
        carrito={[]}
        onVaciar={onVaciarMock}
        correoUsuario="cliente@test.cl"
      />
    );

    const boton = screen.queryByRole('button', { name: /realizar compra/i });
    expect(boton).toBeNull();
  });

  it('muestra alerta si no hay correoUsuario', () => {
    render(
      <BotonCompra
        carrito={carritoMock}
        onVaciar={onVaciarMock}
        correoUsuario={null}
      />
    );

    const boton = screen.getByRole('button', { name: /realizar compra/i });
    fireEvent.click(boton);

    expect(window.alert).toHaveBeenCalledWith(
      'Debes iniciar sesión antes de realizar la compra.'
    );
    expect(globalThis.fetch).not.toHaveBeenCalled();
    expect(onVaciarMock).not.toHaveBeenCalled();
  });

  it('flujo feliz: llama a los microservicios, muestra el modal y vacía el carrito', async () => {
    render(
      <BotonCompra
        carrito={carritoMock}
        onVaciar={onVaciarMock}
        correoUsuario="cliente@test.cl"
      />
    );

    const boton = screen.getByRole('button', { name: /realizar compra/i });
    expect(boton).toBeInTheDocument();

    fireEvent.click(boton);

    await waitFor(() => {
      // Se llamaron las 2 peticiones:
      expect(globalThis.fetch).toHaveBeenCalledTimes(2);
      // Se vació el carrito en el front:
      expect(onVaciarMock).toHaveBeenCalledTimes(1);
      // Se muestra el modal de boleta:
      expect(screen.getByText(/boleta de compra/i)).toBeInTheDocument();
      expect(screen.getByText(/total a pagar/i)).toBeInTheDocument();
    });
  });
});
