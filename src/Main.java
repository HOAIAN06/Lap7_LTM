import javafx.application.Application;
import javafx.stage.Stage;
import service.AppService;
import ui.MailAppController;
import ui.MailAppView;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        MailAppView view = new MailAppView();
        new MailAppController(new AppService(), view);

        stage.setTitle("Mail App");
        stage.setScene(view.createScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
