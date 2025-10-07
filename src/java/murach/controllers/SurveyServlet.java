package murach.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.mail.MessagingException;

import murach.business.User;
import murach.business.EmailUtil;

public class SurveyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // get parameters from the request
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String dateOfBirth = request.getParameter("dateOfBirth");   // thêm ngày sinh
        String heardFrom = request.getParameter("heardFrom");
        String wantsUpdates = request.getParameter("wantsUpdates");
        String emailOK = request.getParameter("emailOK");           // thêm emailOK
        String contactVia = request.getParameter("contactVia");

        // process parameters
        if (heardFrom == null) {
            heardFrom = "NA";
        }
        if (wantsUpdates == null) {
            wantsUpdates = "No";
        } else {
            wantsUpdates = "Yes";
        }
        if (emailOK == null) {
            emailOK = "No";
        } else {
            emailOK = "Yes";
        }

        // store data in User object
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setDateOfBirth(dateOfBirth);   // cần thêm field trong User.java
        user.setHeardFrom(heardFrom);
        user.setWantsUpdates(wantsUpdates);
        user.setEmailOK(emailOK);           // cần thêm field trong User.java
        user.setContactVia(contactVia);

        // store User object in request
        request.setAttribute("user", user);

        // Chapter 14: Gửi email xác nhận sau khi hoàn thành survey
        boolean emailSent = false;
        String emailMessage = "";
        
        // Gửi email cho tất cả user có email hợp lệ (bỏ điều kiện emailOK)
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
            try {
                EmailUtil.sendSurveyConfirmationEmail(user);
                emailSent = true;
                emailMessage = "Email xác nhận đã được gửi đến " + user.getEmail();
            } catch (MessagingException | java.io.UnsupportedEncodingException e) {
                emailSent = false;
                emailMessage = "Có lỗi khi gửi email: " + e.getMessage();
                // Log lỗi (trong thực tế nên dùng logging framework)
                System.err.println("Email sending failed: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            emailMessage = "Email không được gửi (email không hợp lệ hoặc trống)";
        }
        
        // Thêm thông tin về việc gửi email vào request
        request.setAttribute("emailSent", emailSent);
        request.setAttribute("emailMessage", emailMessage);

        // forward request to JSP
        String url = "/survey.jsp";
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
