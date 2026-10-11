package arthurrodrigues.monitorvagas.presentation.controller;

import arthurrodrigues.monitorvagas.business.service.UsuarioService;
import arthurrodrigues.monitorvagas.presentation.dto.login.CadastroUsuarioDTO;
import arthurrodrigues.monitorvagas.presentation.dto.login.LoginDTO;
import arthurrodrigues.monitorvagas.presentation.dto.UsuarioDTO;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import arthurrodrigues.monitorvagas.presentation.dto.login.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Operações relacionadas à autenticação e ao cadastro de usuários")
public class AuthenticationController {
    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @Operation(
            summary = "Autenticar usuário",
            description = "Autentica o usuário utilizando suas credenciais e retorna um token de acesso."
    )
    @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso.")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO){
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.senha());
        var auth = authenticationManager.authenticate(usernamePassword);

        TokenDTO tokenDTO = usuarioService.login((Usuario) auth.getPrincipal());
        return ResponseEntity.ok().body(tokenDTO);
    }

    @PostMapping("/register")
    @Operation(summary = "Cadastrar usuário", description = "Realiza o cadastro de um novo usuário na aplicação.")
    @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados de cadastro inválidos ou e-mail já cadastrado.")
    public ResponseEntity<UsuarioDTO> register(@Valid @RequestBody CadastroUsuarioDTO cadastroUsuarioDTO){
        return ResponseEntity.ok().body(usuarioService.registrarUsuario(
                cadastroUsuarioDTO.nome(),
                cadastroUsuarioDTO.email(),
                cadastroUsuarioDTO.senha()
        ));
    }
}