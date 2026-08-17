package arthurrodrigues.monitorvagas.infrastructure.converter;

import arthurrodrigues.monitorvagas.business.dtos.PreferenciaUsuarioDTO;
import arthurrodrigues.monitorvagas.infrastructure.entity.PreferenciaUsuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PreferenciaUsuarioConverter {
    @Mapping(target = "Id", ignore = true)
    public PreferenciaUsuario toPreferenciaUsuario(PreferenciaUsuarioDTO preferenciaUsuarioDTO);
    public PreferenciaUsuarioDTO toPreferenciaUsuarioDTO(PreferenciaUsuario preferenciaUsuario);
}
