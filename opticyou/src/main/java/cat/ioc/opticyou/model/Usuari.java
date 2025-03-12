package cat.ioc.opticyou.model;

import jakarta.persistence.*;

@Entity
@Table(name="usuari",schema = "opticyou")
public class Usuari {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idusuari")
    private Long idUsuari;
    private String nom;
    private String email;
    private String contrasenya;
    @Column(name="rol")
    private String rol;

    public Usuari() {
      }

    public Usuari(Long idUsuari, String nom, String email, String contrasenya, String rol) {
        this.idUsuari = idUsuari;
        this.nom = nom;
        this.email = email;
        this.contrasenya = contrasenya;
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
