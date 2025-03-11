package cat.ioc.opticyou.dto;


import cat.ioc.opticyou.util.Rol;

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
}
