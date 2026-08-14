package arthurrodrigues.monitorvagas.business.scraper;

import arthurrodrigues.monitorvagas.business.dtos.JobDTO;
import arthurrodrigues.monitorvagas.business.interfaces.IScraper;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GupyService implements IScraper {
    private final String url = "https://portal.gupy.io/vagas";

    @Override
    public List<JobDTO> buscarVagas() throws InterruptedException{
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);

        List<JobDTO> vagasEncontradas = new ArrayList<>();
        try {

            driver.get(url);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

//            String[] palavrasChaves = {"pedreiro", "operador", "instalador"};


            int quantidade = 0;
            while(true) {
                List<WebElement> vagasCards = wait.until(
                        ExpectedConditions.presenceOfAllElementsLocatedBy(
                                By.cssSelector("div.eco-cssveg")
                        )
                );

                quantidade += vagasCards.size();
                System.out.println("Quantidadeeee: " + quantidade);

                 vagasCards.forEach(vaga -> {
                     String tituloVaga = vaga.findElement(By.cssSelector("h3.eco-2lmcva")).getText();
                     String nomeEmpresa = vaga.findElement(By.cssSelector("p.eco-t2rjet")).getText();
                     String dataPublicacao = vaga.findElement(By.cssSelector("p.eco-7ia25n")).getText();
                     String urlVaga = vaga.findElement(By.cssSelector("a.eco-p5p5uj")).getAttribute("href");

                     vagasEncontradas.add(new JobDTO(tituloVaga, nomeEmpresa, dataPublicacao, urlVaga));
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
        }

        return vagasEncontradas;
    }
}