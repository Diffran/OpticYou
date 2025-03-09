package cat.ioc.opticyou.mapper;

import cat.ioc.opticyou.dto.UsuariDTO;
import cat.ioc.opticyou.model.Usuari;

public class UsuariMapper {
    public static UsuariDTO toDto(Usuari usuari){
        if (usuari == null) {
            return null;
        }
        UsuariDTO usuariDTO = new UsuariDTO();
        usuariDTO.setIdUsuari(usuari.getIdUsuari());
        usuariDTO.setNom(usuari.getNom());
        usuariDTO.setEmail(usuari.getEmail());
        usuariDTO.setAdmin(usuari.getAdmin());
        return usuariDTO;
    }
    public static Usuari toEntity(UsuariDTO usuariDTO){
        if (usuariDTO == null) {
            return null;
        }
        Usuari usuari = new Usuari();
        usuari.setIdUsuari(usuariDTO.getIdUsuari());
        usuari.setNom(usuariDTO.getNom());
        usuari.setEmail(usuariDTO.getEmail());
        usuari.setAdmin(usuariDTO.isAdmin());
        return usuari;
    }
}
