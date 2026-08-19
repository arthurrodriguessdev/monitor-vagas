package arthurrodrigues.monitorvagas.business.controller;

import arthurrodrigues.monitorvagas.business.PreferenciaService;
import arthurrodrigues.monitorvagas.business.dtos.PreferenciaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/preferencia")
public class PreferenciaController {
    private final PreferenciaService preferenciaService;

    @PostMapping()
    public ResponseEntity<PreferenciaDTO> adicionarPreferencia(
            @RequestParam("nomePreferencia") String nome,
            @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(preferenciaService.adicionarPreferencia(nome, token));
    }
}
