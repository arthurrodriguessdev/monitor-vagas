package arthurrodrigues.monitorvagas.business.scraper;

import arthurrodrigues.monitorvagas.business.dtos.JobDTO;
import arthurrodrigues.monitorvagas.business.interfaces.IScraper;
import arthurrodrigues.monitorvagas.business.utils.ScraperUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
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
public class InfoJobsService implements IScraper {
    @Value("${url.infojobs}")
    private String url;
    private final int limiteVagasBuscar = 500;

    @Override
    public List<JobDTO> buscarVagas() throws InterruptedException {
        WebDriver driver = ScraperUtils.getDriver();
        List<JobDTO> vagasEncontradas = new ArrayList<>();
        int quantidadeVagas = 0;

        try {
            driver.get(url);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            while(quantidadeVagas < limiteVagasBuscar) {
                List<WebElement> vagasCards = wait.until(
                        ExpectedConditions.presenceOfAllElementsLocatedBy(
                                By.cssSelector("div.pt-24.px-24.cursor-pointer.js_vacancyLoad.js_rowCard.js_cardLink")
                        )
                );

                quantidadeVagas += vagasCards.size();
                vagasCards.forEach(vaga -> {
                    String tituloVaga = vaga.findElement(By.cssSelector("h2.js_vacancyTitle")).getText();
                    String nomeEmpresa = vaga.findElement(By.cssSelector("span.text-nowrap")).getText();
                    String dataPublicacao = null;
                    String urlVaga = vaga.findElement(By.cssSelector("a.text-decoration-none")).getAttribute("href");

                    ScraperUtils.adicionarVaga(vagasEncontradas, tituloVaga, nomeEmpresa, dataPublicacao, urlVaga);
                });
            }

        } finally {
            driver.quit();
            log.info("Foram encontradas {} vagas no InfoJobs", quantidadeVagas);
        }

        return vagasEncontradas;
    }
}