package arthurrodrigues.monitorvagas.fixtures.dto;

import arthurrodrigues.monitorvagas.presentation.dto.login.LoginDTO;

public class LoginDTOFixture {
    public static LoginDTO build(String email, String senha){
        return new LoginDTO(email, senha);
    }

    public static LoginDTO build(){
        return new LoginDTO("emailteste@gmail.com", "senha@teste");
    }
}