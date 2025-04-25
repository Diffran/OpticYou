package cat.ioc.opticyou.mapper;


import cat.ioc.opticyou.dto.TreballadorDTO;
import cat.ioc.opticyou.model.Clinica;
import cat.ioc.opticyou.model.Treballador;
import cat.ioc.opticyou.util.Rol;

public class TreballadorMapper {

    /**
     * Converteix un Treballador a TreballadorDTO
     * @param treballador El treballador a convertir
     * @return TreballadorDTO
     */
    public static TreballadorDTO toDto(Treballador treballador){
        TreballadorDTO treballadorDTO = new TreballadorDTO();

        treballadorDTO.setIdUsuari(treballador.getIdUsuari());
        treballadorDTO.setNom(treballador.getNom());
        treballadorDTO.setEmail(treballador.getEmail());
        treballadorDTO.setRol(Rol.TREBALLADOR);
        treballadorDTO.setEspecialitat(treballador.getEspecialitat());
        treballadorDTO.setEstat(treballador.getEstat());
        treballadorDTO.setIniciJornada(treballador.getIniciJornada());
        treballadorDTO.setDiesJornada(treballador.getDiesJornada());
        treballadorDTO.setFiJornada(treballador.getFiJornada());
        treballadorDTO.setClinicaId(treballador.getClinica() != null ? treballador.getClinica().getIdClinica() : null);

        return treballadorDTO;
    }

    /**
     * Converteix un TreballadorDTO a Treballador
     * @param treballadorDTO El TreballadorDTO a convertir
     * @param clinica        La clínica associada al treballador
     * @return Treballador
     */
    public static Treballador toEntity(TreballadorDTO treballadorDTO, Clinica clinica){
        Treballador treballador = new Treballador();

        treballador.setIdUsuari(treballadorDTO.getIdUsuari());
        treballador.setNom(treballadorDTO.getNom());
        treballador.setEmail(treballadorDTO.getEmail());
        treballador.setContrasenya(treballadorDTO.getContrasenya());
        treballador.setRol(Rol.TREBALLADOR);
        treballador.setEspecialitat(treballadorDTO.getEspecialitat());
        treballador.setEstat(treballadorDTO.getEstat());
        treballador.setIniciJornada(treballadorDTO.getIniciJornada());
        treballador.setDiesJornada(treballadorDTO.getDiesJornada());
        treballador.setFiJornada(treballadorDTO.getFiJornada());
        treballador.setClinica(clinica);

        return treballador;
    }
}
