package arthurrodrigues.monitorvagas.fixtures.dto;

import arthurrodrigues.monitorvagas.presentation.dto.PreferenciaDTO;

public class PreferenciaDTOFixture {
    public static PreferenciaDTO build(Long idUsuario, String nome){
        return new PreferenciaDTO(idUsuario, nome);
    }

    public static PreferenciaDTO build(){
        return new PreferenciaDTO(1L, "Python");
    }
}