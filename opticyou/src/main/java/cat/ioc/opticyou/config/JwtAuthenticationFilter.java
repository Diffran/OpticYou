package cat.ioc.opticyou.config;

import cat.ioc.opticyou.service.JwtService;
import cat.ioc.opticyou.service.UsuariService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filtra les sol·licituds al servidor per verificar si el token del header és vàlid. Si ho és estableix autenticació del usuari
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuariService usuariService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UsuariService usuariService) {
        this.jwtService = jwtService;
        this.usuariService = usuariService;
    }
    /**
     * Filtra les sol·licituds HTTP per verificar el token JWT en el header.
     * Si el token és vàlid, estableix l'autenticació, el dona per bo.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response
            , @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        final String email;

        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token=getTokenFromRequest(request);
        email = jwtService.getEmailFromToken(token);

        if(StringUtils.isNotEmpty(email) && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = usuariService.userDetailsService().loadUserByUsername(email);

            if(jwtService.isTokenValid(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     *Extreu el token del header
     *
     * @param request sol·licitud HTTP.
     * @return El token JWT com a cadena.
     */
    private String getTokenFromRequest(HttpServletRequest request){
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return authHeader.substring(7);
    }
}
