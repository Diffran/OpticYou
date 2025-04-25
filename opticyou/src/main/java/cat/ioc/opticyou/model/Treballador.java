package cat.ioc.opticyou.model;

import cat.ioc.opticyou.util.Rol;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="treballador",schema="opticyou")
@PrimaryKeyJoinColumn(name = "idtreballador")
public class Treballador extends Usuari{
    private String especialitat;

    private String estat;

    @Column(name = "inici_jornada")
    private String iniciJornada;

    @Column(name = "dies_jornada")
    private String diesJornada;

    @Column(name = "fi_jornada")
    private String fiJornada;

    @ManyToOne
    @JoinColumn(name = "clinica_idclinica")
    private Clinica clinica;

    public Treballador() {
    }

    public Treballador(String especialitat, String estat, String iniciJornada, String diesJornada, String fiJornada, Clinica clinica) {
        this.especialitat = especialitat;
        this.estat = estat;
        this.iniciJornada = iniciJornada;
        this.diesJornada = diesJornada;
        this.fiJornada = fiJornada;
        this.clinica = clinica;
    }

    public Treballador(Long idUsuari, String nom, String email, String contrasenya, Rol rol, String especialitat, String estat, String iniciJornada, String diesJornada, String fiJornada, Clinica clinica) {
        super(idUsuari, nom, email, contrasenya, rol);
        this.especialitat = especialitat;
        this.estat = estat;
        this.iniciJornada = iniciJornada;
        this.diesJornada = diesJornada;
        this.fiJornada = fiJornada;
        this.clinica = clinica;
    }

    public String getEspecialitat() {
        return especialitat;
    }

    public void setEspecialitat(String especialitat) {
        this.especialitat = especialitat;
    }

    public String getEstat() {
        return estat;
    }

    public void setEstat(String estat) {
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

    public Clinica getClinica() {
        return clinica;
    }

    public void setClinica(Clinica clinica) {
        this.clinica = clinica;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Treballador that)) return false;
        return Objects.equals(getEspecialitat(), that.getEspecialitat()) && getEstat() == that.getEstat() && Objects.equals(getIniciJornada(), that.getIniciJornada()) && Objects.equals(getDiesJornada(), that.getDiesJornada()) && Objects.equals(getFiJornada(), that.getFiJornada()) && Objects.equals(getClinica(), that.getClinica());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEspecialitat(), getEstat(), getIniciJornada(), getDiesJornada(), getFiJornada(), getClinica());
    }
}
