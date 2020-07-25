import alert.OperationAlert;
import browser.BrowserProperties;
import evaluation.AnswerElement;
import exceptions.BrowserErrorException;
import exceptions.BrowserNotSelectedException;
import exceptions.ElementNotFoundException;
import exceptions.EmptyPasswordFieldException;
import exceptions.EmptyUsernameFieldException;
import exceptions.ErrorKillProcessException;
import exceptions.IncorrectUserException;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.AutomaticBrowser;
import evaluation.ProfessorElement;
import evaluation.QuestionElement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.openqa.selenium.remote.BrowserType;
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
    private ObservableList<ProfessorElement> professorsObservableList;
    private HashMap<String, List<AnswerElement>> selectedAnswerHashMap;
    private List<QuestionElement> questionsList;
    private ProfessorElement professorSelected;
    private String selectedBrowser;
    private Tab tab;
    private Stage stage;
    @FXML private TextField userTextField;
    @FXML private ToggleGroup toggleGroup;
    @FXML private ToggleButton safariButton;
    @FXML private ToggleButton chromeButton;
    @FXML private ToggleButton firefoxButton;
    @FXML private PasswordField passwordPasswordField;
    @FXML private Button evaluateButton;
    @FXML private Button getProfessorButton;
    @FXML private Button configureQuestionsAndAnswerButton;
    @FXML private ListView<ProfessorElement> professorListView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureToggleButton();
        configureListView();
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
        FXMLLoader loader = new FXMLLoader( getClass().getResource("/AutoViste.fxml") );
        loader.setController(this);
        try {
            AnchorPane autoPane = loader.load();
            tab = new Tab("Automatización");
            tab.setContent(autoPane);
            Parent root = autoPane;
            stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
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

    public List<QuestionElement> getQuestionsList() {
        return questionsList;
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
        ConfigureController configureController = new ConfigureController(questionsList);
        configureController.setAutomaticEval(this);
        if (selectedAnswerHashMap != null) {
            configureController.setSelectedAnswerHashMap(selectedAnswerHashMap);
        }
        configureController.getStage();
        selectedAnswerHashMap = configureController.getSelectedAnswerHashMap();
    }

    @FXML
    void getProfessorButtonPressed(ActionEvent event) {
        try {
            professorsObservableList.clear();
            getProfessorsFromBrowser();
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

    @FXML
    void evaluateProfessorButtonPressed(ActionEvent event) {
        if( professorSelected != null && selectedAnswerHashMap != null && selectedBrowser != null) {
            try {
                evaluateProfessorInBrowser();
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
                String content = "Debes matar el proceso manualmente.";
                OperationAlert.showErrorAlert(title, content);
            }
        }
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
        questionsList = automaticBrowser.getQuestions(selectedBrowser);
        killProcess();
    }

    private void getProfessorsFromBrowser() throws BrowserErrorException, IncorrectUserException, ElementNotFoundException, ErrorKillProcessException {
        AutomaticBrowser automaticBrowser = new AutomaticBrowser( userTextField.getText(), passwordPasswordField.getText() );
        professorsObservableList.addAll( automaticBrowser.getProfessor(selectedBrowser) );
        professorListView.setItems(professorsObservableList);
        killProcess();
    }

    private void evaluateProfessorInBrowser() throws BrowserErrorException, IncorrectUserException, ErrorKillProcessException {
        AutomaticBrowser automaticBrowser = new AutomaticBrowser(userTextField.getText(), passwordPasswordField.getText());
        automaticBrowser.evaluateOptions(selectedBrowser, selectedAnswerHashMap, professorSelected.getProfessor() );
        killProcess();
    }

    private void killProcess() throws ErrorKillProcessException {
        //  Yes but actually no.
        try{
            SystemControl.killProcess(selectedBrowser);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            throw new ErrorKillProcessException("¡No se pudo eliminar el proceso!", e);
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

    private void getQuestionnariePreconfiguredFromFile() {
        try {
            questionsList = (List) Serializer.unSerializeObject(Serializer.DATA_QUESTIONS_PATH);
        } catch (IOException | ClassNotFoundException e) {
            String title = "¡Problema al leer archivo de respuestas!";
            String content = "¡Existe un problema para leer las preguntas del archivo de respuestas, verifica que exista en la carpeta correspondiente!";
            OperationAlert.showErrorAlert(title, content);
        }
    }

    private void setListenerToListView() {
        professorListView.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            if(newValue != null) {
                professorSelected = newValue;
                evaluateButton.setDisable(false);
            }
        });
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
                getProfessorButton.setDisable(false);
            }
        });
    }

    private void configureListView() {
        professorsObservableList = FXCollections.observableArrayList();
        professorListView.setCellFactory( param -> new ListCell<ProfessorElement>() {
            protected void updateItem(ProfessorElement p, boolean empty) {
                super.updateItem(p, empty);
                if(empty || p == null || p.getProfessor() == null){
                    setText("");
                } else{
                    setText( p.getProfessor() );
                }
            }
        });
        setListenerToListView();
    }

}
