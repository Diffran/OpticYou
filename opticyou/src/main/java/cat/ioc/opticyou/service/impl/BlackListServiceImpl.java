package cat.ioc.opticyou.service.impl;

import cat.ioc.opticyou.model.JwtBlackList;
import cat.ioc.opticyou.repositori.BlackListRespository;
import cat.ioc.opticyou.service.BlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlackListServiceImpl implements BlackListService {
    @Autowired
    private BlackListRespository jwtBlackListRepository;

    public BlackListServiceImpl(BlackListRespository jwtBlackListRepository) {
        this.jwtBlackListRepository = jwtBlackListRepository;
    }

    @Override
    public boolean isTokenBlackListed(String token){
        return jwtBlackListRepository.existsByToken(token);
    }

    @Override
    public boolean logout(String token){
        if (!jwtBlackListRepository.existsByToken(token)) {
            jwtBlackListRepository.save(new JwtBlackList(token));
            return true;
        }
        return  false;
    }
}
