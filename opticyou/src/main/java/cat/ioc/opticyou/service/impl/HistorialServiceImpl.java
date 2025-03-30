package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.dto.ClientDTO;
import cat.ioc.opticyou.mapper.ClientMapper;
import cat.ioc.opticyou.model.Client;
import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.repositori.HistorialRepository;
import cat.ioc.opticyou.util.Rol;
import cat.ioc.opticyou.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistorialServiceImpl {//falta la interfaz implements
    @Autowired
    HistorialRepository historialRepository;

    public int createHistorial(Historial historial) {
            historialRepository.save(historial);
            return 1;
    }
    public Historial getHistorialById(Long id){
        return  historialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No hi ha cap historial amb id: " + id));
    }

    public boolean deleteHistorial(Long id){
        historialRepository.deleteById(id);
        Historial h =   historialRepository.findById(id)
                .orElse(null);
        if(h == null){
            return false;
        }
        return true;
    }
}
