package cat.ioc.opticyou.dto;


import cat.ioc.opticyou.util.Rol;

import java.util.Objects;

/**
 * DTO que representa la informació d'un usuari
 * té id, nom, email i el rol del usuari, no mostra MAI la contrasenya
 */
public class UsuariDTO {
    private Long idUsuari;
    private String nom;
    private String email;
    private Rol rol;

    public UsuariDTO() {
        this.idUsuari = idUsuari;
    }
    public UsuariDTO(Long idUsuari, String nom, String email, Rol rol) {
        this.idUsuari = idUsuari;
        this.nom = nom;
        this.email = email;
        this.rol = rol;
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

    public Long getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(Long idUsuari) {
        this.idUsuari = idUsuari;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuariDTO usuariDTO)) return false;
        return Objects.equals(getIdUsuari(), usuariDTO.getIdUsuari()) && Objects.equals(getNom(), usuariDTO.getNom()) && Objects.equals(getEmail(), usuariDTO.getEmail()) && getRol() == usuariDTO.getRol();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdUsuari(), getNom(), getEmail(), getRol());
    }
}
