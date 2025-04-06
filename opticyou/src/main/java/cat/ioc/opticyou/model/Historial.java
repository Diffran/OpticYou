package cat.ioc.opticyou.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Classe que representa l'historial d'un client.
 * Conté informació sobre les patologies del client i la data de creació.
 * Cada client té un historial associat, que és únic per a cada client.
 * Les actualitzacions d'aquest historial es realitzen en cascada a través de la relació amb la classe Client.
 */
@Entity
@Table(name = "historial", schema = "opticyou")
public class Historial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idhistorial;

    @Column(name = "data_creacio", nullable = false, updatable = false)
    private LocalDateTime dataCreacio = LocalDateTime.now();

    @Column(length = 450)
    private String patologies;

    @OneToOne(mappedBy = "historial", cascade = CascadeType.ALL)
    @JsonIgnore
    private Client client;

    public Historial() {
    }

    public Historial(Long idhistorial, LocalDateTime dataCreacio, String patologies, Client client) {
        this.idhistorial = idhistorial;
        this.dataCreacio = dataCreacio;
        this.patologies = patologies;
        this.client = client;
    }

    public Long getIdhistorial() {
        return idhistorial;
    }

    public void setIdhistorial(Long idhistorial) {
        this.idhistorial = idhistorial;
    }

    public LocalDateTime getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(LocalDateTime dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public String getPatologies() {
        return patologies;
    }

    public void setPatologies(String patologies) {
        this.patologies = patologies;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
