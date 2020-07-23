package gui;

import evaluation.AnswerElement;
import gui.configure.ConfigureController;
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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class AutomatizationController implements Initializable {
    private ObservableList<ProfessorElement> professorElementObservableList;
    private List<QuestionElement> questionElementList;
    private ProfessorElement professorSelected;
    private HashMap<String, AnswerElement> selectedAnswerHashMap;
    private Tab tab;
    @FXML private ToggleGroup toggleGroup;
    @FXML private ToggleButton safariButton;
    @FXML private ToggleButton chromeButton;
    @FXML private ToggleButton firefoxButton;
    @FXML private TextField userTextField;
    @FXML private PasswordField passwordPasswordField;
    @FXML private ListView<ProfessorElement> professorListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureToggleButton();
        configureListView();
    }

    public void getStage() {
        FXMLLoader loader = new FXMLLoader( getClass().getResource("/gui/AutoViste.fxml") );
        loader.setController(this);
        try {
            AnchorPane autoPane = loader.load();
            tab = new Tab("Automatización");
            tab.setContent(autoPane);
            Parent root = autoPane;
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Tab getTab() {
        getStage();
        return tab;
    }

    @FXML
    void chromeButtonPressed(ActionEvent event) {
        chromeButton.setSelected(true);
        getQuestionnarieAndProfessors(BrowserType.CHROME);
    }

    @FXML
    void firefoxButtonPressed(ActionEvent event) {
        firefoxButton.setSelected(true);
        getQuestionnarieAndProfessors(BrowserType.FIREFOX);

    }

    @FXML
    void safariButtonPressed(ActionEvent event) {
        safariButton.setSelected(true);
        getQuestionnarieAndProfessors(BrowserType.SAFARI);
    }

    @FXML
    void configureAnswersButtonPressed(ActionEvent event) {
        if(questionElementList != null && questionElementList.size() != 0 ) {
            ConfigureController configureController = new ConfigureController(questionElementList);
            if(selectedAnswerHashMap != null) {
                configureController.setSelectedAnswersHashMap(selectedAnswerHashMap);
            }
            configureController.getStage();
            selectedAnswerHashMap = configureController.getSelectedAnswersHashMap();
            System.out.println("CONTENIDO DEL HASH MAPPP *************");
            configureController.getSelectedAnswersHashMap().forEach( (key, value) -> System.out.println("Key:" + key + " Value" + value) );
            System.out.println("***********************CONTENIDO DEL HASH MAPPP *************");
        }
    }

    @FXML
    void evaluateProfessorButtonPressed(ActionEvent event) {
        if( professorSelected != null && selectedAnswerHashMap != null) {
            AutomaticBrowser automaticBrowser = new AutomaticBrowser(userTextField.getText(), passwordPasswordField.getText());
            automaticBrowser.evaluateOptions(BrowserType.CHROME, selectedAnswerHashMap, professorSelected.getProfessor() );
        }
    }

    private void getQuestionnarieAndProfessors(String browserType) {
        AutomaticBrowser automaticBrowser = new AutomaticBrowser( userTextField.getText(), passwordPasswordField.getText() );
        Object[] questionnarie = automaticBrowser.getProfessorAndQuestions(browserType);
        professorElementObservableList.addAll( (List)questionnarie[0] );
        questionElementList = new ArrayList<>();
        questionElementList.addAll((List) questionnarie[1]);
        professorListView.setItems(professorElementObservableList);
    }

    private void configureToggleButton() {
        safariButton.setToggleGroup(toggleGroup);
        chromeButton.setToggleGroup(toggleGroup);
        firefoxButton.setToggleGroup(toggleGroup);
    }

    private void configureListView() {
        professorElementObservableList = FXCollections.observableArrayList();
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

    private void setListenerToListView() {
        professorListView.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            if(newValue != null) {
                professorSelected = newValue;
            }
        });
    }

}
