package service;

import java.util.List;

import db.SupabaseDB;
import models.Mail;
import network.MailProtocol;

public class AppService {
    private final SupabaseDB database = new SupabaseDB();
    private String currentUser;

    public String getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    // Đăng ký tài khoản mới.
    // Tham số: username, password.
    // Trả về: true nếu thành công.
    public boolean register(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
        }
        return database.registerUser(username.trim(), password);
    }

    // Đăng nhập và lưu phiên hiện tại.
    // Tham số: username, password.
    // Trả về: true nếu thành công.
    public boolean login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
        }
        boolean ok = database.loginUser(username.trim(), password);
        currentUser = ok ? username.trim() : null;
        return ok;
    }

    public void logout() {
        currentUser = null;
    }

    // Gửi mail qua Gmail cố định trong code, lưu lịch sử với sender = username đã đăng nhập.
    // Tham số: receiver, subject, content.
    // Trả về: true nếu gửi và lưu DB đều thành công.
    public boolean sendMail(String receiver, String subject, String content) throws Exception {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Vui lòng đăng nhập trước khi gửi mail.");
        }
        if (isBlank(receiver)) {
            throw new IllegalArgumentException("Vui lòng nhập email người nhận.");
        }

        Mail mail = new Mail(currentUser, receiver.trim(), nullToEmpty(subject), nullToEmpty(content));
        MailProtocol.sendEmailViaGmail(mail);
        return database.saveMailToDb(mail);
    }

    // Đọc hộp thư từ Database theo Gmail người nhận.
    // Tham số: receiverGmail.
    // Trả về: danh sách thư.
    public List<Mail> readMails(String receiverGmail) {
        if (isBlank(receiverGmail)) {
            throw new IllegalArgumentException("Vui lòng nhập Gmail người nhận để đọc hộp thư.");
        }
        return database.getMailsByReceiver(receiverGmail.trim());
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
