package arthurrodrigues.monitorvagas.business.dtos;

import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;
import arthurrodrigues.monitorvagas.infrastructure.entity.VagaRecebida;

import java.util.List;

public record UsuarioDTO (
        Long id,
        String nome,
        String email,
        List<Preferencia> preferencias,
        List<VagaRecebida> vagasRecebidas){}
