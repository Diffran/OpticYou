package cat.ioc.opticyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class OpticyouApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpticyouApplication.class, args);
	}

}
