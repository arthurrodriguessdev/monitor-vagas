package arthurrodrigues.monitorvagas.business;

import arthurrodrigues.monitorvagas.business.dtos.JobDTO;
import arthurrodrigues.monitorvagas.business.scraper.GupyService;
import arthurrodrigues.monitorvagas.business.scraper.InfoJobsService;
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

    @Scheduled(fixedDelay = 100000)
    public void buscarVagas(){
        try {
//            emailService.enviarEmail(List.of(
//                    new JobDTO("vaga tal", "empresa tal", "14/09/2020", "url"),
//                    new JobDTO("vaga tal", "empresa tal", "14/09/2020", "url")));
            List<JobDTO> vagasEncontradas = new ArrayList<>();
            vagasEncontradas.addAll(gupyService.buscarVagas());
            vagasEncontradas.addAll(infoJobsService.buscarVagas());

            List<String> palavras = formatarListaPreferencias(List.of("Auxiliar", "Cozinheiro"));
            List<JobDTO> vagasFinais = new ArrayList<>(filtrarVagas(palavras, vagasEncontradas));
            emailService.enviarEmail(vagasFinais, vagasFinais.size());

        } catch(Exception e){
            System.out.println("ERRO" + e.getMessage() + e.getCause());
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