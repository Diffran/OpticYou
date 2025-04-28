package cat.ioc.opticyou.config;

import cat.ioc.opticyou.dto.UsuariDTO;
import cat.ioc.opticyou.service.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuració de la seguretat de l'aplicació. Diu que ha de filtrar i com.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UsuariService usuariService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(UsuariService usuariService, JwtAuthenticationFilter jwtAuthenticationFilter){
        this.usuariService = usuariService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    /**
     * Configura la seguretat de l'aplicació. Desactiva CSRF, utilitza la politica stateless,
     * permet l'accés a rutes específiques sense autenticació i afegeix el filtre JWT.
     *
     * @param http El objecte HttpSecurity per configurar la seguretat.
     * @return El SecurityFilterChain configurat.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authHttp -> authHttp
                        .requestMatchers(  "/swagger/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/v3/api-docs/**",
                                "/auth/**")
                        .permitAll().anyRequest().authenticated())
                .authenticationProvider(authenticactionProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .requiresChannel(channel -> channel.anyRequest().requiresSecure());

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    /**
     * Configura el gestor d'autenticació per a l'aplicació.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    /**
     * Configura el proveïdor d'autenticació amb el usuariService i el passwordEncoder
     */
    @Bean
    public AuthenticationProvider authenticactionProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(usuariService.userDetailsService());
          authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Configura el codificador de contrasenyes per l'autenticació. Ara no fa res.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }

}