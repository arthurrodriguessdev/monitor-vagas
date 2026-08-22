package arthurrodrigues.monitorvagas.business;

import arthurrodrigues.monitorvagas.business.dtos.JobDTO;
import arthurrodrigues.monitorvagas.business.dtos.UsuarioDTO;
import arthurrodrigues.monitorvagas.business.dtos.VagaRecebidaDTO;
import arthurrodrigues.monitorvagas.business.scraper.GupyService;
import arthurrodrigues.monitorvagas.business.scraper.InfoJobsService;
import arthurrodrigues.monitorvagas.infrastructure.converter.UsuarioConverter;
import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import arthurrodrigues.monitorvagas.infrastructure.entity.VagaRecebida;
import arthurrodrigues.monitorvagas.infrastructure.exceptions.VagaNotFoundException;
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
    private final InfoJobsService infoJobsService;
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final VagaRecebidaService vagaRecebidaService;

    @Scheduled(fixedDelay = 100000)
    @Transactional
    public void buscarVagas(){
        List<JobDTO> vagasEncontradas = getAllVagas();

        List<Usuario> usuarios = usuarioRepository.findAll();
        usuarios.forEach(usuario->{
            // Conversão de usuário para DTO
            UsuarioDTO usuarioDTO = usuarioConverter.toUsuarioDTO(usuario);
            Long idUsuario = usuarioDTO.id();

            List<Preferencia> preferenciasUsuario = usuarioDTO.preferencias();
            List<VagaRecebida> vagaRecebidasUsuario = usuarioDTO.vagasRecebidas(); // Lista de vagas que o usuário já foi notificado
            List<String> nomePreferencias = new ArrayList<>();

            for(Preferencia pref : preferenciasUsuario) {
                nomePreferencias.add(formatarPreferencia(pref.nome));
            }

            List<JobDTO> vagasFinais = filtrarVagas(nomePreferencias, vagasEncontradas, vagaRecebidasUsuario, idUsuario);
            if(!vagasFinais.isEmpty()) {
                log.info("Foram encontradas {} vagas para o usuário: {}", vagasFinais.size(), usuarioDTO.email());
                emailService.enviarEmail(vagasFinais, usuarioDTO, vagasFinais.size());

            } else{
                log.info("Não foram encontradas vagas para as preferências do usuário de e-email: {}", usuarioDTO.email());
            }
        });
    }

    public List<JobDTO> getAllVagas(){
        List<JobDTO> vagasEncontradas = new ArrayList<>();
        try {
            vagasEncontradas.addAll(gupyService.buscarVagas());
        } catch(Exception e){
            log.error("Erro ao buscar vagas na gupyService: " + e);
        }

        try {
            vagasEncontradas.addAll(infoJobsService.buscarVagas());
        } catch(Exception e) {
            log.error("Erro ao buscar vagas no infoJobsService: " + e);
        }

        if(vagasEncontradas.isEmpty()){
            throw new VagaNotFoundException("Nenhuma vaga foi encontrada.");
        }

        return vagasEncontradas;
    }

    public List<JobDTO> filtrarVagas(List<String> filtros, List<JobDTO> vagas, List<VagaRecebida> vagasRecebidas, Long idUsuario){
        List<JobDTO> vagasFinais = new ArrayList<>();
        filtros.forEach(palavra->{
            vagas.stream().filter(vaga->vaga.tituloVaga().toLowerCase().contains(palavra))
                    .forEach(vaga->{
                        if(!vagasFinais.contains(vaga)){
                            vagasFinais.add(vaga);
                        }
                    });
        });

        // Percorre as vagas finais, retirando as tarefas que já foram notificadas
        List<JobDTO> vagasNotificar = new ArrayList<>(vagasFinais);
        vagasFinais.forEach(vaga->{
            if(vagasRecebidas.isEmpty()){
                vagasFinais.forEach(vagaFinal->{
                    VagaRecebidaDTO vagaRecebidaDTO = new VagaRecebidaDTO(vagaFinal.tituloVaga(), vagaFinal.urlVaga(), idUsuario);
                    vagaRecebidaService.registrarVagaRecebida(vagaRecebidaDTO);
                });

            } else {

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
            }
        });

        return vagasNotificar;
    }

    public String formatarPreferencia(String preferencia){
        return preferencia.toLowerCase();
    }
}