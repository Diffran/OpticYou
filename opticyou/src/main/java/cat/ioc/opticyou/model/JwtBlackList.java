package cat.ioc.opticyou.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="blacklisted",schema = "opticyou")
public class JwtBlackList {
    @Id
    @Column(name = "token", nullable = false, unique = true,length = 255)
    private String token;

    public JwtBlackList(String token) {
        this.token = token;
    }
    public JwtBlackList(){}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
