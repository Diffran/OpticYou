package cat.ioc.opticyou.config;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


    @Configuration
    public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            // Permitir solicitudes desde localhost:3000 (frontend)
            registry.addMapping("/**") // Aplica a todas las rutas
                    .allowedOrigins("http://localhost:3000") // Permite solicitudes desde http://localhost:3000
                    .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos permitidos
                    .allowedHeaders("*") // Permite todos los encabezados
                    .allowCredentials(true);  // Permitir cookies o credenciales
        }
    }

