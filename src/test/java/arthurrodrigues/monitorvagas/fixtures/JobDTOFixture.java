package arthurrodrigues.monitorvagas.fixtures;

import arthurrodrigues.monitorvagas.presentation.dto.JobDTO;

public class JobDTOFixture {
    public static JobDTO build(String tituloVaga, String nomeEmpresa, String dataPublicacao, String urlVaga){
        return new JobDTO(tituloVaga, nomeEmpresa, dataPublicacao, urlVaga);
    }

    public static JobDTO build(){
        return new JobDTO(
                "Desenvolvedor Java",
                "Google",
                "19/01/2026",
                "google.vagas.com");
    }
}