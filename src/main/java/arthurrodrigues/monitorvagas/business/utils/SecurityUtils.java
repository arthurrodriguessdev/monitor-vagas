package arthurrodrigues.monitorvagas.business.utils;

public class SecurityUtils {
    public static String tokenFormat(String token){
        if(token == null){ return null; }
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }

        return token;
    }
}
