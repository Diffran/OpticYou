package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.repositori.HistorialRepository;
import cat.ioc.opticyou.service.HistorialService;
import cat.ioc.opticyou.util.Rol;
import cat.ioc.opticyou.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistorialServiceImpl implements HistorialService {
    @Autowired
    private HistorialRepository historialRepository;
    @Autowired
    private JwtServiceImpl jwtService;

    @Override
    public boolean updateHistorial(Historial historial, String token){
        if(!jwtService.isTokenExpired(Utils.extractBearerToken(token)) && Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))){
            try{
                getHistorialById(historial.getIdhistorial());//si no hi ha llença una exception
                historialRepository.save(historial);
                return true;
            }catch (EntityNotFoundException e){
                return false;
            }
        }
        throw new SecurityException("Token expirat o no ADMIN");
    }
    @Override
    public Historial getHistorialById(Long id, String token){
        if(!jwtService.isTokenExpired(Utils.extractBearerToken(token)) && Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))){
            Optional<Historial> historial = historialRepository.findById(id);
            return historial.get();
        }
        throw new SecurityException("Token expirat o no ADMIN");
    }

    @Override
    public List<Historial> getAllHistorial(String token) {
        if(!jwtService.isTokenExpired(Utils.extractBearerToken(token)) && Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))){
            return historialRepository.findAll();
        }
        throw new SecurityException("Token expirat o no ADMIN");
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
