package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.dto.HistorialDTO;
import cat.ioc.opticyou.mapper.HistorialMapper;
import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.repositori.HistorialRepository;
import cat.ioc.opticyou.service.ClientService;
import cat.ioc.opticyou.service.HistorialService;
import cat.ioc.opticyou.util.Rol;
import cat.ioc.opticyou.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistorialServiceImpl implements HistorialService {//falta la interfaz implements
    @Autowired
    private HistorialRepository historialRepository;
    @Autowired
    private ClientServiceImpl clientServiceImpl;
    @Autowired
    private JwtServiceImpl jwtService;

    @Override
    public int createHistorial(HistorialDTO historialDTO, String token) {
        if (!jwtService.isTokenExpired(Utils.extractBearerToken(token)) && Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))){
            historialRepository.save(HistorialMapper.toEntity(historialDTO, clientServiceImpl));
            return 1;
        }
        return 0;
    }
    @Override
    public HistorialDTO getHistorialById(Long id, String token){
        if (!jwtService.isTokenExpired(Utils.extractBearerToken(token)) && Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))){
            Historial historial =   historialRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("No hi ha cap historial amb id: " + id));
            return HistorialMapper.toDto(historial);
        }
        throw new SecurityException("Token expirat o no ADMIN");
    }
    @Override
    public boolean deleteHistorial(Long id, String token){
        if (!jwtService.isTokenExpired(Utils.extractBearerToken(token)) && Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))){
            historialRepository.deleteById(id);
            Historial h =   historialRepository.findById(id)
                    .orElse(null);
            if(h == null){
                return false;
            }
            return true;
        }
        throw new SecurityException("Token expirat o no ADMIN");
    }
    @Override
    public boolean updateHistorial(HistorialDTO historialDTO, String token){
        if (!jwtService.isTokenExpired(Utils.extractBearerToken(token)) && Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))){
            Optional<Historial> historial = historialRepository.findById(historialDTO.getIdhistorial());
            if(!historial.isPresent()){
                return false;
            }
            historialRepository.save(HistorialMapper.toEntity(historialDTO, clientServiceImpl));
            return true;
        }
        throw new SecurityException("Token expirat o no ADMIN");
     }

     @Override
     public List<HistorialDTO> getAllHistorialByClinica(Long clinicaId, String token){
//  TODO: implementar
     }
    @Override
    public int createHistorial(Historial historial) {
            historialRepository.save(historial);
            return 1;
    }
    @Override
    public Historial getHistorialById(Long id){
        return  historialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No hi ha cap historial amb id: " + id));
    }
    @Override
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
