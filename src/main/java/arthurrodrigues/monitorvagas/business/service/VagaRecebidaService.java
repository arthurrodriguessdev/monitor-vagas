package arthurrodrigues.monitorvagas.business.service;

import arthurrodrigues.monitorvagas.presentation.dto.VagaRecebidaDTO;
import arthurrodrigues.monitorvagas.infrastructure.converter.VagaRecebidaConverter;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import arthurrodrigues.monitorvagas.infrastructure.entity.VagaRecebida;
import arthurrodrigues.monitorvagas.infrastructure.exception.UsuarioNotFound;
import arthurrodrigues.monitorvagas.infrastructure.repository.UsuarioRepository;
import arthurrodrigues.monitorvagas.infrastructure.repository.VagaRecebidaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VagaRecebidaService {
    private final VagaRecebidaRepository vagaRecebidaRepository;
    private final VagaRecebidaConverter vagaRecebidaConverter;
    private final UsuarioRepository usuarioRepository;

    public void registrarVagaRecebida(VagaRecebidaDTO vagaRecebidaDTO){
        Usuario usuarioDono = usuarioRepository.findById(vagaRecebidaDTO.idUsuario())
                        .orElseThrow(()->{
                            log.error("Usuário não encontrado para salvar a vaga recebida");
                            return new UsuarioNotFound("Usuário não encontrado");
                        });

        VagaRecebida vagaRecebida = vagaRecebidaConverter.toVagaRecebida(vagaRecebidaDTO);
        vagaRecebida.setUsuario(usuarioDono);

        // Garantir a não duplicidade de chave (urlVaga, idUsuario)
        if(!vagaRecebidaRepository.existsByUrlVagaAndUsuario(vagaRecebidaDTO.urlVaga(), usuarioDono)){
            vagaRecebidaRepository.save(vagaRecebida);
        }
    }
}
