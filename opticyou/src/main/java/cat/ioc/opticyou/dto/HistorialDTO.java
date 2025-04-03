package cat.ioc.opticyou.dto;

import cat.ioc.opticyou.model.Client;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class HistorialDTO {

    private Long idhistorial;
    private LocalDateTime dataCreacio = LocalDateTime.now();
    private String patologies;
    private Long idClient;

    public HistorialDTO() {
    }

    public HistorialDTO(Long idhistorial, LocalDateTime dataCreacio, String patologies, Long idClient) {
        this.idhistorial = idhistorial;
        this.dataCreacio = dataCreacio;
        this.patologies = patologies;
        this.idClient = idClient;
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

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }
}
