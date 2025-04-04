package cat.ioc.opticyou.dto.loginLogout;

/**
 * DTO que representa la resposta d'inici de sessió per al client
 * té si ha sigut success, el token creat i el rol del usuari
 */
public class ClientLoginResponseDTO {
    private boolean success;
    private String token;
    private String rol;

    public ClientLoginResponseDTO(boolean success, String token, String rol) {
        this.success = success;
        this.token = token;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
