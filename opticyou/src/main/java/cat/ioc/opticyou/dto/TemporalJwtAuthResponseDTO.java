package cat.ioc.opticyou.dto;

public class TemporalJwtAuthResponseDTO {
    private String token;
    private UsuariDTO usuariDTO;

    public TemporalJwtAuthResponseDTO(String token, UsuariDTO usuariDTO) {
        this.token = token;
        this.usuariDTO = usuariDTO;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UsuariDTO getUsuariDTO() {
        return usuariDTO;
    }

    public void setUsuariDTO(UsuariDTO usuariDTO) {
        this.usuariDTO = usuariDTO;
    }
}
