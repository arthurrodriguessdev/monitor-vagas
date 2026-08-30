package arthurrodrigues.monitorvagas.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados necessários para autenticação do usuário")
public record LoginDTO(
        @Schema(description = "Endereço de e-mail cadastrado", example = "bianca@gmail.com")
        String email,

        @Schema(description = "Senha do usuário", example = "exemplosenha@123")
        String senha
) {}