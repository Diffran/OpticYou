package cat.ioc.opticyou.dto;


public class JwtAuthenticationResponseDTO {
    private String token;
    public JwtAuthenticationResponseDTO(String token, String userId) {
        this.token = token;
    }
    public JwtAuthenticationResponseDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
}
