package arthurrodrigues.monitorvagas.business.controller;

import arthurrodrigues.monitorvagas.business.PreferenciaService;
import arthurrodrigues.monitorvagas.business.dtos.PreferenciaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/preferencia")
public class PreferenciaController {
    private final PreferenciaService preferenciaService;

    @PostMapping()
    public ResponseEntity<PreferenciaDTO> adicionarPreferencia(
            @RequestParam("nomePreferencia") String nome,
            @RequestParam("idPreferenciaUsuario") Long id){
        return ResponseEntity.ok(preferenciaService.adicionarPreferencia(nome, id));
    }
}
