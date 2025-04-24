package cat.ioc.opticyou.service;

import cat.ioc.opticyou.dto.ClientDTO;
import cat.ioc.opticyou.dto.TreballadorDTO;

import java.util.List;

public interface TreballadorService {
    int createTreballador(TreballadorDTO treballadorDTO, String token);
    TreballadorDTO getTreballadorById(Long id, String token);
    List<TreballadorDTO> getAllTreballadors(String token);
    boolean updateTreballador(TreballadorDTO treballadorDTO, String token);
    int deleteTreballador(Long id, String token);
}
