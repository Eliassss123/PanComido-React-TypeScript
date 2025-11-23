package com.pancomido.envio.serviceEnvio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.pancomido.pancomido.envio.modelEnvio.Envio;
import com.pancomido.pancomido.envio.repositoryEnvio.EnvioRepository;

import java.util.List;

@Service
public class EnvioServiceImpl implements EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Envio crearEnvio(Envio envio) {
        String runCliente = envio.getCliente().getRun();
                Cliente cliente = clienteRepository.findByRun(runCliente)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con run: " + runCliente));
    
        envio.setCliente(cliente);
        
        return envioRepository.save(envio);
    }



    @Override
    public void eliminarEnvio(Integer id) {
        Envio envio = envioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Envío no encontrado con id: " + id));
        envioRepository.delete(envio);
    }
    

    @Override
    public Envio actualizarEstado(Integer id, String nuevoEstado) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado con id: " + id));
        envio.setEstado(nuevoEstado);
        return envioRepository.save(envio);
    }

    @Override
    public List<Envio> obtenerTodos() {
        return envioRepository.findAll();
    }
}