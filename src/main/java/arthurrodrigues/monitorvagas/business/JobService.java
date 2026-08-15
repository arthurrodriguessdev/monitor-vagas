package arthurrodrigues.monitorvagas.business;

import arthurrodrigues.monitorvagas.business.dtos.JobDTO;
import arthurrodrigues.monitorvagas.business.scraper.GupyService;
import arthurrodrigues.monitorvagas.business.scraper.InfoJobsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
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
            emailService.enviarEmail(vagasEncontradas, vagasEncontradas.size());
        } catch(Exception e){
            System.out.println("ERRO" + e.getMessage() + e.getCause());
        }
    }
}