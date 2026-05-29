package group3.paws_hope.service;


import group3.paws_hope.dto.req.LoginUser;
import group3.paws_hope.dto.req.RegisterUser;
import group3.paws_hope.dto.res.LoginRes;
import group3.paws_hope.entity.User;
import group3.paws_hope.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public boolean register(RegisterUser input){
        if(userRepository.existsByEmail(input.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        if(userRepository.existsByUsername(input.getUsername())){
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(input.getUsername());
        user.setEmail(input.getEmail());
        user.setFullName(input.getFullName());
        user.setPhone(input.getPhone());
        user.setPasswordHash(passwordEncoder.encode(input.getPassword()));
        user.setRole(User.Role.USER);
        user.setStatus(true);
        userRepository.save(user);
        return true;
    }

    public LoginRes authenticate(LoginUser input){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Email or password is not correct"));
        if (user == null)
            throw new UsernameNotFoundException("Email or password is not correct");
        String jwtToken = jwtService.generateToken(user);
        LoginRes loginRes = new LoginRes();
        loginRes.setToken(jwtToken);
        return loginRes;
    }
}

