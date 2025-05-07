package cat.ioc.opticyou.mapper;

import cat.ioc.opticyou.dto.DiagnosticDTO;
import cat.ioc.opticyou.model.Diagnostic;
import cat.ioc.opticyou.model.Historial;

public class DiagnosticMapper {
    public static DiagnosticDTO toDto(Diagnostic diagnostic) {
        DiagnosticDTO dto = new DiagnosticDTO();
        dto.setIddiagnostic(diagnostic.getIddiagnostic());
        dto.setDescripcio(diagnostic.getDescripcio());
        dto.setDate(diagnostic.getDate());
        dto.setHistorialId(diagnostic.getHistorial().getIdhistorial());
        return dto;
    }

    public static Diagnostic toEntity(DiagnosticDTO dto, Historial historial) {
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setIddiagnostic(dto.getIddiagnostic());
        diagnostic.setDescripcio(dto.getDescripcio());
        diagnostic.setDate(dto.getDate());
        diagnostic.setHistorial(historial);
        return diagnostic;
    }
}
