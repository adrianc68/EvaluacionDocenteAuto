import alert.OperationAlert;
import evaluation.QuestionType;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import evaluation.AnswerElement;
import evaluation.QuestionElement;
import util.Serializer;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ConfigureController implements Initializable {
    private AutomaticEval automaticEval;
    private HashMap<String, List<AnswerElement>> selectedAnswerHashMap;
    private ObservableList<AnswerElement> answersObservableList;
    private ObservableList<QuestionElement> questionsObservableList;
    private QuestionElement questionSelected;
    private AnswerElement answerSelected;
    private List<QuestionElement> questionsList;
    private Stage stage;
    @FXML private ListView<AnswerElement> answerListView;
    @FXML private ListView<QuestionElement> questionListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureListView();
        setItemsToListView();
    }

    public void getStage() {
        FXMLLoader loader = new FXMLLoader( getClass().getResource("/ConfigureVista.fxml") );
        loader.setController(this);
        try {
            AnchorPane autoPane = loader.load();
            Parent root = autoPane;
            stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAutomaticEval(AutomaticEval automaticEval) {
        this.automaticEval = automaticEval;
    }

    public ConfigureController(List<QuestionElement> questionElementList) {
        this.questionsList = questionElementList;
    }

    public HashMap<String, List<AnswerElement>> getSelectedAnswerHashMap() {
        return selectedAnswerHashMap;
    }

    public void setSelectedAnswerHashMap(HashMap<String, List<AnswerElement>> selectedAnswerHashMap) {
        this.selectedAnswerHashMap = selectedAnswerHashMap;
    }

    @FXML
    void closeButtonPressed(ActionEvent event) {
        stage.close();
    }

    @FXML
    void updateQuestionsButtonPressed(ActionEvent event) {
        automaticEval.updateQuestionnaire();
        questionsList = automaticEval.getQuestionsList();
        questionsObservableList.clear();
        questionsObservableList.addAll(questionsList);
        questionListView.setItems(questionsObservableList);
    }

    @FXML
    void resetAnswersButtonPressed(ActionEvent event) {
        selectedAnswerHashMap.clear();
        answerListView.getSelectionModel().clearSelection();
        questionListView.getSelectionModel().clearSelection();
        questionSelected = null;
        answerSelected = null;
    }

    @FXML
    void saveAnswerButtonPressed(ActionEvent event) {
        if(answerSelected != null) {
            selectedAnswerHashMap.put( questionSelected.getQuestion() , castObservableListToList( answerListView.getSelectionModel().getSelectedItems() ) );
        }
    }

    @FXML
    void saveAnswersInFileButtonPressed(ActionEvent event) {
        try {
            Serializer.serializeObject(selectedAnswerHashMap, Serializer.DATA_ANSWER_SELECTED_PATH);
            Serializer.serializeObject(questionsList, Serializer.DATA_QUESTIONS_PATH);
            showSuccessfullAlert();
        } catch (IOException e) {
            String title = "¡Problema al guardar las preguntas y respuestas en el archivo de respuestas!";
            String content = "¡Existe un problema para almacenar, verifica que el archivo exista en la carpeta contenedora!";
            OperationAlert.showErrorAlert(title, content);
        }
    }

    private void showSuccessfullAlert() {
        String title = "¡Guardado en archivo de respuestas!";
        String content = "Se ha guardado las preguntas y respuestas en el archivo de respuestas. Esta se cargará al iniciar el sistema";
        OperationAlert.showSuccessfullAlert(title, content);
    }

    private List<AnswerElement> castObservableListToList(ObservableList<AnswerElement> observableList) {
        List<AnswerElement> list = new ArrayList<>();
        for ( AnswerElement input : observableList ) {
            list.add(input);
        }
        return list;
    }

    private void setItemsToListView() {
        questionsObservableList.clear();
        if( questionsList != null ){
            questionsObservableList.addAll(questionsList);
            questionListView.setItems(questionsObservableList);
        }
    }

    private void configureListView() {
        answersObservableList = FXCollections.observableArrayList();
        questionsObservableList = FXCollections.observableArrayList();
        if(selectedAnswerHashMap == null) {
            selectedAnswerHashMap = new HashMap<>();
        }
        questionListView.setCellFactory( param -> new ListCell<QuestionElement>() {
            protected void updateItem(QuestionElement p, boolean empty){
                super.updateItem(p, empty);
                if(empty || p == null || p.getQuestion() == null){
                    setText("");
                } else{
                    setText(p.getQuestion());
                }
            }
        });
        answerListView.setCellFactory( param -> new ListCell<AnswerElement>() {
            protected void updateItem(AnswerElement p, boolean empty) {
                super.updateItem(p, empty);
                if(empty || p == null || p.getAnswer() == null) {
                    setText("");
                } else {
                    setText( p.getAnswer() );
                }
            }
        });
        setListenerToListView();
    }

    private void setListenerToListView() {
        questionListView.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            if(newValue != null) {
                questionSelected = newValue;
                answersObservableList.clear();
                answersObservableList.addAll( questionSelected.getAnswerElements() );
                answerListView.setItems(answersObservableList);
                if(questionSelected.getQuestionType().equals( QuestionType.MULTIPLE) ) {
                    answerListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                } else {
                    answerListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                }
                List<AnswerElement> selectedAnswerPreviously = selectedAnswerHashMap.get( questionSelected.getQuestion() );
                if(selectedAnswerPreviously != null ) {
                    for(AnswerElement answerElement : selectedAnswerPreviously) {
                        answerListView.getSelectionModel().select(answerElement.getElementPosition() - 1);
                    }
                }
            }
        });
        answerListView.getSelectionModel().selectedItemProperty().addListener( ((observable, oldValue, newValue) -> {
            if(newValue != null) {
                answerSelected = newValue;
            }
        }));
    }

}
