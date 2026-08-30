package arthurrodrigues.monitorvagas.infrastructure.scraper;

import arthurrodrigues.monitorvagas.presentation.dto.JobDTO;
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

@Service
@Slf4j
public class ThorService implements IScraper {
    @Value("${URL_THOR_VAGAS}")
    private String url;
    private final int limiteVagas = 600;

    @Override
    public List<JobDTO> buscarVagas() throws InterruptedException {
        WebDriver driver = ScraperUtils.getDriver();

        int numeroPagina = 1;
        List<JobDTO> vagasEncontradas = new ArrayList<>();

        WebElement botaoProximaPagina = null;
        try {
            driver.get(url);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            while(true) {
                botaoProximaPagina = null;
                List<WebElement> vagasCards = wait.until(
                        ExpectedConditions.presenceOfAllElementsLocatedBy(
                                By.cssSelector("div.cell-list-content")
                        )
                );

                List<WebElement> itensPaginacao = wait.until(
                        ExpectedConditions.presenceOfAllElementsLocatedBy(
                                By.cssSelector("li.page-item a")
                        )
                );

                // Busca o botão de "próximo" da paginação
                for (WebElement itemPaginacao : itensPaginacao) {
                    if (itemPaginacao.getText().startsWith("Next")) {
                        botaoProximaPagina = itemPaginacao;
                    }
                }

                log.info("Buscando vagas na página: {} do ProgramaThor", numeroPagina);
                vagasCards.forEach(vaga -> {
                    List<WebElement> informacoesVaga = vaga.findElements(By.cssSelector("div.cell-list-content-icon span"));

                    String tituloVaga = vaga.findElement(By.cssSelector("h3.text-24.line-height-30")).getText();
                    String nomeEmpresa = informacoesVaga.getFirst().getText();
                    String dataPublicacao = null;
                    String urlVaga = vaga.findElement(By.xpath("./ancestor::a")).getAttribute("href");

                    ScraperUtils.adicionarVaga(vagasEncontradas, tituloVaga, nomeEmpresa, dataPublicacao, urlVaga);
                });

                if(botaoProximaPagina != null && vagasEncontradas.size() < limiteVagas){

                    // Coloca o driver na URL da próxima página
                    String urlProximaPagina = botaoProximaPagina.getAttribute("href");
                    if(urlProximaPagina != null){
                        driver.get(urlProximaPagina);
                        numeroPagina += 1;
                    }

                } else{
                    break;
                }
            }

        } finally {
            driver.quit();
            log.info("Foram encontradas {} vagas no ProgramaThor", vagasEncontradas.size());
        }

        return vagasEncontradas;
    }
}
