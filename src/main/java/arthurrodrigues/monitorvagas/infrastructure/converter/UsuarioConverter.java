package arthurrodrigues.monitorvagas.infrastructure.converter;

import arthurrodrigues.monitorvagas.business.dtos.UsuarioDTO;
import arthurrodrigues.monitorvagas.business.dtos.UsuarioDTORequest;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioConverter {
    UsuarioDTORequest toUsuarioDTORequest(Usuario usuario);
    UsuarioDTO toUsuarioDTO(Usuario usuario);
}
