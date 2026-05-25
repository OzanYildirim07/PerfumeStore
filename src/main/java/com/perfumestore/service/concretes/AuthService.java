package com.perfumestore.service.concretes;

import com.perfumestore.dto.request.LoginRequest;
import com.perfumestore.dto.request.RegisterRequest;
import com.perfumestore.dto.response.AuthResponse;
import com.perfumestore.entity.User;
import com.perfumestore.exception.BusinessException;
import com.perfumestore.repository.UserRepository;
import com.perfumestore.security.JwtTokenProvider;
import com.perfumestore.service.abstracts.IAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // 1. Kullanıcının inputa ne yazdığını buluyoruz (Kullanıcı adı mı yoksa mail mi?)
        String loginInput = request.getUsername();
        String finalUsername = loginInput;

        // 2. Eğer input içinde '@' işareti varsa, kullanıcı kesin mail girmiştir kanka!
        if (loginInput.contains("@")) {
            // Hemen repodaki o ayrı duran findByEmail metodunu tetikliyoruz
            User userByEmail = userRepository.findByEmail(loginInput)
                    .orElseThrow(() -> new BusinessException("User not found with this email"));

            // Spring Security arka planda hala username beklediği için,
            // veri tabanından bulduğumuz gerçek kullanıcı adını (Örn: Ozan12) sisteme paslıyoruz kanka. Sihir burada!
            finalUsername = userByEmail.getUsername();
        }

        // 3. Spring Security artık her türlü gerçek kullanıcı adıyla doğrulama yapıyor, sistem asla bozulmaz!
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(finalUsername, request.getPassword())
        );

        String token = jwtTokenProvider.generateToken(authentication);

        // 4. Son olarak kullanıcı nesnesini çekip response'u jilet gibi dönüyoruz kanka
        User user = userRepository.findByUsername(finalUsername)
                .orElseThrow(() -> new BusinessException("User not found"));

        return new AuthResponse(token, "Bearer", user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email is already in use");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.USER);
        userRepository.save(user);
        String token = jwtTokenProvider.generateToken(user.getUsername());
        return new AuthResponse(token, "Bearer", user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());
    }
}
