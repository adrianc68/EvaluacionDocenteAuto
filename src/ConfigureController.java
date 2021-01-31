import alert.OperationAlert;
import evaluation.AnswerElement;
import evaluation.QuestionMapVBox;
import evaluation.QuestionType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import evaluation.QuestionElement;
import util.Serializer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ConfigureController implements Initializable {
    private Stage stage;
    private AutomaticEval automaticEval;
    private List<QuestionElement> questionsPreconfiguredList;
    private QuestionElement questionElementSelectedFromFlowPane;
    @FXML private FlowPane questionsFlowPane;
    @FXML private VBox tableQuestionVBox;
    @FXML private ListView<String> subQuestionListView;
    private ObservableList<String> rowInstructionTableObservableList;
    @FXML private ListView<AnswerElement> subAnswerListView;
    private ObservableList<AnswerElement> tableAnswerElementObservableList;
    @FXML private VBox simpleQuestionVBox;
    @FXML private ListView<AnswerElement> simpleQuestionAnswersListView;
    private ObservableList<AnswerElement> answerElementObservableList;
    @FXML private VBox openQuestionVBox;
    @FXML private Label questionLabel;
    @FXML private TextArea openQuestionTextArea;

    private HashMap<String, List<AnswerElement>> selectedAnswerHashMap;
    private AnswerElement answerSelected;
    private LinkedHashMap<String, AnswerElement> answerTableElementsSelected;

    private QuestionMapVBox questionMapVBoxSelected;
    private String subQuestionSelected;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hideQuestionsVBox();
        initializeSelectedListenerListView();
        initializeSimpleQuestionCellFactory();
        initializeTableSubAnswerCellFactory();
        questionLabel.setText("Selecciona una pregunta del mapa de preguntas para desplegar las respuestas.");

        if (selectedAnswerHashMap == null) {
            selectedAnswerHashMap = new HashMap<>();
        } else {
            selectedAnswerHashMap.forEach( (key, value) -> {

            });
        }


        if (questionsPreconfiguredList != null) {
            initializeQuestionElementSelectedListener();
        }

    }

    public void getStage() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConfigureVista.fxml"));
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

    public HashMap<String, List<AnswerElement>> getSelectedAnswerHashMap() {
        return selectedAnswerHashMap;
    }

    public void setSelectedAnswerHashMap(HashMap<String, List<AnswerElement>> selectedAnswerHashMap) {
        this.selectedAnswerHashMap = selectedAnswerHashMap;
    }

    public void setAutomaticEval(AutomaticEval automaticEval) {
        this.automaticEval = automaticEval;
    }

    public void setQuestionsPreconfiguredList(List<QuestionElement> questionsPreconfiguredList) {
        this.questionsPreconfiguredList = questionsPreconfiguredList;
    }

    @FXML
    void CancelButtonPressed(ActionEvent event) {
        stage.close();
    }

    @FXML
    void UpdateQuestionButtonPressed(ActionEvent event) {
        automaticEval.updateQuestionnaire();
    }

    @FXML
    void saveChangesToFileButtonPressed(ActionEvent event) {
        try {
            Serializer.serializeObject(selectedAnswerHashMap, Serializer.DATA_ANSWER_SELECTED_PATH);
            Serializer.serializeObject(questionsPreconfiguredList, Serializer.DATA_QUESTIONS_PATH);
            showSuccessfullAlert();
        } catch (IOException e) {
            String title = "¡Problema al guardar las preguntas y respuestas en el archivo de respuestas!";
            String content = "¡Existe un problema para almacenar, verifica que el archivo exista en la carpeta contenedora!";
            OperationAlert.showErrorAlert(title, content);
        }
    }

    private void clearListView() {
        subAnswerListView.getItems().clear();
        subQuestionListView.getItems().clear();
        simpleQuestionAnswersListView.getItems().clear();
    }

    private void hideQuestionsVBox() {
        openQuestionVBox.setVisible(false);
        simpleQuestionVBox.setVisible(false);
        tableQuestionVBox.setVisible(false);
    }

    private void showSuccessfullAlert() {
        String title = "¡Guardado en archivo de respuestas!";
        String content = "Se ha guardado las preguntas y respuestas en el archivo de respuestas. Esta se cargará al iniciar el sistema";
        OperationAlert.showSuccessfullAlert(title, content);
    }

    private void initializeSimpleQuestionCellFactory() {
        simpleQuestionAnswersListView.setCellFactory(e -> new ListCell<AnswerElement>() {
            @Override
            protected void updateItem(AnswerElement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getInstruction() == null) {
                    setText(null);
                } else {
                    setText(item.getInstruction());
                }
            }
        });
    }

    private void initializeTableSubAnswerCellFactory() {
        subAnswerListView.setCellFactory(e -> new ListCell<AnswerElement>() {
            @Override
            protected void updateItem(AnswerElement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getInstruction() == null) {
                    setText(null);
                } else {
                    setText(item.getInstruction());
                }
            }
        });
    }

    private void initializeSelectedListenerListView() {
        simpleQuestionAnswersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (selectedAnswerHashMap != null) {
                    repaintAnswerMapVBox();
                    selectedAnswerHashMap.put(questionElementSelectedFromFlowPane.getQuestion(), castObservableListToList(simpleQuestionAnswersListView.getSelectionModel().getSelectedItems()));
                }
            }
        });

        subAnswerListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (selectedAnswerHashMap != null) {
                    repaintAnswerMapVBox();
                    newValue.setSubInstruction(subQuestionSelected);
                    answerTableElementsSelected.put( subQuestionSelected, newValue);
                    if( selectedAnswerHashMap.get(questionElementSelectedFromFlowPane.getQuestion()) != null ) {
                        for( AnswerElement element : selectedAnswerHashMap.get(questionElementSelectedFromFlowPane.getQuestion() ) ) {
                            answerTableElementsSelected.put( element.getSubInstruction(), element);
                        }
                    }
                    selectedAnswerHashMap.put(questionElementSelectedFromFlowPane.getQuestion(), castLinkedHashMapToList(answerTableElementsSelected) );
                }
            }
        }));

        subQuestionListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if( selectedAnswerHashMap != null ) {
                    tableAnswerElementObservableList = FXCollections.observableArrayList();
                    tableAnswerElementObservableList.addAll(questionElementSelectedFromFlowPane.getTableAnswerElements().getRows().get(newValue) );
                    subAnswerListView.setItems(tableAnswerElementObservableList);
                    subQuestionSelected = newValue;
                    setSelectedAnswers();
                } // INTRODUCIDO VERIFICAR BUG
            }
        });


        openQuestionTextArea.textProperty().addListener( (observable, oldValue, newValue) -> {
            if(newValue != null) {
                if(selectedAnswerHashMap != null ) {
                    List<AnswerElement> answerList = new ArrayList<>();
                    AnswerElement answerElement = new AnswerElement();
                    answerElement.setInstruction( openQuestionTextArea.getText() );
                    answerElement.setElementPosition(1);
                    // FALTA AGREGAR SOPORTE PARA PREGUNTAS ABIERTAS
                    // AGREGAR SOPORTE
                    // SETANSWERS PARA OPEN QUESTION
                    // Y TERMINAR EL DE EVALUACION
                    //answerList.add()
                    selectedAnswerHashMap.put( questionElementSelectedFromFlowPane.getQuestion(), )
                }
            }
        });



    }

    private List<AnswerElement> castObservableListToList(ObservableList<AnswerElement> observableList) {
        List<AnswerElement> list = new ArrayList<>();
        for (AnswerElement input : observableList) {
            list.add(input);
        }
        return list;
    }

    private List<AnswerElement> castLinkedHashMapToList(LinkedHashMap<String, AnswerElement> listInput) {
        List<AnswerElement> list = new ArrayList<>();
        listInput.forEach( (key, value) -> {
            list.add(value);
        });
        return list;
    }

    private void setSelectedAnswers() {
        if (questionElementSelectedFromFlowPane.getQuestionType() == QuestionType.SINGLE || questionElementSelectedFromFlowPane.getQuestionType() == QuestionType.MULTIPLE) {
            List<AnswerElement> selectedAnswerPreviously = selectedAnswerHashMap.get(questionElementSelectedFromFlowPane.getQuestion());
            if (selectedAnswerPreviously != null) {
                repaintAnswerMapVBox();
                for (AnswerElement answerElement : selectedAnswerPreviously) {
                    simpleQuestionAnswersListView.getSelectionModel().select(answerElement.getElementPosition()-1);
                }
            }
        } else if (questionElementSelectedFromFlowPane.getQuestionType() == QuestionType.TABLE) {
            List<AnswerElement> selectedAnswerPreviously = selectedAnswerHashMap.get(questionElementSelectedFromFlowPane.getQuestion() );
            if(selectedAnswerPreviously != null ) {
                for( AnswerElement answerElement : selectedAnswerPreviously){
                    if( answerElement.getSubInstruction().equals(subQuestionSelected) ) {
                        subAnswerListView.getSelectionModel().select(answerElement.getElementPosition()-1);
                        break;
                    }
                }
            }
        } else if (questionElementSelectedFromFlowPane.getQuestionType() == QuestionType.OPEN) {

        }


    }

    private void repaintAnswerMapVBox() {
        questionMapVBoxSelected.setAnswered(true);
        questionMapVBoxSelected.repaint();
    }

    private void initializeQuestionElementSelectedListener() {
        for (int i = 0; i < questionsPreconfiguredList.size(); i++) {
            QuestionElement questionElementFromList = questionsPreconfiguredList.get(i);
            QuestionMapVBox questionMapVBox = new QuestionMapVBox(questionElementFromList, i + 1);
            if(selectedAnswerHashMap != null && selectedAnswerHashMap.containsKey(questionElementFromList.getQuestion() ) ) {
                questionMapVBox.setAnswered(true);
                questionMapVBox.repaint();
            }

            questionMapVBox.setOnMouseClicked((MouseEvent event) -> {
                hideQuestionsVBox();
                clearListView();
                questionElementSelectedFromFlowPane = questionElementFromList;
                questionMapVBoxSelected = questionMapVBox;
                questionLabel.setText(questionMapVBox.getNumber() + ".- " + questionElementFromList.getQuestion());
                if (questionElementSelectedFromFlowPane.getQuestionType() == QuestionType.SINGLE) {
                    simpleQuestionVBox.setVisible(true);
                    answerElementObservableList = FXCollections.observableArrayList();
                    answerElementObservableList.addAll(questionElementSelectedFromFlowPane.getAnswerElements());
                    simpleQuestionAnswersListView.setItems(answerElementObservableList);
                    simpleQuestionAnswersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                } else if (questionElementSelectedFromFlowPane.getQuestionType() == QuestionType.MULTIPLE) {
                    simpleQuestionVBox.setVisible(true);
                    answerElementObservableList = FXCollections.observableArrayList();
                    answerElementObservableList.addAll(questionElementSelectedFromFlowPane.getAnswerElements());
                    simpleQuestionAnswersListView.setItems(answerElementObservableList);
                    simpleQuestionAnswersListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                } else if (questionElementSelectedFromFlowPane.getQuestionType() == QuestionType.OPEN) {
                    openQuestionVBox.setVisible(true);
                    openQuestionTextArea.clear();
                } else if (questionElementSelectedFromFlowPane.getQuestionType() == QuestionType.TABLE) {
                    tableQuestionVBox.setVisible(true);
                    rowInstructionTableObservableList = FXCollections.observableArrayList();
                    rowInstructionTableObservableList.addAll(questionElementSelectedFromFlowPane.getTableAnswerElements().getRows().keySet() );
                    subQuestionListView.setItems(rowInstructionTableObservableList);
                    answerTableElementsSelected = new LinkedHashMap<>();
                }
                setSelectedAnswers();
            });
            questionsFlowPane.getChildren().add(questionMapVBox);
        }
    }


}
