package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.dto.ClinicaDTO;
import cat.ioc.opticyou.mapper.ClinicaMapper;
import cat.ioc.opticyou.model.Clinica;
import cat.ioc.opticyou.repositori.ClinicaRepository;
import cat.ioc.opticyou.service.ClinicaService;
import cat.ioc.opticyou.util.Rol;
import cat.ioc.opticyou.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servei per gestionar les operacions relacionades amb les clíniques.
 * Proporciona funcionalitat per crear, actualitzar, obtenir i eliminar clíniques.
 */
@Service
public class ClinicaServiceImpl implements ClinicaService {
    @Autowired
    private ClinicaRepository clinicaRepository;
    @Autowired
    private JwtServiceImpl jwtService;

    /**
     * Crea una nova clínica si el token és vàlid i l'usuari té el rol d'ADMIN.
     * Si la clínica ja existeix, retorna -1. Si el token és expirat o l'usuari no és ADMIN,
     * retorna 0. En cas contrari, crea la clínica i retorna 1.
     *
     * @param clinicaDTO El DTO de la clínica a crear.
     * @param token El token JWT per autenticar l'usuari.
     * @return 1 si la clínica s'ha creat correctament, -1 si la clínica ja existeix,
     *         0 si el token és invàlid o l'usuari no té el rol d'ADMIN.
     */
    @Override
    public int createClinica(ClinicaDTO clinicaDTO, String token) {
        if(!jwtService.isTokenExpired(Utils.extractBearerToken(token)) && Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))){
            if(!clinicaRepository.existsById(clinicaDTO.getIdClinica())){
                clinicaDTO.setIdClinica(null);
                clinicaRepository.save(ClinicaMapper.toEntity(clinicaDTO));
                return 1;
            }else{
                return -1;
            }
        }else
        return 0;
    }

    /**
     * Obté les dades d'una clínica pel seu ID si el token és vàlid i l'usuari té el rol d'ADMIN.
     * Si el token ha caducat o l'usuari no té el rol adequat, llença una excepció.
     * Si no es troba la clínica amb l'ID proporcionat, llença una excepció.
     *
     * @param id L'ID de la clínica que es vol obtenir.
     * @param token El token JWT per autenticar l'usuari.
     * @return El DTO de la clínica si el token és vàlid i l'usuari té el rol d'ADMIN.
     * @throws SecurityException Si el token ha caducat o l'usuari no és ADMIN.
     * @throws EntityNotFoundException Si no es troba cap clínica amb l'ID proporcionat.
     */
    @Override
    public ClinicaDTO getClinicaById(Long id, String token) {
        if(jwtService.isTokenExpired(Utils.extractBearerToken(token))) {
            throw new SecurityException("Token expirat");
        }
        if(jwtService.getRolFromToken(Utils.extractBearerToken(token)) != Rol.ADMIN) {
            throw new SecurityException("Accés denegat: cal ser ADMIN");
        }
        Clinica clinica = clinicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No hi ha cap clinica amb id: " + id));


        return ClinicaMapper.toDto(clinica);
    }

    /**
     * Obté les dades d'una clínica pel seu ID.
     * Si no es troba cap clínica amb l'ID proporcionat, llença una excepció.
     *
     * @param id L'ID de la clínica que es vol obtenir.
     * @return La clínica corresponent a l'ID proporcionat.
     * @throws EntityNotFoundException Si no es troba cap clínica amb l'ID proporcionat.
     */
    @Override
    public Clinica getClinicaById(Long id) {
        return  clinicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No hi ha cap clinica amb id: " + id));
    }


    /**
     * Obté la llista de totes les clíniques.
     * L'accés a aquesta informació està restringit als usuaris amb rol ADMIN.
     * Si el token està expirat o invàlid, o si l'usuari no té el rol ADMIN, es llençarà una excepció.
     *
     * @param token El token JWT de l'usuari per autenticar la petició.
     * @return Una llista de DTOs amb les dades de totes les clíniques.
     * @throws SecurityException Si el token està expirat o invàlid, o si l'usuari no té rol ADMIN.
     */
    @Override
    public List<ClinicaDTO> getAllClinicas(String token) {
        if (jwtService.isTokenExpired(Utils.extractBearerToken(token))) {
            throw new SecurityException("Token expirat o invàlid");
        }
        if (jwtService.getRolFromToken(Utils.extractBearerToken(token)) != Rol.ADMIN) {
            throw new SecurityException("Accés denegat: Es requereix rol ADMIN");
        }

        List<Clinica> cliniques = clinicaRepository.findAll();

        return cliniques.stream()
                .map(ClinicaMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualitza la informació d'una clínica existent.
     * L'accés a aquesta operació està restringit als usuaris amb rol ADMIN.
     * Si el token està expirat o invàlid, o si l'usuari no té el rol ADMIN, es llençarà una excepció.
     * Si no es troba la clínica amb el ID especificat, es retornarà false.
     *
     * @param clinicaDTO El DTO que conté les noves dades de la clínica a actualitzar.
     * @param token El token JWT de l'usuari per autenticar la petició.
     * @return true si la clínica es va actualitzar correctament, false si no es va trobar la clínica amb l'ID especificat.
     * @throws SecurityException Si el token està expirat o invàlid, o si l'usuari no té rol ADMIN.
     */
    @Override
    public boolean updateClinica(ClinicaDTO clinicaDTO, String token) {

        if (jwtService.isTokenExpired(Utils.extractBearerToken(token))) {
            throw new SecurityException("Token expirat o invàlid");
        }
        if (jwtService.getRolFromToken(Utils.extractBearerToken(token)) != Rol.ADMIN) {
            throw new SecurityException("Accés denegat: Es requereix rol ADMIN");
        }
        Optional<Clinica> clinicaOptional = clinicaRepository.findById(clinicaDTO.getIdClinica());
        if (!clinicaOptional.isPresent()) {
            return false;
        }
        clinicaRepository.save(ClinicaMapper.toEntity(clinicaDTO));
        return true;
    }

    /**
     * Elimina una clínica per ID.
     * L'accés a aquesta operació està restringit als usuaris amb rol ADMIN.
     * Si el token està expirat o invàlid, o si l'usuari no té el rol ADMIN, es llençarà una excepció.
     * Si no es troba la clínica amb l'ID especificat, es retornarà false.
     *
     * @param id L'ID de la clínica a eliminar.
     * @param token El token JWT de l'usuari per autenticar la petició.
     * @return true si la clínica es va eliminar correctament, false si no es va trobar la clínica amb l'ID especificat.
     * @throws SecurityException Si el token està expirat o invàlid, o si l'usuari no té rol ADMIN.
     */
    @Override
    public boolean deleteClinica(Long id, String token) {
        if (jwtService.isTokenExpired(Utils.extractBearerToken(token))) {
            throw new SecurityException("Token expirat o invàlid");
        }
        if (jwtService.getRolFromToken(Utils.extractBearerToken(token)) != Rol.ADMIN) {
            throw new SecurityException("Accés denegat: Es requereix rol ADMIN");
        }

        if (!clinicaRepository.existsById(id)) {
            return false;
        }

        clinicaRepository.deleteById(id);
        return true;
    }

}
