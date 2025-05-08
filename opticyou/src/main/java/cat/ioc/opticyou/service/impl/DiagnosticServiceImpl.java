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

    /**
     * Crea un diagnòstic i l'associa a un historial mèdic.
     * Aquesta operació només es pot realitzar per un usuari amb rol d'admin.
     * Si el token JWT és vàlid, l'usuari és administrador i el diagnòstic no existeix prèviament, es desa un nou diagnòstic.
     *
     * @param diagnosticDTO Les dades del diagnòstic a crear.
     * @param token         El token JWT d'autenticació.
     * @return              1 si el diagnòstic es crea correctament, -1 si ja existeix, 0 en cas contrari.
     */
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

    /**
     * Obté una llista de diagnòstics associats a un historial mèdic concret.
     * Aquesta operació només es pot realitzar per un usuari amb rol d'admin.
     * Si el token JWT és vàlid i l'usuari és administrador, es retorna la llista de diagnòstics d'aquell historial.
     * En cas contrari, es llença una excepció de seguretat.
     *
     * @param historialId L'identificador de l'historial mèdic.
     * @param token       El token JWT d'autenticació.
     * @return            Una llista de diagnòstics convertits a DiagnosticDTO.
     * @throws SecurityException Si el token és expirat o l'usuari no té rol d'admin.
     */

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

    /**
     * Actualitza la descripció d'un diagnòstic existent.
     * Aquesta operació només es pot realitzar per un usuari amb rol d'admin.
     * Si el token JWT és vàlid i l'usuari és administrador, s'actualitza la descripció del diagnòstic especificat.
     * No es poden modificar ni la data, ni l'ID de l'historial, ni l'ID del diagnòstic.
     * En cas contrari, es llença una excepció de seguretat.
     *
     * @param diagnosticDTO Les dades del diagnòstic amb la nova descripció.
     * @param token         El token JWT d'autenticació.
     * @return              Retorna un valor boolean que indica si l'actualització s'ha realitzat amb èxit.
     * @throws SecurityException Si el token és expirat o l'usuari no té rol d'admin.
     */
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

    /**
     * Elimina un diagnòstic de la base de dades. Aquesta operació només es pot realitzar per un usuari amb rol d'admin.
     * Si el token JWT és vàlid i l'usuari és administrador, es procedeix a eliminar el diagnòstic.
     * En cas contrari, es llença una excepció de seguretat.
     *
     * @param id     L'identificador del diagnòstic a eliminar.
     * @param token  El token JWT d'autenticació.
     * @return       Retorna true si l'eliminació és exitosa, false si el diagnòstic no existeix.
     * @throws SecurityException Si el token és expirat o l'usuari no té rol d'admin.
     */
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
