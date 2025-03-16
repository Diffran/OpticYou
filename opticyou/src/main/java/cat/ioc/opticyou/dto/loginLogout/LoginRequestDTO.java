package cat.ioc.opticyou.dto.loginLogout;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * DTO que representa la sol·licitud d'inici de sessió del usuari.
 * demana el email i la contrasenya.
 */
public class LoginRequestDTO {
    @Email
    private String email;
    @NotNull
    private String password;

    public LoginRequestDTO(){}
    public LoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
