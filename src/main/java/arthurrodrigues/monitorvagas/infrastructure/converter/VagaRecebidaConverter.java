package arthurrodrigues.monitorvagas.infrastructure.converter;

import arthurrodrigues.monitorvagas.presentation.dto.VagaRecebidaDTO;
import arthurrodrigues.monitorvagas.infrastructure.entity.VagaRecebida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VagaRecebidaConverter {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    VagaRecebida toVagaRecebida(VagaRecebidaDTO vagaRecebidaDTO);
}