package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.dto.DiagnosticDTO;
import cat.ioc.opticyou.mapper.DiagnosticMapper;
import cat.ioc.opticyou.model.Diagnostic;
import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.repositori.DiagnosticRepositori;
import cat.ioc.opticyou.repositori.HistorialRepository;
import cat.ioc.opticyou.service.DiagnosticService;
import cat.ioc.opticyou.util.Rol;
import cat.ioc.opticyou.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiagnosticServiceImpl implements DiagnosticService {
    @Autowired
    private DiagnosticRepositori diagnosticRepositori;
    @Autowired
    private HistorialRepository historialRepository;
    @Autowired
    private JwtServiceImpl jwtService;

    @Override
    public int createDiagnostic(DiagnosticDTO diagnosticDTO, String token) {
        if(!jwtService.isTokenExpired(Utils.extractBearerToken(token)) && Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))){
            if(!diagnosticRepositori.existsById(diagnosticDTO.getIddiagnostic())){
                diagnosticDTO.setIddiagnostic(null);
                Historial historial = historialRepository.findById(diagnosticDTO.getHistorialId())
                                .orElseThrow(() -> new EntityNotFoundException("No s'ha trobat l'historial"));
                diagnosticRepositori.save(DiagnosticMapper.toEntity(diagnosticDTO,historial));
                return 1;
            }else{
                return -1;
            }
        }else
            return 0;
    }

    @Override
    public List<DiagnosticDTO> getAllDiagnosticsByHistorial(Long historialId, String token) {
        if (jwtService.isTokenExpired(Utils.extractBearerToken(token)) && jwtService.getRolFromToken(Utils.extractBearerToken(token)) != Rol.ADMIN) {
            throw new SecurityException("Token expirat o accés denegat: cal rol ADMIN");
        }
        List<Diagnostic> diagnostics = diagnosticRepositori.findByHistorialIdhistorial(historialId);
        return  diagnostics.stream()
                .map(DiagnosticMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateDiagnostic(DiagnosticDTO diagnosticDTO, String token) {
        if (jwtService.isTokenExpired(Utils.extractBearerToken(token)) && jwtService.getRolFromToken(Utils.extractBearerToken(token)) != Rol.ADMIN) {
            throw new SecurityException("Token expirat o accés denegat: cal rol ADMIN");
        }
        Diagnostic diagnostic = diagnosticRepositori.findById(diagnosticDTO.getIddiagnostic())
                .orElseThrow(() -> new EntityNotFoundException("No s'ha trobat cap diagnostic amb aquest id"));
        if (diagnostic == null) {
            return false;
        }
        diagnostic.setDescripcio(diagnosticDTO.getDescripcio());
        diagnosticRepositori.save(diagnostic);
        return true;
    }

    @Override
    public boolean deleteDiagnostic(Long id, String token) {
        if (jwtService.isTokenExpired(Utils.extractBearerToken(token)) && jwtService.getRolFromToken(Utils.extractBearerToken(token)) != Rol.ADMIN) {
            throw new SecurityException("Token expirat o accés denegat: cal rol ADMIN");
        }
        if(!diagnosticRepositori.existsById(id)){
            return false;
        }

        diagnosticRepositori.deleteById(id);
        return true;
    }
}
