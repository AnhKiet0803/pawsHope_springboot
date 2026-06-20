package group3.paws_hope.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter; // Lưu ý import đúng class này

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Cho phép tất cả các nguồn cổng (React của bạn) kết nối sang
        configuration.setAllowedOriginPatterns(List.of("*"));

        // Cho phép đầy đủ các phương thức gửi data
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Cho phép tất cả các Header
        configuration.setAllowedHeaders(List.of("*"));

        // Bắt buộc phải có khi dùng JWT: Cho phép React đọc được header "Authorization" chứa Token
        configuration.setExposedHeaders(List.of("Authorization"));

        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        // Trả về một CorsFilter chạy ở tầng cấu hình thấp nhất (vòng ngoài cùng)
        return new CorsFilter(source);
    }
}