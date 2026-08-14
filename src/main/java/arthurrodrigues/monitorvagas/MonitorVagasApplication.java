package arthurrodrigues.monitorvagas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonitorVagasApplication {
	public static void main(String[] args) {
		SpringApplication.run(MonitorVagasApplication.class, args);
	}
}