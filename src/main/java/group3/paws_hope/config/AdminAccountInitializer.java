package group3.paws_hope.config;

import group3.paws_hope.entity.User;
import group3.paws_hope.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminAccountInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPasswordHash("123456");
                admin.setEmail("admin@pawshope.com");
                admin.setFullName("System Admin");

                userRepository.save(admin);
                System.out.println(" TẠO TÀI KHOẢN ADMIN THÀNH CÔNG!");
            }
        };
    }
}