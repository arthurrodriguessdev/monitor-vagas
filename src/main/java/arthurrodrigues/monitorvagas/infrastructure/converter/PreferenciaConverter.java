package arthurrodrigues.monitorvagas.infrastructure.converter;

import arthurrodrigues.monitorvagas.business.dtos.PreferenciaDTO;
import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PreferenciaConverter {
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    public Preferencia toPreferencia(PreferenciaDTO preferenciaDTO);

    @Mapping(source = "preferencia.usuario.id", target = "idUsuario")
    public PreferenciaDTO toPreferenciaDTO(Preferencia preferencia);
}