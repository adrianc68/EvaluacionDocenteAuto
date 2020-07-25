import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AutomaticEval automaticEval = new AutomaticEval();
        automaticEval.showStage();
        System.out.println(automaticEval.processName());
    }

}
