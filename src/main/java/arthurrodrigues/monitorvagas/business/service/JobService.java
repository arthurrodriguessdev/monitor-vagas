package arthurrodrigues.monitorvagas.business.service;

import arthurrodrigues.monitorvagas.presentation.dto.JobDTO;
import arthurrodrigues.monitorvagas.presentation.dto.UsuarioDTO;
import arthurrodrigues.monitorvagas.presentation.dto.VagaRecebidaDTO;
import arthurrodrigues.monitorvagas.infrastructure.scraper.GupyService;
import arthurrodrigues.monitorvagas.infrastructure.scraper.ThorService;
import arthurrodrigues.monitorvagas.infrastructure.converter.UsuarioConverter;
import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import arthurrodrigues.monitorvagas.infrastructure.entity.VagaRecebida;
import arthurrodrigues.monitorvagas.infrastructure.exception.VagaNotFoundException;
import arthurrodrigues.monitorvagas.infrastructure.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class JobService{
    private final GupyService gupyService;
    private final ThorService thorService;
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final VagaRecebidaService vagaRecebidaService;

    // @Scheduled(fixedDelay = 10000000) TO DO: descomentar depois e ajustar um horário específico para buscar
    @Transactional // Para manter a integridade dos dados
    public void buscarVagas(){
        List<JobDTO> vagasEncontradas = getAllVagas();

        List<Usuario> usuarios = usuarioRepository.findAll();
        if(usuarios.isEmpty()){
            log.info("Nenhum usuário foi encontrado");

        } else {
            usuarios.forEach(usuario -> {
                // Conversão de usuário para DTO
                UsuarioDTO usuarioDTO = usuarioConverter.toUsuarioDTO(usuario);
                Long idUsuario = usuarioDTO.id();

                List<Preferencia> preferenciasUsuario = usuarioDTO.preferencias();
                List<VagaRecebida> vagaRecebidasUsuario = usuarioDTO.vagasRecebidas(); // Lista de vagas que o usuário já foi notificado
                List<String> nomePreferencias = new ArrayList<>();

                for (Preferencia pref : preferenciasUsuario) {
                    nomePreferencias.add(formatarPreferencia(pref.nome));
                }

                List<JobDTO> vagasFinais = filtrarVagas(nomePreferencias, vagasEncontradas, vagaRecebidasUsuario, idUsuario);
                if (!vagasFinais.isEmpty()) {
                    log.info("Foram encontradas {} vagas para o usuário: {}", vagasFinais.size(), usuarioDTO.email());
                    emailService.enviarEmail(vagasFinais, usuarioDTO, vagasFinais.size());

                } else {
                    log.info("Não foram encontradas vagas para as preferências do usuário de e-email: {}", usuarioDTO.email());
                }
            });
        }
    }

    /**
     * Busca vagas nas plataformas Gupy e InfoJobs.
     * <p>
     * Caso ocorra um erro durante a busca em uma das plataformas,
     * registra o erro, mas continua a busca nas demais.
     *
     * @return uma lista contendo todas as vagas encontradas.
     * @throws VagaNotFoundException caso nenhuma vaga seja encontrada
     * em nenhuma das plataformas.
     */
    public List<JobDTO> getAllVagas(){
        List<JobDTO> vagasEncontradas = new ArrayList<>();
        try {
            vagasEncontradas.addAll(gupyService.buscarVagas());
        } catch(Exception e){
            log.error("Erro ao buscar vagas na gupyService: ", e);
        }

        try {
            vagasEncontradas.addAll(thorService.buscarVagas());
        } catch(Exception e) {
            log.error("Erro ao buscar vagas no ThorService: ", e);
        }

        if(vagasEncontradas.isEmpty()){
            throw new VagaNotFoundException("Nenhuma vaga foi encontrada.");
        }

        return vagasEncontradas;
    }

    /**
     * Filtra as vagas de acordo com as preferências do usuário e remove da lista
     * aquelas que já foram notificadas anteriormente.
     * <p>
     * As vagas que ainda não foram recebidas são registradas para evitar o envio
     * de notificações duplicadas em execuções futuras.
     *
     * @param filtros lista contendo os nomes das preferências do usuário utilizadas para filtrar as vagas.
     * @param vagas lista de todas as vagas encontradas nas plataformas.
     * @param vagasRecebidas vagas que já foram notificadas ao usuário.
     * @param idUsuario identificador do usuário que receberá as vagas.
     * @return lista contendo apenas as vagas que devem ser notificadas.
     */
    public List<JobDTO> filtrarVagas(List<String> filtros, List<JobDTO> vagas, List<VagaRecebida> vagasRecebidas, Long idUsuario){
        // Adiciona vagas compatíveis com as preferências do usuário
        List<JobDTO> vagasFinais = new ArrayList<>();
        filtros.forEach(palavra->{
            vagas.stream().filter(vaga->vaga.tituloVaga().toLowerCase().contains(palavra))
                    .forEach(vaga->{
                        if(!vagasFinais.contains(vaga)){
                            vagasFinais.add(vaga);
                        }
                    });
        });

        // Se o usuário ainda não recebeu nenhuma vaga, todas são registradas e notificadas
        if(vagasRecebidas.isEmpty()) {
            vagasFinais.forEach(vaga -> {
                VagaRecebidaDTO vagaRecebidaDTO = new VagaRecebidaDTO(
                        vaga.tituloVaga(),
                        vaga.urlVaga(),
                        idUsuario
                );

                vagaRecebidaService.registrarVagaRecebida(vagaRecebidaDTO);
            });

            return vagasFinais;
        }

        // Cria uma cópia para remover as vagas já notificadas sem alterar a lista original
        List<JobDTO> vagasNotificar = new ArrayList<>(vagasFinais);
        vagasFinais.forEach(vaga->{
            boolean vagaRecebeu = false;
            for (VagaRecebida vagaRecebida : vagasRecebidas) {
                if (vaga.urlVaga().equals(vagaRecebida.urlVaga)) {
                    vagaRecebeu = true;
                    break;
                }
            }

            if (vagaRecebeu) {
                vagasNotificar.remove(vaga);

            } else {
                VagaRecebidaDTO vagaRecebidaDTO = new VagaRecebidaDTO(vaga.tituloVaga(), vaga.urlVaga(), idUsuario);
                vagaRecebidaService.registrarVagaRecebida(vagaRecebidaDTO);
            }
        });

        return vagasNotificar;
    }

    public String formatarPreferencia(String preferencia){
        return preferencia.toLowerCase();
    }

    public List<JobDTO> pesquisarVagas(List<String> listaPreferencias){
        if(listaPreferencias == null || listaPreferencias.isEmpty()){
            throw new IllegalArgumentException("Informe pelo menos uma palavra para buscar vagas");
        }

        log.debug("Pesquisando vagas com as preferências: {}", listaPreferencias);
        return this.getAllVagas(listaPreferencias);
    }

    private List<JobDTO> getAllVagas(List<String> listaPreferencias){
        List<JobDTO> vagasEncontradas = new ArrayList<>();

        try {
            vagasEncontradas.addAll(gupyService.buscarVagas(listaPreferencias));
        } catch(Exception e){
            log.error("Erro ao pesquisar vagas na gupyService: ", e);
        }

        if(vagasEncontradas.isEmpty()){
            throw new VagaNotFoundException("Nenhuma vaga foi encontrada");
        }

        return vagasEncontradas;
    }
}