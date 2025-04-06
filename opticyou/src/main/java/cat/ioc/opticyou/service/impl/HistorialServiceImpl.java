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

/**
 * Gestiona els processos relacionats amb els historial dels clients, com la creació, actualització
 * i eliminació dels historials. Utilitza els repositoris corresponents per interactuar amb la base de dades.
 */
@Service
public class HistorialServiceImpl implements HistorialService {
    @Autowired
    private HistorialRepository historialRepository;
    @Autowired
    private JwtServiceImpl jwtService;

    /**
     * Actualitza un historial existent a la base de dades.
     *
     * Comprova que el token JWT sigui vàlid i que l'usuari sigui ADMIN abans de realitzar l'operació.
     * Si el historial no es troba, llança una excepció.
     *
     * @param historial El historial a actualitzar.
     * @param token El token JWT de l'usuari que realitza la petició.
     * @return true si l'actualització és exitosa, false si el historial no existeix.
     * @throws SecurityException Si el token és invàlid o l'usuari no té permisos d'ADMIN.
     */
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

    /**
     * Obté un historial per la seva ID.
     *
     * Comprova que el token JWT sigui vàlid i que l'usuari sigui ADMIN abans de realitzar l'operació.
     * Si el historial no es troba, llança una excepció.
     *
     * @param id L'ID del historial a obtenir.
     * @param token El token JWT de l'usuari que realitza la petició.
     * @return El historial associat a l'ID proporcionat.
     * @throws SecurityException Si el token és invàlid o l'usuari no té permisos d'ADMIN.
     */
    @Override
    public Historial getHistorialById(Long id, String token){
        if(!jwtService.isTokenExpired(Utils.extractBearerToken(token)) && Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))){
            Optional<Historial> historial = historialRepository.findById(id);
            return historial.get();
        }
        throw new SecurityException("Token expirat o no ADMIN");
    }

    /**
     * Obté tots els històrics.
     *
     * Comprova que el token JWT sigui vàlid i que l'usuari sigui ADMIN abans de realitzar l'operació.
     *
     * @param token El token JWT de l'usuari que realitza la petició.
     * @return Una llista de tots els històrics disponibles.
     * @throws SecurityException Si el token és invàlid o l'usuari no té permisos d'ADMIN.
     */
    @Override
    public List<Historial> getAllHistorial(String token) {
        if(!jwtService.isTokenExpired(Utils.extractBearerToken(token)) && Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))){
            return historialRepository.findAll();
        }
        throw new SecurityException("Token expirat o no ADMIN");
    }

    /**
     * Crea un nou historial i el desa a la base de dades.
     *
     * @param historial L'objecte Historial a crear.
     * @return Retorna 1 si l'historial es crea correctament.
     */
    @Override
    public int createHistorial(Historial historial) {
            historialRepository.save(historial);
            return 1;
    }

    /**
     * Obté un historial per ID.
     * Si no es troba, llança una excepció EntityNotFoundException.
     *
     * @param id L'ID de l'historial a cercar.
     * @return L'objecte Historial trobat.
     * @throws EntityNotFoundException Si no es troba cap historial amb l'ID proporcionat.
     */
    @Override
    public Historial getHistorialById(Long id){
        return  historialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No hi ha cap historial amb id: " + id));
    }

    /**
     * Elimina un historial per ID.
     * Si l'historial no es troba després d'eliminar-lo, retorna false.
     *
     * @param id L'ID de l'historial a eliminar.
     * @return true si l'historial s'ha eliminat correctament, false si no s'ha trobat.
     */
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
