package cat.ioc.opticyou.service;

import cat.ioc.opticyou.model.JwtBlackList;
import cat.ioc.opticyou.repositori.BlackListRespository;
import cat.ioc.opticyou.service.impl.BlackListServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
public class BlackListServiceTest {
    @Mock
    private BlackListRespository jwtBlackListRepository;

    @InjectMocks
    private BlackListServiceImpl blackListService;

    @Test
    public void testIsTokenBlackListed_Success() {//esta a la blackList
        String token = "testToken_success";
        JwtBlackList jwt = new JwtBlackList(token);
        jwtBlackListRepository.save(jwt);

        when(jwtBlackListRepository.existsByToken(token)).thenReturn(true);
        assertTrue(blackListService.isTokenBlackListed(token));
    }

    @Test
    public void testIsTokenBlackListed_Fail() {//no esta a la blackList
        String token = "testToken_fail";

        when(jwtBlackListRepository.existsByToken(token)).thenReturn(false);
        assertFalse(blackListService.isTokenBlackListed(token));
    }

    @Test
    public void testLogout_Success() {
        String token = "testToken_logout";

        when(jwtBlackListRepository.existsByToken(token)).thenReturn(true);

        assertTrue(blackListService.logout(token));
        verify(jwtBlackListRepository).delete(any(JwtBlackList.class));
    }

    @Test
    public void testLogout_Fail() {
        String token = "testToken_logout";

        when(jwtBlackListRepository.existsByToken(token)).thenReturn(true);

        assertTrue(blackListService.logout(token));
        verify(jwtBlackListRepository, never()).save(any(JwtBlackList.class));
    }

}
