package cat.ioc.opticyou.dto;


public class JwtAuthenticationResponseDTO {
    private String token;
    private String userId;

    public JwtAuthenticationResponseDTO(String token, String userId) {
        this.token = token;
        this.userId = userId;
    }
    public JwtAuthenticationResponseDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
