package rw.tajyire.api.service;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {
  private static final String TAG = "SendGirdEmailService";
  private final SendGrid sendGridClient;

  @Autowired
  public MailService(SendGrid sendGridClient) {
    this.sendGridClient = sendGridClient;
  }

  public boolean sendText(String fromEmail, String fromName, String to, String subject, String body) throws IOException {
    return sendEmail(fromEmail, fromName, to, subject, new Content("text/plain", body));
  }

  public boolean sendHTML(String fromEmail, String fromName, String to, String subject, String body) throws IOException {
    return sendEmail(fromEmail, fromName, to, subject, new Content("text/html", body));
  }

  private boolean sendEmail(String fromEmail, String fromName, String to, String subject, Content content)
      throws IOException {
    Mail mail = new Mail(new Email(fromEmail, fromName), subject, new Email(to), content);
    Request request = new Request();
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    request.setBody(mail.build());
    this.sendGridClient.api(request);
    return true;
  }
}
