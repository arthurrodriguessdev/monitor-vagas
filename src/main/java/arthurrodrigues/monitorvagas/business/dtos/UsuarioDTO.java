package arthurrodrigues.monitorvagas.business.dtos;

import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;

import java.util.List;

public record UsuarioDTO (String nome, String email, List<Preferencia> preferencias){}
