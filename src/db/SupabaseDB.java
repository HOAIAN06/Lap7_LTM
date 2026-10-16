package db;

import models.Mail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupabaseDB {
    private static final String URL =
            "jdbc:postgresql://db.yqoasqcynanjhbtgpfuq.supabase.co:5432/postgres?sslmode=require";
    private static final String USER = "postgres";
    private static final String PASSWORD = "xjQTiDiHERZdgH9g";

    // Mở kết nối JDBC tới PostgreSQL trên Supabase.
    // Tham số: không có.
    // Trả về: đối tượng Connection nếu kết nối thành công, null nếu xảy ra lỗi.
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Đăng ký một người dùng mới trong bảng users.
    // Tham số: username - tên đăng nhập; password - mật khẩu dạng văn bản thường.
    // Trả về: true nếu thêm đúng một người dùng, false nếu lỗi hoặc tên đăng nhập đã tồn tại.
    public boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = getConnection()) {
            if (connection == null) {
                return false;
            }

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                return statement.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra thông tin đăng nhập trong bảng users.
    // Tham số: username - tên đăng nhập; password - mật khẩu dạng văn bản thường.
    // Trả về: true nếu username và password khớp một bản ghi, false nếu không khớp hoặc có lỗi.
    public boolean loginUser(String username, String password) {
        String sql = "SELECT 1 FROM users WHERE username = ? AND password = ?";

        try (Connection connection = getConnection()) {
            if (connection == null) {
                return false;
            }

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);

                try (ResultSet result = statement.executeQuery()) {
                    return result.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lưu lịch sử một thư vào bảng mails.
    // Tham số: mail - thư chứa người gửi, người nhận, tiêu đề và nội dung.
    // Trả về: true nếu thêm đúng một thư, false nếu đối tượng mail không hợp lệ hoặc có lỗi.
    public boolean saveMailToDb(Mail mail) {
        if (mail == null) {
            return false;
        }

        String sql = "INSERT INTO mails (sender, receiver, subject, content) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            if (connection == null) {
                return false;
            }

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, mail.sender);
                statement.setString(2, mail.receiver);
                statement.setString(3, mail.subject);
                statement.setString(4, mail.content);
                return statement.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
