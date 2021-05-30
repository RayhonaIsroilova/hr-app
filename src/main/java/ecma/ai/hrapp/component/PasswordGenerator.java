package ecma.ai.hrapp.component;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGenerator {

    public String genRandomPassword(int len) {

        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();


        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());//
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }
}
