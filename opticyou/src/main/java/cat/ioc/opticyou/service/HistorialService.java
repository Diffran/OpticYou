package cat.ioc.opticyou.service;

import cat.ioc.opticyou.model.Historial;

public interface HistorialService {
    boolean deleteHistorial(Long id);
    boolean deleteHistorial(Long id, String token);
    Historial getHistorialById(Long id);
    Historial getHistorialById(Long id, String token);
    int createHistorial(Historial historial);
    int createHistorial(Historial historial, String token);
}
