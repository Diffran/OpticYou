package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.model.JwtBlackList;
import cat.ioc.opticyou.repositori.BlackListRespository;
import cat.ioc.opticyou.service.BlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Gestiona la invalidació de tokens JWT i els posa a la blacklist de la BD
 */
@Service
public class BlackListServiceImpl implements BlackListService {
    @Autowired
    private BlackListRespository jwtBlackListRepository;

    public BlackListServiceImpl(BlackListRespository jwtBlackListRepository) {
        this.jwtBlackListRepository = jwtBlackListRepository;
    }

    /**
     * guarda el token a la blacklist per no poder entrar dues sessions amb el mateix token
     * @param token
     */
    @Override
    public void login(String token){
        jwtBlackListRepository.save(new JwtBlackList(token));
    }

    /**
     * Busca si el token està invalidad
     * @param token
     * @return
     */
    @Override
    public boolean isTokenBlackListed(String token){
        return jwtBlackListRepository.existsByToken(token);
    }

    /**
     * Elimina el token de la blacklisted
     * @param token
     * @return boolean
     */
    @Override
    public boolean logout(String token){
        if (jwtBlackListRepository.existsByToken(token)) {
            jwtBlackListRepository.delete(new JwtBlackList(token));
            return true;
        }
        return  false;
    }
}
