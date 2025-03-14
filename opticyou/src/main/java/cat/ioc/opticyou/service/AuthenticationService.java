package cat.ioc.opticyou.service;

import cat.ioc.opticyou.dto.loginLogout.JwtAuthenticationDTO;
import cat.ioc.opticyou.dto.loginLogout.LoginRequestDTO;

public interface AuthenticationService {
    //JwtAuthenticationResponse signUp(LoginRequestDTO request);
    JwtAuthenticationDTO login(LoginRequestDTO request);
}
