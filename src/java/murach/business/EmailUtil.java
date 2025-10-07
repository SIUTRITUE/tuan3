package murach.business;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

/**
 * EmailUtil - Lớp tiện ích để gửi email sử dụng JavaMail API
 * Áp dụng kiến thức từ Chapter 14: JavaMail
 */
public class EmailUtil {

    // Sử dụng EmailConfigUtil để lấy cấu hình từ properties file
    // Chapter 14: Best practices - externalize configuration

    /**
     * Gửi email xác nhận khi user hoàn thành survey
     * @param user User object chứa thông tin người dùng
     * @throws MessagingException nếu có lỗi khi gửi email
     */
    public static void sendSurveyConfirmationEmail(User user) throws MessagingException, UnsupportedEncodingException {
        System.out.println("=== Bắt đầu gửi email ===");
        System.out.println("To: " + user.getEmail());
        
        // Bước 1: Lấy Properties cho SMTP từ config
        Properties properties = EmailConfigUtil.getSmtpProperties();
        
        System.out.println("SMTP Host: " + properties.getProperty("mail.smtp.host"));
        System.out.println("Username: " + EmailConfigUtil.getUsername());
        System.out.println("Password: " + (EmailConfigUtil.getPassword() != null ? "***" + EmailConfigUtil.getPassword().substring(Math.max(0, EmailConfigUtil.getPassword().length() - 4)) : "NULL"));

        // Bước 2: Tạo Session với Authenticator
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        EmailConfigUtil.getUsername(),
                        EmailConfigUtil.getPassword()
                );
            }
        });

        // Bật debug mode nếu được cấu hình
        session.setDebug(EmailConfigUtil.isDebugMode());

        // Bước 3: Tạo MimeMessage
        MimeMessage message = new MimeMessage(session);

        // Set From
        message.setFrom(new InternetAddress(EmailConfigUtil.getUsername(), EmailConfigUtil.getFromName()));

        // Set To
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(user.getEmail()));

        // Set Subject
        message.setSubject("Cảm ơn bạn đã tham gia khảo sát!", "UTF-8");

        // Set Content (HTML)
        String htmlContent = createHtmlEmailContent(user);
        message.setContent(htmlContent, "text/html; charset=UTF-8");

        // Bước 4: Gửi email bằng Transport.send()
        Transport.send(message);
    }

    /**
     * Tạo nội dung HTML cho email
     * @param user User object
     * @return String HTML content
     */
    private static String createHtmlEmailContent(User user) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append(".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }");
        html.append(".content { padding: 20px; background-color: #f9f9f9; }");
        html.append(".info { background-color: white; padding: 15px; margin: 10px 0; border-left: 4px solid #4CAF50; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");

        html.append("<div class='header'>");
        html.append("<h1>Cảm ơn bạn đã tham gia khảo sát!</h1>");
        html.append("</div>");

        html.append("<div class='content'>");
        html.append("<h2>Xin chào ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("!</h2>");
        html.append("<p>Chúng tôi đã nhận được thông tin khảo sát của bạn. Dưới đây là tóm tắt thông tin:</p>");

        html.append("<div class='info'>");
        html.append("<h3>Thông tin cá nhân:</h3>");
        html.append("<p><strong>Họ tên:</strong> ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("</p>");
        html.append("<p><strong>Email:</strong> ").append(user.getEmail()).append("</p>");
        html.append("<p><strong>Ngày sinh:</strong> ").append(user.getDateOfBirth()).append("</p>");
        html.append("</div>");

        html.append("<div class='info'>");
        html.append("<h3>Thông tin khảo sát:</h3>");
        html.append("<p><strong>Bạn biết đến chúng tôi qua:</strong> ").append(user.getHeardFrom()).append("</p>");
        html.append("<p><strong>Muốn nhận cập nhật:</strong> ").append(user.getWantsUpdates()).append("</p>");
        html.append("<p><strong>Đồng ý nhận email:</strong> ").append(user.getEmailOK()).append("</p>");
        html.append("<p><strong>Liên hệ qua:</strong> ").append(user.getContactVia()).append("</p>");
        html.append("</div>");

        html.append("<p>Cảm ơn bạn đã dành thời gian tham gia khảo sát của chúng tôi!</p>");
        html.append("<p>Trân trọng,<br>Đội ngũ Survey Application</p>");
        html.append("</div>");

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    /**
     * Gửi email đơn giản (text thuần)
     * @param toEmail Email người nhận
     * @param subject Tiêu đề
     * @param content Nội dung
     * @throws MessagingException
     */
    public static void sendSimpleEmail(String toEmail, String subject, String content) throws MessagingException {
        Properties properties = EmailConfigUtil.getSmtpProperties();

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        EmailConfigUtil.getUsername(),
                        EmailConfigUtil.getPassword()
                );
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EmailConfigUtil.getUsername()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject, "UTF-8");
        message.setText(content, "UTF-8");

        Transport.send(message);
    }
}