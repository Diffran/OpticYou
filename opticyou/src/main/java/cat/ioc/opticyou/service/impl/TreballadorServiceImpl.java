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
                if (treballador.is) {
                    return ClientMapper.toDto(client.get());
                }
            }

            throw new EntityNotFoundException("No hi ha cap client amb el id entrat");
        }

        throw new SecurityException("Token expirat o no autoritzat");
    }

    @Override
    public List<ClientDTO> getAllTreballadors(String token) {
        return List.of();
    }

    @Override
    public boolean updateTreballador(TreballadorDTO treballadorDTO, String token) {
        return false;
    }

    @Override
    public int deleteTreballador(Long id, String token) {
        return 0;
    }
}
