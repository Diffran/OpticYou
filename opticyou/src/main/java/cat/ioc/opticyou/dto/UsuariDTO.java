package cat.ioc.opticyou.dto;



public class UsuariDTO {
    private Long idUsuari;
    private String nom;
    private String email;
    private String contrasenya;
    private String rol;

    public UsuariDTO() {
        this.idUsuari = idUsuari;
    }
    public UsuariDTO(Long idUsuari, String nom, String email, String rol, String contrasenya) {
        this.idUsuari = idUsuari;
        this.nom = nom;
        this.email = email;
        this.rol = rol;
        this.contrasenya=contrasenya;
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

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Long getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(Long idUsuari) {
        this.idUsuari = idUsuari;
    }
}
