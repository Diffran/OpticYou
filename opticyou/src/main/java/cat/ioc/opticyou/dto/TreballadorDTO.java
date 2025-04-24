package cat.ioc.opticyou.dto;

import cat.ioc.opticyou.util.EstatTreballador;
import cat.ioc.opticyou.util.Rol;

import java.util.Objects;

/**
 * DTO que representa la informació d'un treballador.
 * Conté l'ID del treballador, el nom, l'email, l'especialitat, l'estat (actiu/inactiu),
 * l'horari d'inici, els dies de la jornada, l'horari de fi de jornada, i l'ID de la clínica associada.
 */
public class TreballadorDTO extends UsuariDTO{
    private Long idUsuari;
    private String nom;
    private String email;
    private String especialitat;
    private EstatTreballador estat;  // Estat pot ser 'actiu' o 'inactiu' (en minúscules)
    private String iniciJornada;
    private String diesJornada;
    private String fiJornada;
    private Long clinicaId;

    public TreballadorDTO() {
    }

    public TreballadorDTO(Long idUsuari, String nom, String email, String contrasenya, Rol rol, Long idUsuari1, String nom1, String email1, String especialitat, EstatTreballador estat, String iniciJornada, String diesJornada, String fiJornada, Long clinicaId) {
        super(idUsuari, nom, email, contrasenya, rol);
        this.idUsuari = idUsuari1;
        this.nom = nom1;
        this.email = email1;
        this.especialitat = especialitat;
        this.estat = estat;
        this.iniciJornada = iniciJornada;
        this.diesJornada = diesJornada;
        this.fiJornada = fiJornada;
        this.clinicaId = clinicaId;
    }

    public Long getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(Long idUsuari) {
        this.idUsuari = idUsuari;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEspecialitat() {
        return especialitat;
    }

    public void setEspecialitat(String especialitat) {
        this.especialitat = especialitat;
    }

    public EstatTreballador getEstat() {
        return estat;
    }

    public void setEstat(EstatTreballador estat) {
        this.estat = estat;
    }

    public String getIniciJornada() {
        return iniciJornada;
    }

    public void setIniciJornada(String iniciJornada) {
        this.iniciJornada = iniciJornada;
    }

    public String getDiesJornada() {
        return diesJornada;
    }

    public void setDiesJornada(String diesJornada) {
        this.diesJornada = diesJornada;
    }

    public String getFiJornada() {
        return fiJornada;
    }

    public void setFiJornada(String fiJornada) {
        this.fiJornada = fiJornada;
    }

    public Long getClinicaId() {
        return clinicaId;
    }

    public void setClinicaId(Long clinicaId) {
        this.clinicaId = clinicaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreballadorDTO that)) return false;
        return Objects.equals(getIdUsuari(), that.getIdUsuari()) && Objects.equals(getNom(), that.getNom()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getEspecialitat(), that.getEspecialitat()) && Objects.equals(getEstat(), that.getEstat()) && Objects.equals(getIniciJornada(), that.getIniciJornada()) && Objects.equals(getDiesJornada(), that.getDiesJornada()) && Objects.equals(getFiJornada(), that.getFiJornada()) && Objects.equals(getClinicaId(), that.getClinicaId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdUsuari(), getNom(), getEmail(), getEspecialitat(), getEstat(), getIniciJornada(), getDiesJornada(), getFiJornada(), getClinicaId());
    }
}
