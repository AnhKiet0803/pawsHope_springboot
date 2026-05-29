package group3.paws_hope.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenicationFilter jwtAuthenicationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry
                                ->authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/adoption_guidelines/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/donation_campaigns/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/donations").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/expenses/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/item_donations").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/organization_info").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/pets/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/pet_status_logs/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/rescue_reports").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/rescue_reports/tracking/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/volunteer_applications").permitAll()
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                // thêm cách xử lí token(jwt) để biết thông tin (provider)
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenicationFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); //Get, Post, Put, Delete
        configuration.addAllowedOrigin("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", configuration);
        //source.registerCorsConfiguration("/api/v2/**", configuration);
        return source;
    }
}

