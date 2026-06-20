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
            // Kiểm tra xem database đã có user nào chưa (tránh tạo lặp lại mỗi khi Restart)
            // Thay vì kiểm tra count() == 0, ta kiểm tra xem username "admin" đã tồn tại chưa
            // Kiểm tra xem đã có tài khoản "admin" chưa
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPasswordHash("123456");

                // 👇 THÊM CÁC DÒNG NÀY ĐỂ TRÁNH LỖI "CANNOT BE NULL" 👇
                admin.setEmail("admin@pawshope.com"); // Cung cấp email bắt buộc
                admin.setFullName("System Admin");    // Cung cấp họ tên (nếu bắt buộc)

                // Gán quyền Admin (Tuỳ thuộc vào cấu trúc Entity của bạn)
                // Nếu cột role là String:
                // admin.setRole("ADMIN");

                // Nếu cột status là Boolean:
                // admin.setStatus(true);

                userRepository.save(admin);
                System.out.println("==================================================");
                System.out.println("🚀 ĐÃ TẠO TÀI KHOẢN ADMIN THÀNH CÔNG!");
                System.out.println("==================================================");
            }
        };
    }
}