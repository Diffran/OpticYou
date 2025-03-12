package cat.ioc.opticyou.service;

import cat.ioc.opticyou.dto.UsuariDTO;

import java.util.List;

public interface UsuariService {
    void update(String email);
    void delete(String email);
    UsuariDTO getByEmail(String email);
    List<UsuariDTO> getAll();
    public boolean authenticate(String email, String contrasenya);
}
