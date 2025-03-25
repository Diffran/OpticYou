package cat.ioc.opticyou.service;

import cat.ioc.opticyou.dto.ClinicaDTO;

import java.util.List;

public interface ClinicaService {
    int createClinica(ClinicaDTO clinicaDTO, String token);
    ClinicaDTO getClinicaById(Long id, String token);

    List<ClinicaDTO> getAllClinicas(String token);
    boolean updateClinica(ClinicaDTO clinicaDTO, String token);

    boolean deleteClinica(Long id, String token);
}
