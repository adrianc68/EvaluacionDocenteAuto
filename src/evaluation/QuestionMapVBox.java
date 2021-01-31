package evaluation;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class QuestionMapVBox extends VBox {
    private final int CARD_HEIGHT = 30;
    private final int CARD_WIDTH = 30;
    private final String ANSWERED_QUESTION_COLOR = "#4db3b2";
    private final String NOT_ANSWERED_QUESTION_COLOR = "#999999";
    private int number;
    private QuestionElement questionElement;
    private boolean answered = false;

    public QuestionMapVBox(QuestionElement questionElement, int number) {
        this.questionElement = questionElement;
        this.number = number;
        initCard();
    }

    public QuestionElement getQuestionElement() {
        return questionElement;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
        repaint();
    }

    public void repaint() {
        getChildren().clear();
        initCard();
    }

    public int getNumber() {
        return number;
    }

    public void initCard() {
        setHeight(CARD_HEIGHT);
        setPrefHeight(CARD_HEIGHT);
        setMaxHeight(CARD_HEIGHT);
        setMinHeight(CARD_HEIGHT);
        setWidth(CARD_WIDTH);
        setPrefWidth(CARD_WIDTH);
        setMaxWidth(CARD_WIDTH);
        setMinWidth(CARD_WIDTH);
        String color = (answered) ? ANSWERED_QUESTION_COLOR : NOT_ANSWERED_QUESTION_COLOR;
        setStyle("-fx-background-color:" + color + ";" );
        //setStyle("-fx-background-color:blue");
        Label numberLabel = new Label();
        numberLabel.setText( String.valueOf(number) );
        numberLabel.setStyle("-fx-text-fill:#fff");
        setAlignment(Pos.CENTER);
        getChildren().add(numberLabel);
    }


}
