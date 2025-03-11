package cat.ioc.opticyou.model;

import cat.ioc.opticyou.util.Rol;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="usuari",schema = "opticyou")
public class Usuari implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idusuari")
    private Long idUsuari;
    private String nom;
    private String email;
    private String contrasenya;
    @Enumerated(EnumType.STRING)
    private Rol rol;

    public Usuari() {
      }

    public Usuari(Long idUsuari, String nom, String email, String contrasenya, Rol rol) {
        this.idUsuari = idUsuari;
        this.nom = nom;
        this.email = email;
        this.contrasenya = contrasenya;
        this.rol = rol;
    }

    //SEGURETAT
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((rol.name())));
    }

    @Override
    public String getPassword() {
        return this.contrasenya;
    }

    @Override
    public String getUsername() {
        return this.email;
    }


    //GETTERS I SETTERS
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

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Long getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(Long idUsuari) {
        this.idUsuari = idUsuari;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
