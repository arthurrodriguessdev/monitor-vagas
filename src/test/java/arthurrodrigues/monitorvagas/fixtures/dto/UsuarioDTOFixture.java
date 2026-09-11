package arthurrodrigues.monitorvagas.fixtures.dto;

import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;
import arthurrodrigues.monitorvagas.infrastructure.entity.VagaRecebida;
import arthurrodrigues.monitorvagas.presentation.dto.UsuarioDTO;

import java.util.List;

public class UsuarioDTOFixture {
    public static UsuarioDTO build(
            Long id,
            String nome,
            String email,
            List<Preferencia> preferencias,
            List<VagaRecebida> vagasRecebidas
    ){
        return new UsuarioDTO(id, nome, email, preferencias, vagasRecebidas);
    }

    public static UsuarioDTO build(){
        return new UsuarioDTO(
                1L,
                "Arthur",
                "emailteste@gmail.com",
                null, null
        );
    }
}
