package cat.ioc.opticyou.model;

import cat.ioc.opticyou.util.Rol;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="client",schema="opticyou")
@PrimaryKeyJoinColumn(name = "idclient")
public class Client extends Usuari {
    private String telefon;
    private String sexe;
    @Column(name = "data_naixament")
    private String dataNaixament;
    @ManyToOne
    @JoinColumn(name = "clinica_idclinica")
    private Clinica clinica;
    @OneToOne
    @JoinColumn(name = "historial_idhistorial", nullable = false)
    private Historial historial;

    public Client(){}

    public Client(String telefon, String sexe, String dataNaixament, Clinica clinica, Historial historial) {

        this.telefon = telefon;
        this.sexe = sexe;
        this.dataNaixament = dataNaixament;
        this.clinica = clinica;
        this.historial = historial;
    }

    public Client(Long idUsuari, String nom, String email, String contrasenya, Rol rol, Long idClient, String telefon, String sexe, String dataNaixament, Clinica clinica, Historial historial) {
        super(idUsuari, nom, email, contrasenya, rol);;
        this.telefon = telefon;
        this.sexe = sexe;
        this.dataNaixament = dataNaixament;
        this.clinica = clinica;
        this.historial = historial;
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

    public Clinica getClinica() {
        return clinica;
    }

    public void setClinica(Clinica clinica) {
        this.clinica = clinica;
    }

    public Historial getHistorial() {
        return historial;
    }

    public void setHistorial(Historial historial) {
        this.historial = historial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return Objects.equals(getTelefon(), client.getTelefon()) && Objects.equals(getSexe(), client.getSexe()) && Objects.equals(getDataNaixament(), client.getDataNaixament()) && Objects.equals(getClinica(), client.getClinica()) && Objects.equals(getHistorial(), client.getHistorial());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTelefon(), getSexe(), getDataNaixament(), getClinica(), getHistorial());
    }
}




