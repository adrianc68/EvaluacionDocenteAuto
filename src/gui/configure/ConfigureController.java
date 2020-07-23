package gui.configure;

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
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ConfigureController implements Initializable {
    private AnswerElement answerSelected;
    private QuestionElement questionSelected;
    private HashMap<String, AnswerElement> selectedAnswersHashMap;
    private ObservableList<AnswerElement> answerElementObservableList;
    private ObservableList<QuestionElement> questionElementObservableList;
    private List<QuestionElement> questionElementsList;
    @FXML private ListView<AnswerElement> answerListView;
    @FXML private ListView<QuestionElement> questionListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureListView();
        setItemsToListView();
    }

    public ConfigureController(List<QuestionElement> questionElementList) {
        this.questionElementsList = questionElementList;
    }

    public void setSelectedAnswersHashMap(HashMap<String, AnswerElement> selectedAnswersHashMap) {
        this.selectedAnswersHashMap = selectedAnswersHashMap;
    }

    public HashMap<String, AnswerElement> getSelectedAnswersHashMap() {
        return selectedAnswersHashMap;
    }

    public void getStage() {
        FXMLLoader loader = new FXMLLoader( getClass().getResource("/gui/configure/ConfigureVista.fxml") );
        loader.setController(this);
        try {
            AnchorPane autoPane = loader.load();
            Parent root = autoPane;
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void resetAnswersButtonPressed(ActionEvent event) {
        selectedAnswersHashMap.clear();
        answerListView.getSelectionModel().clearSelection();
        questionListView.getSelectionModel().clearSelection();
        questionSelected = null;
        answerSelected = null;
    }

    @FXML
    void saveAnswerButtonPressed(ActionEvent event) {
        if(answerSelected != null) {
            System.out.println(answerListView.getSelectionModel().getSelectedItems());
            selectedAnswersHashMap.put( questionSelected.getQuestion() , answerSelected);
        }
    }

    @FXML
    void lookAnswerButtonPressed(ActionEvent event) {
//        ConfiguredAnswerController configuredAnswerController = new ConfiguredAnswerController(hashMap);
//        configuredAnswerController.getStage();
    }

    private void setItemsToListView() {
        questionElementObservableList.clear();
        questionElementObservableList.addAll(questionElementsList);
        questionListView.setItems(questionElementObservableList);
    }

    private void configureListView() {
        answerElementObservableList = FXCollections.observableArrayList();
        questionElementObservableList = FXCollections.observableArrayList();
        if(selectedAnswersHashMap == null) {
            selectedAnswersHashMap = new HashMap<>();
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
                if(empty || p == null || p.getAnswer() == null){
                    setText("");
                } else{
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
                answerElementObservableList.clear();
                answerElementObservableList.addAll( questionSelected.getAnswerElements() );
                answerListView.setItems(answerElementObservableList);
                if(questionSelected.getQuestionType().equals( QuestionType.MULTIPLE) ) {
                    answerListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                } else {
                    answerListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                }


                AnswerElement answerSelectedPreviously = selectedAnswersHashMap.get(questionSelected.getQuestion());
                if(answerSelectedPreviously != null) {
                    answerListView.getSelectionModel().select(answerSelectedPreviously);
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
