package arthurrodrigues.monitorvagas.business.controller;

import arthurrodrigues.monitorvagas.business.UsuarioService;
import arthurrodrigues.monitorvagas.business.dtos.LoginDTO;
import arthurrodrigues.monitorvagas.business.dtos.UsuarioDTO;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO){
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.senha());
        var auth = authenticationManager.authenticate(usernamePassword);

        String token = usuarioService.login((Usuario) auth.getPrincipal());
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> register(@RequestParam("nome") String nome,
                                               @RequestParam("email") String email,
                                               @RequestParam("senha") String senha){
        return ResponseEntity.ok().body(usuarioService.registrarUsuario(nome, email, senha));
    }
}
