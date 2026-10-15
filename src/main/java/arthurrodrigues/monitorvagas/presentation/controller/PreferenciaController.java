package arthurrodrigues.monitorvagas.presentation.controller;

import arthurrodrigues.monitorvagas.business.service.PreferenciaService;
import arthurrodrigues.monitorvagas.infrastructure.config.SecurityConfig;
import arthurrodrigues.monitorvagas.presentation.dto.PreferenciaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/preferencia")
@Tag(name = "Preferências", description = "Operações relacionadas ao gerenciamento das preferências dos usuários")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
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

            @RequestHeader(value = "Authorization", required = false) String token){
        return ResponseEntity.ok(preferenciaService.adicionarPreferencia(nome, token));
    }

    @GetMapping()
    @Operation(
            summary = "Listagem de todas as preferências de um usuário",
            description = "Lista as preferências cadastradas por um usuário."
    )
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso.")
    public ResponseEntity<List<PreferenciaDTO>> listarPreferenciasUsuario(
            @RequestHeader(value = "Authorization", required = false) String token){
        return ResponseEntity.ok().body(preferenciaService.listarPreferenciasUsuario(token));
    }

    @PatchMapping("/{idPreferencia}")
    @Operation(
            summary = "Atualização do nome da preferência",
            description = "Atualiza o nome de uma preferência do usuário"
    )
    @ApiResponse(responseCode = "200", description = "Preferência atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Preferência não encontrada")
    public ResponseEntity<PreferenciaDTO> atualizarNomePreferencia(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable Long idPreferencia,
            @RequestParam(name = "nomeAtualizado", required = true) String nomeAtualizado
    ){
        return ResponseEntity.ok().body(preferenciaService.atualizarNomePreferencia(
                token,
                idPreferencia,
                nomeAtualizado
        ));
    }

    @DeleteMapping("/{idPreferencia}")
    @Operation(
            summary = "Deleta uma preferência",
            description = "Deleta uma preferência do usuário pelo ID"
    )
    @ApiResponse(responseCode = "204", description = "Preferência excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Preferência não encontrada ou não pode ser manipulada")
    public ResponseEntity<Void> deletarPreferencia(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long idPreferencia){
        preferenciaService.deletarPreferencia(idPreferencia, token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
