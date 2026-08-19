package arthurrodrigues.monitorvagas.infrastructure.security;

import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Service
public class TokenService {
    @Value("${secretKeyJwt}")
    private String secretKeyJWT;

    private Instant getExpirationDate(){
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }

    public String generateToken(Usuario usuario){
        // Método que gera o token retornado para o usuário

        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKeyJWT);
            String token = JWT.create()
                    .withIssuer("monitor-vagas-api")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);

            return token;

        } catch(JWTCreationException e){
            log.error("Erro ao gerar o token JWT: {}", e.getMessage());
            throw new RuntimeException("Ocorreu um erro ao gerar o token");
        }
    }

    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKeyJWT);
            return JWT.require(algorithm)
                    .withIssuer("monitor-vagas-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch(JWTVerificationException e){
            log.error("Token inválido: {}", e.getMessage());
            return "";
        }
    }
}