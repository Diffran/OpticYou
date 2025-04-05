package cat.ioc.opticyou.service;

import cat.ioc.opticyou.dto.HistorialDTO;
import cat.ioc.opticyou.model.Historial;

import java.util.List;

public interface HistorialService {
    boolean deleteHistorial(Long id);
//    boolean deleteHistorial(Long id, String token);
    Historial getHistorialById(Long id);
    HistorialDTO getHistorialById(Long id, String token);
    int createHistorial(Historial historial);
//    int createHistorial(HistorialDTO historialDTO, String token);
//    boolean updateHistorial(HistorialDTO historialDTO, String token);
//    List<HistorialDTO> getAllHistorial(String token);
}
