package arthurrodrigues.monitorvagas.business.utils;

import arthurrodrigues.monitorvagas.business.dtos.JobDTO;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class ScraperUtils {
    public static WebDriver getDriver(){
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        return new ChromeDriver(options);
    }

    public static void adicionarVaga(
            List<JobDTO> vagas,
            String tituloVaga,
            String nomeEmpresa,
            String dataPublicacao,
            String urlVaga){
        vagas.add(new JobDTO(tituloVaga, nomeEmpresa, dataPublicacao, urlVaga));
    }
}