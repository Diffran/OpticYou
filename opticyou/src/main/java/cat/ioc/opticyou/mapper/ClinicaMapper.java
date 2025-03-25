package cat.ioc.opticyou.mapper;

import cat.ioc.opticyou.dto.ClinicaDTO;
import cat.ioc.opticyou.dto.UsuariDTO;
import cat.ioc.opticyou.model.Clinica;
import cat.ioc.opticyou.model.Usuari;

public class ClinicaMapper {
    public static ClinicaDTO toDto(Clinica clinica){
        if (clinica == null) {
            return null;
        }
        ClinicaDTO clinicaDTO = new ClinicaDTO();
        clinicaDTO.setIdClinica(clinica.getIdClinica());
        clinicaDTO.setNom(clinica.getNom());
        clinicaDTO.setDireccio(clinica.getDireccio());
        clinicaDTO.setTelefon(clinica.getTelefon());
        clinicaDTO.setHorari_opertura(clinica.getHorari_opertura());
        clinicaDTO.setHorari_tancament(clinica.getHorari_tancament());
        clinicaDTO.setEmail(clinica.getEmail());
        return clinicaDTO;
    }

    public static Clinica toEntity(ClinicaDTO clinicaDTO){
        if (clinicaDTO == null) {
            return null;
        }
        Clinica clinica = new Clinica();
        clinica.setIdClinica(clinicaDTO.getIdClinica());
        clinica.setNom(clinicaDTO.getNom());
        clinica.setDireccio(clinicaDTO.getDireccio());
        clinica.setTelefon(clinicaDTO.getTelefon());
        clinica.setHorari_opertura(clinicaDTO.getHorari_opertura());
        clinica.setHorari_tancament(clinicaDTO.getHorari_tancament());
        clinica.setEmail(clinicaDTO.getEmail());
        return clinica;
    }
}
