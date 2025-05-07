package cat.ioc.opticyou.model;

import jakarta.persistence.*;

import java.sql.Timestamp;


@Entity
@Table(name = "diagnostic")
public class Diagnostic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iddiagnostic;
    private String descripcio;
    private Timestamp date;
    @ManyToOne
    @JoinColumn(name = "historial_idhistorial", nullable = false)
    private Historial historial;

    public Diagnostic() {
    }

    public Diagnostic(Long iddiagnostic, String descripcio, Timestamp date, Historial historial) {
        this.iddiagnostic = iddiagnostic;
        this.descripcio = descripcio;
        this.date = date;
        this.historial = historial;
    }

    public Long getIddiagnostic() {
        return iddiagnostic;
    }

    public void setIddiagnostic(Long iddiagnostic) {
        this.iddiagnostic = iddiagnostic;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Historial getHistorial() {
        return historial;
    }

    public void setHistorial(Historial historial) {
        this.historial = historial;
    }
}
