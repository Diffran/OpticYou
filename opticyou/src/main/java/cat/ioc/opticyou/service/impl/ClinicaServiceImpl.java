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

@Service
public class ClinicaServiceImpl implements ClinicaService {
    @Autowired
    private ClinicaRepository clinicaRepository;
    @Autowired
    private JwtServiceImpl jwtService;

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
