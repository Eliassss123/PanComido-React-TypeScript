package com.pancomido.envio.serviceEnvio;
import java.util.List;


import com.pancomido.pancomido.envio.modelEnvio.Envio;


public interface EnvioService {

    Envio crearEnvio(Envio envio);
    void eliminarEnvio(Integer id);
    Envio actualizarEstado(Integer id, String nuevoEstado);
    List<Envio> obtenerTodos();
}
