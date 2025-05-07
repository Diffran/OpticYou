package cat.ioc.opticyou.service;
import cat.ioc.opticyou.dto.DiagnosticDTO;

import java.util.List;

public interface DiagnosticService {
    int createDiagnostic(DiagnosticDTO diagnosticDTO, String token);
    List<DiagnosticDTO> getAllDiagnosticsByHistorial(Long historialId, String token);
    boolean updateDiagnostic(DiagnosticDTO diagnosticDTO, String token);
    boolean deleteDiagnostic(Long id, String token);
}
