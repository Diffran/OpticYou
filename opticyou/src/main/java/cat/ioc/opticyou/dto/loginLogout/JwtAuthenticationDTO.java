package cat.ioc.opticyou.dto.loginLogout;


/**
 * DTO que representa la resposta del login.
 * només retorna el token
 */
public class JwtAuthenticationDTO {
    private String token;
    public JwtAuthenticationDTO(String token, String userId) {
        this.token = token;
    }
    public JwtAuthenticationDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
}
