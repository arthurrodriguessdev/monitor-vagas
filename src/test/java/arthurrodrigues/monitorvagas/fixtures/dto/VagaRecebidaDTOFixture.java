package arthurrodrigues.monitorvagas.fixtures.dto;

import arthurrodrigues.monitorvagas.presentation.dto.VagaRecebidaDTO;

public class VagaRecebidaDTOFixture {
    public static VagaRecebidaDTO build(String tituloVaga, String urlVaga, Long idUsuario){
        return new VagaRecebidaDTO(tituloVaga, urlVaga, idUsuario);
    }

    public static VagaRecebidaDTO build(){
        return new VagaRecebidaDTO("Vaga Teste", "URL Teste", 1L);
    }
}