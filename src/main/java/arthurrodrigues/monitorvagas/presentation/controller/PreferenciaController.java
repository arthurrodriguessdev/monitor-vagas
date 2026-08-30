package arthurrodrigues.monitorvagas.presentation.controller;

import arthurrodrigues.monitorvagas.business.service.PreferenciaService;
import arthurrodrigues.monitorvagas.presentation.dto.PreferenciaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/preferencia")
@Tag(name = "Preferências", description = "Operações relacionadas ao gerenciamento das preferências dos usuários")
public class PreferenciaController {
    private final PreferenciaService preferenciaService;

    @PostMapping()
    @Operation(
            summary = "Cadastrar preferência",
            description = "Cadastra uma nova preferência para o usuário autenticado."
    )
    @ApiResponse(responseCode = "200", description = "Preferência cadastrada com sucesso.")
    @ApiResponse(responseCode = "400", description = "Não foi possível cadastrar a preferência.")
    @ApiResponse(responseCode = "404", description = "Usuário autenticado não encontrado")
    public ResponseEntity<PreferenciaDTO> adicionarPreferencia(
            @Parameter(description = "Nome da preferência de vaga", example = "Desenvolvedor Java")
            @RequestParam("nomePreferencia") String nome,

            @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(preferenciaService.adicionarPreferencia(nome, token));
    }
}
