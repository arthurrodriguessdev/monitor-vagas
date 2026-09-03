package arthurrodrigues.monitorvagas.infrastructure.converter;

import arthurrodrigues.monitorvagas.presentation.dto.UsuarioDTO;
import arthurrodrigues.monitorvagas.presentation.dto.UsuarioDTORequest;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioConverter {
    UsuarioDTORequest toUsuarioDTORequest(Usuario usuario);
    UsuarioDTO toUsuarioDTO(Usuario usuario);
//    Usuario toUsuario(UsuarioDTO usuarioDTO);
}
