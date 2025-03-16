package cat.ioc.opticyou.mapper;

import cat.ioc.opticyou.dto.UsuariDTO;
import cat.ioc.opticyou.model.Usuari;

/**
 * Classe per mapejar entre Usauri i UsuariDTO
 */
public class UsuariMapper {
    /**
     * converteix un Usuari a UsuariDTO
     * @param usuari
     * @return UsuariDTO
     */
    public static UsuariDTO toDto(Usuari usuari){
        if (usuari == null) {
            return null;
        }
        UsuariDTO usuariDTO = new UsuariDTO();
        usuariDTO.setIdUsuari(usuari.getIdUsuari());
        usuariDTO.setNom(usuari.getNom());
        usuariDTO.setEmail(usuari.getEmail());
        usuariDTO.setRol(usuari.getRol());
        return usuariDTO;
    }

    /**
     * Converteix un UsuariDTO a Usuari
     * @param usuariDTO
     * @return Usuari
     */
    public static Usuari toEntity(UsuariDTO usuariDTO){
        if (usuariDTO == null) {
            return null;
        }
        Usuari usuari = new Usuari();
        usuari.setIdUsuari(usuariDTO.getIdUsuari());
        usuari.setNom(usuariDTO.getNom());
        usuari.setEmail(usuariDTO.getEmail());
        usuari.setRol(usuariDTO.getRol());
        return usuari;
    }
}
