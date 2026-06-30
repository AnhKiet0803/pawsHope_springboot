package group3.paws_hope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@SpringBootApplication
public class PawsHopeApplication {
	public static void main(String[] args) {
		SpringApplication.run(PawsHopeApplication.class, args);
	}
}
