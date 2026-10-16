package ui;

import java.util.List;
import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import models.Mail;

public class MailAppView {
    private final Label sessionLabel = new Label("Chưa đăng nhập");
    private final TextArea inboxArea = new TextArea();

    private Consumer<AuthForm> onRegister;
    private Consumer<AuthForm> onLogin;
    private Runnable onLogout;
    private Consumer<SendForm> onSend;
    private Consumer<ReadForm> onRead;

    public Scene createScene() {
        TabPane tabs = new TabPane(
                new Tab("Tài khoản", buildAuthPane()),
                new Tab("Gửi mail", buildSendPane()),
                new Tab("Đọc mail", buildReadPane())
        );
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        VBox root = new VBox(12, sessionLabel, tabs);
        root.setPadding(new Insets(16));
        VBox.setVgrow(tabs, Priority.ALWAYS);
        return new Scene(root, 720, 520);
    }

    public void setOnRegister(Consumer<AuthForm> handler) {
        this.onRegister = handler;
    }

    public void setOnLogin(Consumer<AuthForm> handler) {
        this.onLogin = handler;
    }

    public void setOnLogout(Runnable handler) {
        this.onLogout = handler;
    }

    public void setOnSend(Consumer<SendForm> handler) {
        this.onSend = handler;
    }

    public void setOnRead(Consumer<ReadForm> handler) {
        this.onRead = handler;
    }

    public void showSession(String username) {
        if (username == null || username.isBlank()) {
            sessionLabel.setText("Chưa đăng nhập");
        } else {
            sessionLabel.setText("Đang đăng nhập: " + username);
        }
    }

    public void showInbox(List<Mail> mails) {
        if (mails == null || mails.isEmpty()) {
            inboxArea.setText("Hộp thư đến trống.");
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mails.size(); i++) {
            Mail mail = mails.get(i);
            builder.append("--- Email ").append(i + 1).append(" ---\n")
                    .append("Gmail người gửi: ").append(mail.sender).append('\n')
                    .append("Gmail người nhận: ").append(mail.receiver).append('\n')
                    .append("Tiêu đề: ").append(mail.subject).append('\n')
                    .append("Nội dung:\n").append(mail.content).append("\n\n");
        }
        inboxArea.setText(builder.toString());
    }

    public void showInfo(String message) {
        showAlert(Alert.AlertType.INFORMATION, message);
    }

    public void showError(String message) {
        showAlert(Alert.AlertType.ERROR, message);
    }

    private VBox buildAuthPane() {
        TextField usernameField = new TextField();
        TextField passwordField = new TextField();

        Button registerButton = new Button("Đăng ký");
        Button loginButton = new Button("Đăng nhập");
        Button logoutButton = new Button("Đăng xuất");

        registerButton.setOnAction(e -> {
            if (onRegister != null) {
                onRegister.accept(new AuthForm(usernameField.getText(), passwordField.getText()));
            }
        });
        loginButton.setOnAction(e -> {
            if (onLogin != null) {
                onLogin.accept(new AuthForm(usernameField.getText(), passwordField.getText()));
            }
        });
        logoutButton.setOnAction(e -> {
            if (onLogout != null) {
                onLogout.run();
            }
        });

        GridPane form = formGrid();
        form.add(new Label("Tên đăng nhập"), 0, 0);
        form.add(usernameField, 1, 0);
        form.add(new Label("Mật khẩu"), 0, 1);
        form.add(passwordField, 1, 1);

        HBox actions = new HBox(10, registerButton, loginButton, logoutButton);
        actions.setAlignment(Pos.CENTER_LEFT);

        return new VBox(12, form, actions);
    }

    private VBox buildSendPane() {
        TextField senderField = new TextField();
        TextField receiverField = new TextField();
        TextField subjectField = new TextField();
        TextArea contentArea = new TextArea();
        contentArea.setPrefRowCount(8);

        Button sendButton = new Button("Gửi mail");
        sendButton.setOnAction(e -> {
            if (onSend != null) {
                onSend.accept(new SendForm(
                        senderField.getText(),
                        receiverField.getText(),
                        subjectField.getText(),
                        contentArea.getText()
                ));
            }
        });

        GridPane form = formGrid();
        form.add(new Label("Gmail người gửi"), 0, 0);
        form.add(senderField, 1, 0);
        form.add(new Label("Email người nhận"), 0, 1);
        form.add(receiverField, 1, 1);
        form.add(new Label("Tiêu đề"), 0, 2);
        form.add(subjectField, 1, 2);
        form.add(new Label("Nội dung"), 0, 3);
        form.add(contentArea, 1, 3);
        GridPane.setVgrow(contentArea, Priority.ALWAYS);

        return new VBox(12, form, sendButton);
    }

    private VBox buildReadPane() {
        TextField receiverGmailField = new TextField();
        Button readButton = new Button("Đọc hộp thư");
        inboxArea.setEditable(false);
        inboxArea.setWrapText(true);
        VBox.setVgrow(inboxArea, Priority.ALWAYS);

        readButton.setOnAction(e -> {
            if (onRead != null) {
                onRead.accept(new ReadForm(receiverGmailField.getText()));
            }
        });

        GridPane form = formGrid();
        form.add(new Label("Gmail người nhận"), 0, 0);
        form.add(receiverGmailField, 1, 0);

        return new VBox(12, form, readButton, inboxArea);
    }

    private GridPane formGrid() {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(4));
        return form;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class AuthForm {
        public final String username;
        public final String password;

        public AuthForm(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public static class SendForm {
        public final String sender;
        public final String receiver;
        public final String subject;
        public final String content;

        public SendForm(String sender, String receiver, String subject, String content) {
            this.sender = sender;
            this.receiver = receiver;
            this.subject = subject;
            this.content = content;
        }
    }

    public static class ReadForm {
        public final String receiverGmail;

        public ReadForm(String receiverGmail) {
            this.receiverGmail = receiverGmail;
        }
    }
}
