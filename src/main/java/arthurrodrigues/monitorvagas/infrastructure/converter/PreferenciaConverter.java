package arthurrodrigues.monitorvagas.infrastructure.converter;

import arthurrodrigues.monitorvagas.business.dtos.PreferenciaDTO;
import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PreferenciaConverter {
    public Preferencia toPreferencia(PreferenciaDTO preferenciaDTO);
    public PreferenciaDTO toPreferenciaDTO(Preferencia preferencia);
}