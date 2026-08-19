package arthurrodrigues.monitorvagas.business.interfaces;

import arthurrodrigues.monitorvagas.business.dtos.JobDTO;

import java.util.List;

public interface IScraper {
    List<JobDTO> buscarVagas() throws InterruptedException;
}
