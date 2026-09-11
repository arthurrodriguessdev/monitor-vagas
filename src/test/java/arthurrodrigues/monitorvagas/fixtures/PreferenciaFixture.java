package arthurrodrigues.monitorvagas.fixtures;

import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;

public class PreferenciaFixture {
    public static Preferencia buil(){
        return new Preferencia(
                UsuarioFixture.build(),
                "Java"
        );
    }
}
