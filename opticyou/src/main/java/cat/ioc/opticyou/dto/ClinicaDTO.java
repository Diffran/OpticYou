package cat.ioc.opticyou.dto;

import java.util.Objects;

/**
 * DTO que representa la informació d'una clínica.
 * Conté l'ID de la clínica, el nom, la direcció, el telèfon, l'horari d'obertura,
 * l'horari de tancament i l'email de la clínica.
 */
public class ClinicaDTO {
    private Long idClinica;
    private String nom;
    private String direccio;
    private String telefon;
    private String horari_opertura;
    private String horari_tancament;
    private String email;

    public ClinicaDTO() {
    }

    public ClinicaDTO(Long idClinica, String nom, String direccio, String telefon, String horari_opertura, String horari_tancament, String email) {
        this.idClinica = idClinica;
        this.nom = nom;
        this.direccio = direccio;
        this.telefon = telefon;
        this.horari_opertura = horari_opertura;
        this.horari_tancament = horari_tancament;
        this.email = email;
    }

    public Long getIdClinica() {
        return idClinica;
    }

    public void setIdClinica(Long idClinica) {
        this.idClinica = idClinica;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getHorari_opertura() {
        return horari_opertura;
    }

    public void setHorari_opertura(String horari_opertura) {
        this.horari_opertura = horari_opertura;
    }

    public String getHorari_tancament() {
        return horari_tancament;
    }

    public void setHorari_tancament(String horari_tancament) {
        this.horari_tancament = horari_tancament;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClinicaDTO that)) return false;
        return Objects.equals(getIdClinica(), that.getIdClinica()) && Objects.equals(getNom(), that.getNom()) && Objects.equals(getDireccio(), that.getDireccio()) && Objects.equals(getTelefon(), that.getTelefon()) && Objects.equals(getHorari_opertura(), that.getHorari_opertura()) && Objects.equals(getHorari_tancament(), that.getHorari_tancament()) && Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdClinica(), getNom(), getDireccio(), getTelefon(), getHorari_opertura(), getHorari_tancament(), getEmail());
    }
}
