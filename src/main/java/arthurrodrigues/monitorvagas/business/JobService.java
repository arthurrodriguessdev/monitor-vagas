package arthurrodrigues.monitorvagas.business;

import arthurrodrigues.monitorvagas.business.dtos.JobDTO;
import arthurrodrigues.monitorvagas.business.scraper.GupyService;
import arthurrodrigues.monitorvagas.business.scraper.InfoJobsService;
import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;
import arthurrodrigues.monitorvagas.infrastructure.entity.PreferenciaUsuario;
import arthurrodrigues.monitorvagas.infrastructure.repository.PreferenciaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
        try {
            List<JobDTO> vagasEncontradas = new ArrayList<>(getAllVagas());

            List<PreferenciaUsuario> preferencias = preferenciaUsuarioRepository.findAll();
            preferencias.forEach(preferencia->{
                List<Preferencia> preferenciasUsuario = preferencia.preferencias;
                List<String> nomePreferencias = new ArrayList<>();

                for(Preferencia pref : preferenciasUsuario) {
                    nomePreferencias.add(pref.nome.toLowerCase());
                }

                List<JobDTO> vagasFinais = new ArrayList<>(filtrarVagas(nomePreferencias, vagasEncontradas));
                emailService.enviarEmail(vagasFinais, vagasFinais.size());

            });

        } catch(Exception e){
            System.out.println("ERRO" + e.getMessage() + e.getCause());
        }
    }

    public List<JobDTO> getAllVagas(){
        try {
            List<JobDTO> vagasEncontradas = new ArrayList<>();
            vagasEncontradas.addAll(gupyService.buscarVagas());
            vagasEncontradas.addAll(infoJobsService.buscarVagas());
            return vagasEncontradas;

        } catch (Exception e){
            return Collections.emptyList();
        }
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