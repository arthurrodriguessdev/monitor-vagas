package arthurrodrigues.monitorvagas.business.dtos;

import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;

import java.util.List;

public record PreferenciaUsuarioDTO(List<Preferencia> preferencias, String email) {}
