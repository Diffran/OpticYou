package cat.ioc.opticyou.service;

import cat.ioc.opticyou.model.Historial;

import java.util.List;

public interface HistorialService {
    boolean updateHistorial(Historial historial, String token);
    Historial getHistorialById(Long id, String token);
    List<Historial> getAllHistorial(String token);
    boolean deleteHistorial(Long id);
//    boolean deleteHistorial(Long id, String token);
    Historial getHistorialById(Long id);
    int createHistorial(Historial historial);

}
