package ucb.CaresyncApp.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ucb.CaresyncApp.entities.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private User mockUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve retornar um novo tokenJWT")
    void generateTokenCase1() {
        when(mockUser.getUsername()).thenReturn("user");
        String token = tokenService.generateToken(mockUser);
        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    @DisplayName("Deve lançar um erro quando o token for inválido")
    void validateTokenCase1() {

        String invalidToken = "token-invalido";

        assertThrows(RuntimeException.class, () -> {
            tokenService.validateToken(invalidToken);
        });
    }
}
