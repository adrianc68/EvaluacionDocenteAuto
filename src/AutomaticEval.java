import alert.OperationAlert;
import evaluation.AnswerElement;
import evaluation.QuestionElement;
import exceptions.BrowserErrorException;
import exceptions.BrowserNotSelectedException;
import exceptions.ElementNotFoundException;
import exceptions.EmptyPasswordFieldException;
import exceptions.EmptyUsernameFieldException;
import exceptions.ErrorKillProcessException;
import exceptions.IncorrectUserException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.openqa.selenium.remote.BrowserType;
import util.AutomaticBrowser;
import util.Serializer;
import util.SystemControl;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutomaticEval implements Initializable {
//    private HashMap<String, List<AnswerElement>> selectedAnswerHashMap;
    private List<QuestionElement> questionsPreconfiguredList;
    private String selectedBrowser;
    private Tab tab;
    private Stage stage;
    private HashMap<String, List<AnswerElement>> selectedAnswerHashMap;
    @FXML private TextField userTextField;
    @FXML private ToggleGroup toggleGroup;
    @FXML private ToggleButton safariButton;
    @FXML private ToggleButton chromeButton;
    @FXML private ToggleButton firefoxButton;
    @FXML private PasswordField passwordPasswordField;
    @FXML private Button evaluateButton;
    @FXML private Button configureQuestionsAndAnswerButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureToggleButton();
        getAnswersPreconfiguredFromFile();
        getQuestionnariePreconfiguredFromFile();
    }

    public AutomaticEval() {
        getStage();
    }

    public Tab getTab() {
        getStage();
        return tab;
    }

    public void getStage() {
        FXMLLoader loader = new FXMLLoader( getClass().getResource("/fxml/AutoViste.fxml") );
        loader.setController(this);
        try {
            AnchorPane autoPane = loader.load();
            tab = new Tab("Automatización");
            tab.setContent(autoPane);
            Parent root = autoPane;
            stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            evaluateButton.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        stage.showAndWait();
    }

    public void updateQuestionnaire() {
        updateQuestions();
    }

    @FXML
    void chromeButtonPressed(ActionEvent event) {
        chromeButton.setSelected(true);
        selectedBrowser = BrowserType.CHROME;
    }

    @FXML
    void firefoxButtonPressed(ActionEvent event) {
        firefoxButton.setSelected(true);
        selectedBrowser = BrowserType.FIREFOX;

    }

    @FXML
    void safariButtonPressed(ActionEvent event) {
        safariButton.setSelected(true);
        selectedBrowser = BrowserType.SAFARI;
    }

    @FXML
    void configureAnswersButtonPressed(ActionEvent event) {
        ConfigureController configureController = new ConfigureController();
        configureController.setAutomaticEval(this);
        configureController.setQuestionsPreconfiguredList( questionsPreconfiguredList );
        configureController.setSelectedAnswerHashMap(selectedAnswerHashMap);
        configureController.getStage();
        selectedAnswerHashMap = configureController.getSelectedAnswerHashMap();
    }

    @FXML
    void evaluateProfessorButtonPressed(ActionEvent event) {
//        if( selectedAnswerHashMap != null && selectedBrowser != null) {
//            try {
//                evaluateProfessorInBrowser();
//            } catch (IncorrectUserException e) {
//                String title = "¡Error en el inicio de sesión!";
//                String content = e.getLocalizedMessage();
//                OperationAlert.showErrorAlert(title, content);
//                Logger.getLogger( AutomaticEval.class.getName() ).log(Level.WARNING, null, e);
//            } catch (BrowserErrorException e) {
//                String title = "¡Error al iniciar el navegador!";
//                String content = e.getCause().getMessage();
//                OperationAlert.showErrorAlert(title, content);
//            } catch (ErrorKillProcessException e) {
//                String title = e.getLocalizedMessage();
//                String content = "Debes matar el proceso manualmente.";
//                OperationAlert.showErrorAlert(title, content);
//            } catch (ElementNotFoundException e) {
//                String title = "¡Error al evaluar!";
//                String content = "Al parecer no has seleccionado un profesor.";
//                OperationAlert.showErrorAlert(title, content);
//            }
//        }
    }

    private void updateQuestions() {
        try {
            verifyInputDataAndSelection();
            getQuestionnarieFromBrowser();
        } catch (EmptyUsernameFieldException e) {
            String title = "¡Verifica tus datos de usuario!";
            String content = "No has llenado el campo para la cuenta de usuario.";
            Logger.getLogger( AutomaticEval.class.getName() ).log(Level.FINE, null, e);
            OperationAlert.showErrorAlert(title, content);
        } catch (EmptyPasswordFieldException e) {
            String title = "¡Verifica tus datos de usuario!";
            String content = "No has llenado el campo para la contraseña de la cuenta de usuario.";
            OperationAlert.showErrorAlert(title, content);
            Logger.getLogger( AutomaticEval.class.getName() ).log(Level.FINE, null, e);
        } catch (BrowserNotSelectedException e) {
            String title = "¡No has seleccionado un navegador!";
            String content = "Es necesario que selecciones un navegador para comenzar el proceso.";
            OperationAlert.showErrorAlert(title, content);
            Logger.getLogger( AutomaticEval.class.getName() ).log(Level.FINE, null, e);
        } catch (ElementNotFoundException e) {
            String title = "¡No se ha encontrado un elemento!";
            String content = e.getLocalizedMessage();
            OperationAlert.showErrorAlert(title, content);
            Logger.getLogger( AutomaticEval.class.getName() ).log(Level.WARNING, null, e);
        } catch (IncorrectUserException e) {
            String title = "¡Error en el inicio de sesión!";
            String content = e.getLocalizedMessage();
            OperationAlert.showErrorAlert(title, content);
            Logger.getLogger( AutomaticEval.class.getName() ).log(Level.WARNING, null, e);
        } catch (BrowserErrorException e) {
            String title = "¡Error al iniciar el navegador!";
            String content = e.getCause().getMessage();
            OperationAlert.showErrorAlert(title, content);
        } catch (ErrorKillProcessException e) {
            String title = e.getLocalizedMessage();
            String content = "Debes matar el proceso manualmente";
            OperationAlert.showErrorAlert(title, content);
        }
    }

    private void getQuestionnarieFromBrowser() throws BrowserErrorException, IncorrectUserException, ElementNotFoundException, ErrorKillProcessException {
        AutomaticBrowser automaticBrowser = new AutomaticBrowser( userTextField.getText(), passwordPasswordField.getText() );
        questionsPreconfiguredList = automaticBrowser.getQuestions(selectedBrowser);
        killProcess();
    }

    private void evaluateProfessorInBrowser() throws BrowserErrorException, IncorrectUserException, ErrorKillProcessException, ElementNotFoundException {
        AutomaticBrowser automaticBrowser = new AutomaticBrowser(userTextField.getText(), passwordPasswordField.getText());
        //automaticBrowser.evaluateOptions(selectedBrowser, selectedAnswerHashMap );
        killProcess();
    }

    private void killProcess() throws ErrorKillProcessException {
        try{
            SystemControl.killProcess(selectedBrowser);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            throw new ErrorKillProcessException("¡No se pudo eliminar el proceso!", e);
        }
    }

    private void getQuestionnariePreconfiguredFromFile() {
        try {
            questionsPreconfiguredList = (List) Serializer.unSerializeObject(Serializer.DATA_QUESTIONS_PATH);
        } catch (IOException | ClassNotFoundException e) {
            String title = "¡Problema al leer archivo de respuestas!";
            String content = "¡Existe un problema para leer las preguntas del archivo de respuestas, verifica que exista en la carpeta correspondiente!";
            OperationAlert.showErrorAlert(title, content);
        }
    }

    private void getAnswersPreconfiguredFromFile() {
        try {
            selectedAnswerHashMap = (HashMap) Serializer.unSerializeObject(Serializer.DATA_ANSWER_SELECTED_PATH);
        } catch (IOException | ClassNotFoundException e) {
            String title = "¡Problema al leer archivo de respuestas!";
            String content = "¡Existe un problema para leer las respuestas seleccionadas del archivo de respuestas, verifica que exista en la carpeta correspondiente!";
            OperationAlert.showErrorAlert(title, content);
        }
    }

    private void verifyInputDataAndSelection() throws EmptyUsernameFieldException, EmptyPasswordFieldException, BrowserNotSelectedException {
        if( userTextField.getText().isEmpty() ) {
            throw new EmptyUsernameFieldException("¡El campo de nombre de usuario no se ha llenado!");
        }
        if( passwordPasswordField.getText().isEmpty() ) {
            throw new EmptyPasswordFieldException("¡El campo de contraseña no se ha llenado!");
        }
        if( selectedBrowser == null ) {
            throw new BrowserNotSelectedException("¡No se ha seleccionado un navegador!");
        }
    }

    private void configureToggleButton() {
        safariButton.setToggleGroup(toggleGroup);
        chromeButton.setToggleGroup(toggleGroup);
        firefoxButton.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener( (observable, oldValue, newValue) -> {
            if(newValue != null) {
                evaluateButton.setDisable(false);
            }
        });
    }

}
