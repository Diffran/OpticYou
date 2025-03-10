package cat.ioc.opticyou.service;

import cat.ioc.opticyou.dto.JwtAuthenticationResponseDTO;
import cat.ioc.opticyou.dto.LoginRequestDTO;

public interface AuthenticationService {
    //JwtAuthenticationResponse signUp(LoginRequestDTO request);
    JwtAuthenticationResponseDTO login(LoginRequestDTO request);
}
