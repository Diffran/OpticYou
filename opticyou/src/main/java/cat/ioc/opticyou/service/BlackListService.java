package cat.ioc.opticyou.service;

public interface BlackListService {
    boolean isTokenBlackListed(String token);
    boolean logout(String token);
}
