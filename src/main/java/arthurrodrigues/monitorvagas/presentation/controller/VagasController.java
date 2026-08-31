package arthurrodrigues.monitorvagas.presentation.controller;

import arthurrodrigues.monitorvagas.business.service.JobService;
import arthurrodrigues.monitorvagas.infrastructure.config.SecurityConfig;
import arthurrodrigues.monitorvagas.presentation.dto.JobDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/vagas")
@Tag(name = "Vagas", description = "Endpoints relacionados à pesquisa de vagas de emprego")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class VagasController {
    private final JobService jobService;

    @Operation(summary = "Pesquisa vagas por meio de uma lista de palavras")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Nenhuma preferência foi informada")
    @ApiResponse(responseCode = "404", description = "Nenhuma vaga encontrada")
    @PostMapping("/pesquisar")
    public ResponseEntity<List<JobDTO>> pesquisarVagas(@RequestBody(required = false) List<String> listaPreferencias,
                                                       @RequestHeader(value = "Authorization", required = false) String token){
        return ResponseEntity.ok().body(jobService.pesquisarVagas(listaPreferencias));
    }
}
