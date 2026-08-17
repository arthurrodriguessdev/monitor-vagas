package arthurrodrigues.monitorvagas.business.controller;

import arthurrodrigues.monitorvagas.business.PreferenciaUsuarioService;
import arthurrodrigues.monitorvagas.business.dtos.PreferenciaUsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuario_preferencia")
public class PreferenciaUsuarioController {
    private final PreferenciaUsuarioService preferenciaUsuarioService;

    @PostMapping()
    public ResponseEntity<PreferenciaUsuarioDTO> adicionarPreferenciaUsuario(@RequestParam("email") String email){
        return ResponseEntity.ok(preferenciaUsuarioService.adicionarPreferenciaUsuario(email));
    }
}
