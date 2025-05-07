package cat.ioc.opticyou.dto;
import java.sql.Timestamp;
public class DiagnosticDTO {
    private Long iddiagnostic;
    private String descripcio;
    private Timestamp date;
    private Long historialId;

    public DiagnosticDTO() {
    }

    public DiagnosticDTO(Long iddiagnostic, String descripcio, Timestamp date, Long historialId) {
        this.iddiagnostic = iddiagnostic;
        this.descripcio = descripcio;
        this.date = date;
        this.historialId = historialId;
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

    public Long getHistorialId() {
        return historialId;
    }

    public void setHistorialId(Long historialId) {
        this.historialId = historialId;
    }

}
