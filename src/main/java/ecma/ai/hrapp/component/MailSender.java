package ecma.ai.hrapp.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailSender {

    @Autowired
    JavaMailSender mailSender;

    public boolean send(String to, String text) throws MessagingException {

        String from = "pdp@gmail.com";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setSubject("Information");
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setText(text);
        mailSender.send(mimeMessage);
        return true;
    }


    public boolean mailTextAddStaff(String email, String code, String pass) throws MessagingException {
        String link = "http://localhost:80/api/user/verifyEmail?email=" + email + "&code" + code;

        String text = "<a href=\"" + link + "\">Emailni tasdiqlash</a>\n" +
                "<br>\n" +
                "<p>Parolingiz: " + pass + "</p>";

        return send(email, text);
    }

    public boolean mailsendTAsk(String email, String code, String pass) throws MessagingException {
        String link = "http://localhost:80/api/user/verifyEmail?email=" + email + "&code" + code;

        String text = "<a href=\"" + link + "\">Emailni tasdiqlash</a>\n" +
                "<br>\n" +
                "<p>Parolingiz: " + pass + "</p>";

        return send(email, text);
    }


}
