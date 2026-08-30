package arthurrodrigues.monitorvagas.business.interfaces;

import arthurrodrigues.monitorvagas.presentation.dto.JobDTO;

import java.util.List;

public interface IScraper {
    List<JobDTO> buscarVagas() throws InterruptedException;
}
