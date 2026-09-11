package arthurrodrigues.monitorvagas.fixtures;

import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;

public class UsuarioFixture {
    public static Usuario build(){
        return new Usuario(
                1L,
                "Arthur",
                "emailteste@gmail.com",
                "senhateste",
                null,
                null
        );
    }
}
