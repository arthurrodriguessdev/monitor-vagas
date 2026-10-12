package arthurrodrigues.monitorvagas.infrastructure.scraper;

import arthurrodrigues.monitorvagas.presentation.dto.JobDTO;
import arthurrodrigues.monitorvagas.business.interfaces.IScraper;
import arthurrodrigues.monitorvagas.business.utils.ScraperUtils;
import arthurrodrigues.monitorvagas.infrastructure.exception.VagaNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GupyService implements IScraper {
    @Value("${url.gupy}")
    private String url;

    private void buscarVagasPorPagina(List<JobDTO> vagasEncontradas, WebDriver driver, String urlBuscar) throws InterruptedException {
        int quantidadeMaximaPaginas = 9;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        for(int i = 2; i <= quantidadeMaximaPaginas; i++){
            List<WebElement> vagasCards = null;
            vagasCards = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(
                            By.cssSelector("div.eco-cssveg")
                    )
            );

            vagasCards.forEach(vaga -> {
                String tituloVaga = vaga.findElement(By.cssSelector("h3.eco-2lmcva")).getText();
                String nomeEmpresa = vaga.findElement(By.cssSelector("p.eco-t2rjet")).getText();
                String dataPublicacao = vaga.findElement(By.cssSelector("p.eco-7ia25n")).getText();
                String urlVaga = vaga.findElement(By.cssSelector("a.eco-p5p5uj")).getAttribute("href");

                ScraperUtils.adicionarVaga(vagasEncontradas, tituloVaga, nomeEmpresa, dataPublicacao, urlVaga);
            });

            driver.get(String.format(urlBuscar, "?page=%d", i));
        }
    }

    private void buscarTodasPaginas(List<JobDTO> vagasEncontradas, String urlNavegar){
        WebDriver driver = ScraperUtils.getDriver();

        try {
            driver.get(urlNavegar);
            buscarVagasPorPagina(vagasEncontradas, driver, urlNavegar);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);

        } finally {
            driver.quit();
            log.info("Foram encontradas {} vagas na Gupy", vagasEncontradas.size());
        }
    }

    @Override
    public List<JobDTO> buscarVagas() {
        List<JobDTO> vagasEncontradas = new ArrayList<>();

        buscarTodasPaginas(vagasEncontradas, url);
        return vagasEncontradas;
    }

    public List<JobDTO> buscarVagas(List<String> listaPreferencias) {
        List<JobDTO> vagasEncontradas = new ArrayList<>();

        if (!listaPreferencias.isEmpty()) {
            for (String preferencia : listaPreferencias) {
                String urlPesquisar = url + "/" + preferencia;

                try {
                    buscarTodasPaginas(vagasEncontradas, urlPesquisar);
                } catch(TimeoutException ex){
                    log.info("Nenhuma vaga foi encontrada para a preferência: {}", preferencia);
                    continue;
                }
            }
        }

        if(vagasEncontradas.isEmpty()){
            log.info("Nenhuma vaga foi encontrada para as preferências descritas");
            throw new VagaNotFoundException("Nenhuma vaga foi encontrada");
        }

        return vagasEncontradas;
    }
}