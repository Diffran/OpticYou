package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.dto.ClientDTO;
import cat.ioc.opticyou.dto.TreballadorDTO;
import cat.ioc.opticyou.mapper.ClientMapper;
import cat.ioc.opticyou.mapper.TreballadorMapper;
import cat.ioc.opticyou.model.Client;
import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.model.Treballador;
import cat.ioc.opticyou.repositori.ClientRepository;
import cat.ioc.opticyou.repositori.ClinicaRepository;
import cat.ioc.opticyou.repositori.TreballadorRepository;
import cat.ioc.opticyou.repositori.UsuariRepositori;
import cat.ioc.opticyou.service.ClinicaService;
import cat.ioc.opticyou.service.TreballadorService;
import cat.ioc.opticyou.util.Rol;
import cat.ioc.opticyou.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TreballadorServiceImpl implements TreballadorService {
    @Autowired
    UsuariRepositori usuariRepositori;
    @Autowired
    TreballadorRepository treballadorRepository;
    @Autowired
    private JwtServiceImpl jwtService;
    @Autowired
    private ClinicaService clinicaService;
    @Autowired
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Crea un treballador i l'associa a una clínica.
     * Aquesta operació només es pot realitzar per un usuari amb rol d'admin.
     * Si el token JWT és vàlid i l'usuari és administrador, es crea un client amb un historial bàsic.
     *
     * @param treballadorDTO Les dades del treballador a crear.
     * @param token     El token JWT d'autenticació.
     * @return          1 si el treballadr es crea correctament, 0 en cas contrari.
     */
    @Override
    public int createTreballador(TreballadorDTO treballadorDTO, String token) {
        if (!jwtService.isTokenExpired(Utils.extractBearerToken(token)) && Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token))) {
            Treballador treballador = TreballadorMapper.toEntity(treballadorDTO,
                    clinicaService.getClinicaById(treballadorDTO.getClinicaId()));
            //treure qualsevol id perque ho faci automaticament spring in codificar el password
            treballador.setIdUsuari(null);
            treballador.setContrasenya(passwordEncoder.encode(treballador.getPassword()));

            treballadorRepository.save(treballador);
            return 1;
        }
        return 0;
    }

    /**
     * Obté un treballador per ID. Aquesta operació només es pot realitzar per un usuari amb rol d'admin.
     * Si el token JWT és vàlid i l'usuari és administrador, es retorna el client amb l'ID indicat.
     * Si el client no es troba, es llença una excepció.
     *
     * Si és treballador només retornarà el treballador amb el id del token, ignorarà el param id
     *
     * @param id    L'ID del treballador a obtenir.
     * @param token El token JWT d'autenticació.
     * @return      El treballador convertit a un TreballadorDTO.
     * @throws SecurityException Si el token és expirat o l'usuari no té rol d'admin.
     * @throws EntityNotFoundException Si no es troba el treballador amb l'ID especificat.
     */
    @Override
    public TreballadorDTO getTreballadorById(Long id, String token) {
        String tokenNoBearer = Utils.extractBearerToken(token);
        Rol rol = jwtService.getRolFromToken(tokenNoBearer);

        if (!jwtService.isTokenExpired(tokenNoBearer)){
            if (Rol.ADMIN == rol){
                Optional<Treballador> treballador = treballadorRepository.findById(id);
                if (treballador.isPresent()) {
                    return TreballadorMapper.toDto(treballador.get());
                }

            }
            if (Rol.TREBALLADOR == rol){//nomes retornarà el treballador si mateixa clinica
                Optional<Treballador> treballador = treballadorRepository.findById(jwtService.getIdFromToken(tokenNoBearer));
                Optional<Treballador> treballadorBuscat = treballadorRepository.findById(id);
                if (treballador.get().getClinica() == treballadorBuscat.get().getClinica()) {
                    return TreballadorMapper.toDto(treballador.get());
                }
            }

            throw new EntityNotFoundException("No hi ha cap treballador amb el id entrat");
        }

        throw new SecurityException("Token expirat o no autoritzat");
    }

    /**
     * Obté una llista de tots els treballadors. Aquesta operació només es pot realitzar per un usuari amb rol d'admin.
     * Si el token JWT és vàlid i l'usuari és administrador, es retorna la llista de treballadors.
     * Obté una llista dels treballadors que pertanyin a la mateixa clínica del token si té el rol TREBALLADOR
     * En cas contrari, es llença una excepció de seguretat.
     *
     * @param token El token JWT d'autenticació.
     * @return      Una llista de Treballadors convertits a ClientDTO.
     * @throws SecurityException Si el token és expirat o l'usuari no té rol d'admin.
     */
    @Override
    public List<TreballadorDTO> getAllTreballadors(String token) {
        String tokenNoBearer = Utils.extractBearerToken(token);
        Rol rol = jwtService.getRolFromToken(tokenNoBearer);

        if (!jwtService.isTokenExpired(Utils.extractBearerToken(token)) &&
                Rol.ADMIN == rol) {
            List<Treballador> treballadors = treballadorRepository.findAll();

            return treballadors.stream()
                    .map(TreballadorMapper::toDto)
                    .collect(Collectors.toList());
        }
        if (Rol.TREBALLADOR == rol){//nomes retornarà el treballador si mateixa clinica
            Optional<Treballador> treballador = treballadorRepository.findById(jwtService.getIdFromToken(tokenNoBearer));
            Long clinicaId = treballador.get().getClinica().getIdClinica();
            List<Treballador> treballadorsMateixaClinica = treballadorRepository.findByClinicaId(clinicaId);
            return treballadorsMateixaClinica.stream()
                    .map(TreballadorMapper::toDto)
                    .collect(Collectors.toList());
        }
        throw new SecurityException("Token expirat o no ADMIN");
    }

    /**
     * Actualitza les dades d'un treballador existent. Aquesta operació només es pot realitzar per un usuari amb rol d'admin.
     * Si el token JWT és vàlid i l'usuari és administrador, es realitza l'actualització de les dades del treballador.
     * En cas contrari, es llença una excepció de seguretat.
     *
     * @param treballadorDTO Els nous detalls del treballador a actualitzar.
     * @param token     El token JWT d'autenticació.
     * @return          Retorna un valor boolean que indica si l'actualització s'ha realitzat amb èxit.
     * @throws SecurityException Si el token és expirat o l'usuari no té rol d'admin.
     */
    @Override
    public boolean updateTreballador(TreballadorDTO treballadorDTO, String token) {
        String tokenNoBearer = Utils.extractBearerToken(token);
        if (!jwtService.isTokenExpired(tokenNoBearer) &&
                (Rol.ADMIN == jwtService.getRolFromToken(tokenNoBearer))){
            Treballador treballador = treballadorRepository.findById(treballadorDTO.getIdUsuari()).orElse(null);

            if (treballador == null) {
                return false;
            }

            Treballador t = TreballadorMapper.toEntity(treballadorDTO,
                    clinicaService.getClinicaById(treballadorDTO.getClinicaId()));

            t.setContrasenya(treballador.getContrasenya());
            t.setIdUsuari(treballador.getIdUsuari());
            treballadorRepository.save(t);
            return true;

        }
        throw new SecurityException("Token expirat o no ADMIN");
    }

    /**
     * Elimina un treballador de la base de dades. Aquesta operació només es pot realitzar per un usuari amb rol d'admin.
     * Si el token JWT és vàlid i l'usuari és administrador, es procedeix a eliminar el treballador. En cas contrari, es llença una excepció de seguretat.
     *
     * @param id     L'identificador del treballador a eliminar.
     * @param token  El token JWT d'autenticació.
     * @return       Retorna -1 si el treballador no existeix, 1 si l'eliminació és exitosa, i 0 si no s'ha pogut eliminar.
     * @throws SecurityException Si el token és expirat o l'usuari no té rol d'admin.
     */
    @Override
    public int deleteTreballador(Long id, String token) {
        if (!jwtService.isTokenExpired(Utils.extractBearerToken(token)) &&
                (Rol.ADMIN == jwtService.getRolFromToken(Utils.extractBearerToken(token)))) {
            Treballador treballador = treballadorRepository.findById(id).orElse(null);

            if (treballador == null) {
                return -1;
            }

            treballadorRepository.deleteById(id);
            usuariRepositori.deleteById(id);

            treballador = treballadorRepository.findById(id).orElse(null);
            if(treballador == null) {
                return 1;
            }
            return 0;
        }
        throw new SecurityException("Token expirat o no ADMIN");
    }
}
