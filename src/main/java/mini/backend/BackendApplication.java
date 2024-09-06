package mini.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BackendApplication {

	// TODO 이거 언제 다 하냐
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
