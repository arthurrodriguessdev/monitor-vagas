package arthurrodrigues.monitorvagas.business;

import arthurrodrigues.monitorvagas.business.dtos.JobDTO;
import arthurrodrigues.monitorvagas.business.scraper.GupyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JobService{
    private final GupyService gupyService;
    private final EmailService emailService;

    @Scheduled(fixedDelay = 100)
    public void buscarVagas(){
        try {
            List<JobDTO> vagasEncontradas = gupyService.buscarVagas();
            emailService.enviarEmail(vagasEncontradas);
        } catch(Exception e){
            System.out.println("ERRO" + e.getMessage() + e.getCause());
        }
    }
}