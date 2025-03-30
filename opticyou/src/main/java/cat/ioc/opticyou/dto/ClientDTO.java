package cat.ioc.opticyou.dto;

import cat.ioc.opticyou.model.Client;
import cat.ioc.opticyou.util.Rol;

public class ClientDTO extends UsuariDTO{
    private String telefon;
    private String sexe;
    private String dataNaixament;
    private String contrasenya;
    private Long clinicaId;
    private Long historialId;

    public ClientDTO(){}

    public ClientDTO(Long idUsuari, String nom, String email, String contrasenya, Rol rol, String telefon, String sexe, String dataNaixament, String contrasenya1, Long clinicaId, Long historialId) {
        super(idUsuari, nom, email, contrasenya, rol);
        this.telefon = telefon;
        this.sexe = sexe;
        this.dataNaixament = dataNaixament;
        this.contrasenya = contrasenya1;
        this.clinicaId = clinicaId;
        this.historialId = historialId;
    }

    @Override
    public String getContrasenya() {
        return contrasenya;
    }

    @Override
    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getDataNaixament() {
        return dataNaixament;
    }

    public void setDataNaixament(String dataNaixament) {
        this.dataNaixament = dataNaixament;
    }

    public Long getClinicaId() {
        return clinicaId;
    }

    public void setClinicaId(Long clinicaId) {
        this.clinicaId = clinicaId;
    }

    public Long getHistorialId() {
        return historialId;
    }

    public void setHistorialId(Long historialId) {
        this.historialId = historialId;
    }
}
