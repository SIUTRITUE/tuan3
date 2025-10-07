package murach.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * EmailConfigUtil - Tiện ích để đọc cấu hình email từ properties file
 * Chapter 14: Best practices cho JavaMail configuration
 */
public class EmailConfigUtil {

    private static Properties emailProperties;

    static {
        loadEmailProperties();
    }

    /**
     * Load email properties từ file email.properties
     */
    private static void loadEmailProperties() {
        emailProperties = new Properties();
        try (InputStream input = EmailConfigUtil.class.getClassLoader()
                .getResourceAsStream("email.properties")) {

            if (input != null) {
                emailProperties.load(input);
                System.out.println("✅ Đã load email.properties thành công!");
                System.out.println("Username: " + emailProperties.getProperty("mail.username"));
                System.out.println("Password length: " + (emailProperties.getProperty("mail.password") != null ? emailProperties.getProperty("mail.password").length() : "NULL"));
            } else {
                System.err.println("❌ Không tìm thấy file email.properties trong classpath");
                // Set default values
                setDefaultProperties();
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file email.properties: " + e.getMessage());
            setDefaultProperties();
        }
    }

    /**
     * Set các giá trị mặc định nếu không đọc được file properties
     */
    private static void setDefaultProperties() {
        emailProperties.setProperty("mail.smtp.host", "smtp.gmail.com");
        emailProperties.setProperty("mail.smtp.port", "587");
        emailProperties.setProperty("mail.smtp.auth", "true");
        emailProperties.setProperty("mail.smtp.starttls.enable", "true");
        emailProperties.setProperty("mail.username", "your-email@gmail.com");
        emailProperties.setProperty("mail.password", "your-app-password");
        emailProperties.setProperty("mail.from.name", "Survey Application");
        emailProperties.setProperty("mail.debug", "false");
    }

    /**
     * Lấy giá trị property theo key
     */
    public static String getProperty(String key) {
        return emailProperties.getProperty(key);
    }

    /**
     * Lấy SMTP Properties để tạo Session
     */
    public static Properties getSmtpProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", getProperty("mail.smtp.port"));
        props.put("mail.smtp.auth", getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", getProperty("mail.smtp.starttls.enable"));
        props.put("mail.smtp.ssl.trust", getProperty("mail.smtp.host"));
        return props;
    }

    /**
     * Lấy username từ Environment Variables hoặc config file
     */
    public static String getUsername() {
        String envUsername = System.getenv("GMAIL_USERNAME");
        return envUsername != null ? envUsername : getProperty("mail.username");
    }
    
    /**
     * Lấy password từ Environment Variables hoặc config file
     */
    public static String getPassword() {
        String envPassword = System.getenv("GMAIL_PASSWORD");
        return envPassword != null ? envPassword : getProperty("mail.password");
    }    /**
     * Lấy tên người gửi
     */
    public static String getFromName() {
        return getProperty("mail.from.name");
    }

    /**
     * Check debug mode
     */
    public static boolean isDebugMode() {
        return "true".equalsIgnoreCase(getProperty("mail.debug"));
    }
}