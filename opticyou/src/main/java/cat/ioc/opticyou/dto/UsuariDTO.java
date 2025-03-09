package cat.ioc.opticyou.dto;



public class UsuariDTO {
    private Long idUsuari;
    private String nom;
    private String email;
    private boolean isAdmin;

    public UsuariDTO() {
        this.idUsuari = idUsuari;
    }
    public UsuariDTO(Long idUsuari, String nom, String email, boolean isAdmin) {
        this.idUsuari = idUsuari;
        this.nom = nom;
        this.email = email;
        this.isAdmin = isAdmin;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Long getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(Long idUsuari) {
        this.idUsuari = idUsuari;
    }
}
