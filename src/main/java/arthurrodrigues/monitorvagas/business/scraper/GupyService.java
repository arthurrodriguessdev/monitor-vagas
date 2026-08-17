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
public class GupyService implements IScraper {
    @Value("${url.gupy}")
    private String url;

    @Override
    public List<JobDTO> buscarVagas() throws InterruptedException{
        WebDriver driver = ScraperUtils.getDriver();
        List<JobDTO> vagasEncontradas = new ArrayList<>();
        int quantidadeVagas = 0;

        try {
            driver.get(url);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            while(true) {
                List<WebElement> vagasCards = wait.until(
                        ExpectedConditions.presenceOfAllElementsLocatedBy(
                                By.cssSelector("div.eco-cssveg")
                        )
                );

                quantidadeVagas += vagasCards.size();
                 vagasCards.forEach(vaga -> {
                     String tituloVaga = vaga.findElement(By.cssSelector("h3.eco-2lmcva")).getText();
                     String nomeEmpresa = vaga.findElement(By.cssSelector("p.eco-t2rjet")).getText();
                     String dataPublicacao = vaga.findElement(By.cssSelector("p.eco-7ia25n")).getText();
                     String urlVaga = vaga.findElement(By.cssSelector("a.eco-p5p5uj")).getAttribute("href");

                     ScraperUtils.adicionarVaga(vagasEncontradas, tituloVaga, nomeEmpresa, dataPublicacao, urlVaga);
                 });

                WebElement proximaPagina = driver.findElement(
                        By.cssSelector("button[aria-label='Próxima página']")
                );

                if(proximaPagina.isEnabled()) {
                    log.info("Buscando na Gupy. URL: {}", driver.getCurrentUrl());
                    proximaPagina.click();
                    Thread.sleep(1000);

                } else {
                    break;
                }
            }

        } finally {
            driver.quit();
            log.info("Foram encontradas {} vagas na Gupy", quantidadeVagas);
        }

        return vagasEncontradas;
    }
}