package arthurrodrigues.monitorvagas.business;

import arthurrodrigues.monitorvagas.business.dtos.JobDTO;
import arthurrodrigues.monitorvagas.business.scraper.GupyService;
import arthurrodrigues.monitorvagas.business.scraper.InfoJobsService;
import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;
import arthurrodrigues.monitorvagas.infrastructure.entity.PreferenciaUsuario;
import arthurrodrigues.monitorvagas.infrastructure.exceptions.VagaNotFoundException;
import arthurrodrigues.monitorvagas.infrastructure.repository.PreferenciaUsuarioRepository;
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
    private final PreferenciaUsuarioRepository preferenciaUsuarioRepository;

    @Scheduled(fixedDelay = 100000)
    public void buscarVagas(){
        List<JobDTO> vagasEncontradas = getAllVagas();

        List<PreferenciaUsuario> preferencias = preferenciaUsuarioRepository.findAllWithPreferences();
        preferencias.forEach(preferencia->{
            List<Preferencia> preferenciasUsuario = preferencia.preferencias;
            List<String> nomePreferencias = new ArrayList<>();

            for(Preferencia pref : preferenciasUsuario) {
                nomePreferencias.add(formatarPreferencia(pref.nome));
            }

            List<JobDTO> vagasFinais = filtrarVagas(nomePreferencias, vagasEncontradas);
            emailService.enviarEmail(vagasFinais, vagasFinais.size());
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

    public List<JobDTO> filtrarVagas(List<String> filtros, List<JobDTO> vagas){
        List<JobDTO> vagasFinais = new ArrayList<>();
        filtros.forEach(palavra->{
            vagasFinais.addAll(vagas.stream()
                    .filter(vaga->vaga.tituloVaga().toLowerCase().contains(palavra)).toList());
        });

        return vagasFinais;
    }

    public String formatarPreferencia(String preferencia){
        return preferencia.toLowerCase();
    }

    public List<String> formatarListaPreferencias(List<String> preferencias){
        return preferencias.stream().map(this::formatarPreferencia).toList();
    }
}