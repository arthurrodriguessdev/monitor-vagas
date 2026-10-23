package arthurrodrigues.monitorvagas.infrastructure.security;

import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.RequestContextFilter;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Slf4j
@Service
public class TokenService {
    @Value("${secretKeyJwt}")
    private String secretKeyJWT;
    @Value("${SECRET_KEY_JWT_REFRESHTOKEN}")
    private String secretKeyRefreshToken;

    private Algorithm getAlgorithm(String secretKey){
        return Algorithm.HMAC256(secretKey);
    }
    private Instant getExpirationDate(String typeToken){
        int diasOuHoras = typeToken.equals("access") ? 1 : 3;
        if(diasOuHoras == 1){
            return LocalDateTime.now().plusHours(diasOuHoras).toInstant(ZoneOffset.of("-03:00"));
        }

        return LocalDateTime.now().plusDays(diasOuHoras).toInstant(ZoneOffset.of("-03:00"));
    }

    public String generateAccessToken(Usuario usuario, Instant expirationDate){
        try{
            String token = JWT.create()
                    .withIssuer("monitor-vagas-api")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(expirationDate)
                    .withClaim("type", "access")
                    .sign(getAlgorithm(secretKeyJWT));

            return token;

        } catch(JWTCreationException e){
            log.error("Erro ao gerar o token JWT: {}", e.getMessage());
            throw new RuntimeException("Ocorreu um erro ao gerar o token");
        }
    }

    public String generateRefreshToken(Usuario usuario, Instant expirationDate){
        try{
            String token = JWT.create()
                    .withIssuer("monitor-vagas-api")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(expirationDate)
                    .withClaim("type", "refresh")
                    .sign(getAlgorithm(secretKeyRefreshToken));

            return token;

        } catch(JWTCreationException e){
            log.error("Erro ao gerar o token JWT: {}", e.getMessage());
            throw new RuntimeException("Ocorreu um erro ao gerar o token");
        }
    }

    public String getRefreshToken(Usuario usuario){
        Instant expirationDate = getExpirationDate("refresh");
        return generateRefreshToken(usuario, expirationDate);
    }

    public String getAccessToken(Usuario usuario){
        Instant expirationDate = getExpirationDate("access");
        return generateAccessToken(usuario, expirationDate);
    }

    public String getAccessToken(Usuario usuario, String refreshToken){
        String usernameSubject = validadeRefreshToken(refreshToken);
        if(!usernameSubject.equals(usuario.getUsername())){
            throw new JWTVerificationException("O refresh token não pertence ao usuário");
        }

        Instant expirationDate = getExpirationDate("access");
        return generateAccessToken(usuario, expirationDate);
    }

    public String getSubjectToken(String token, String typeToken){
        String secretKey = switch (typeToken) {
            case "access" -> secretKeyJWT;
            case "refresh" -> secretKeyRefreshToken;
            default -> throw new IllegalArgumentException("O tipo de chave não é válida.");
        };

        log.warn("Verificando o {} token", typeToken);
        return JWT.require(getAlgorithm(secretKey))
                .withIssuer("monitor-vagas-api")
                .withClaim("type", typeToken)
                .build()
                .verify(token)
                .getSubject();
    }

    public String validateAccessToken(String token){
        return getSubjectToken(token, "access");
    }
    public String validadeRefreshToken(String token){
        return getSubjectToken(token, "refresh");
    }
}