package arthurrodrigues.monitorvagas.business.service;

import arthurrodrigues.monitorvagas.infrastructure.exception.PreferenciaNotFoundException;
import arthurrodrigues.monitorvagas.presentation.dto.PreferenciaDTO;
import arthurrodrigues.monitorvagas.business.utils.SecurityUtils;
import arthurrodrigues.monitorvagas.infrastructure.converter.PreferenciaConverter;
import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import arthurrodrigues.monitorvagas.infrastructure.exception.UsuarioNotFound;
import arthurrodrigues.monitorvagas.infrastructure.repository.PreferenciaRepository;
import arthurrodrigues.monitorvagas.infrastructure.repository.UsuarioRepository;
import arthurrodrigues.monitorvagas.infrastructure.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PreferenciaService {
    private final PreferenciaRepository preferenciaRepository;
    private final PreferenciaConverter preferenciaConverter;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;

    private Usuario getUsuario(String token){
        String emailUsuario = tokenService.getSubjectToken(
                SecurityUtils.tokenFormat(token));

        Usuario usuario = (Usuario) usuarioRepository.findByEmail(emailUsuario);
        if(usuario == null){
            throw new UsuarioNotFound("Usuário não encontrado");
        }

        return usuario;
    }

    public PreferenciaDTO adicionarPreferencia(String nome, String token){
        Usuario usuario = getUsuario(token);
        Preferencia preferencia = preferenciaRepository.save(new Preferencia(usuario, nome));
        return preferenciaConverter.toPreferenciaDTO(preferencia);
    }

    public List<PreferenciaDTO> listarPreferenciasUsuario(String token){
        Usuario usuario = getUsuario(token);
        List<Preferencia> preferencias = preferenciaRepository.findByUsuario(usuario);
        return preferenciaConverter.toListPreferenciaDTO(preferencias);
    }

    public PreferenciaDTO atualizarNomePreferencia(
            String token,
            Long idPreferencia,
            String nomeAtualizado){

        Usuario usuario = getUsuario(token);
        Preferencia preferenciaAtualizar = preferenciaRepository
                .findById(idPreferencia).orElseThrow(()-> new PreferenciaNotFoundException(
                        "A preferência não foi encontrada."
                ));

        verificarUsuarioPreferencia(preferenciaAtualizar, usuario);

        // Atualiza com o novo nome
        preferenciaAtualizar.nome = nomeAtualizado;
        preferenciaRepository.save(preferenciaAtualizar);
        return preferenciaConverter.toPreferenciaDTO(preferenciaAtualizar);
    }

    /**
     Verifica se a preferência pertence ao usuário autenticado que está
     realizando a requisição.

     @param preferencia preferência que será verificada
     @param usuario usuário que está realizando a operação
     @throws PreferenciaNotFoundException se a preferência não pertencer ao usuário
     */
    public void verificarUsuarioPreferencia(Preferencia preferencia, Usuario usuario){
        if(!preferencia.getIdUsuario().equals(usuario.getId())){
            throw new PreferenciaNotFoundException("A preferência não foi encontrada ou não pode ser manipulada.");
        }
    }
}
