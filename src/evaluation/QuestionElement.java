package evaluation;

import java.io.Serializable;
import java.util.List;

public class QuestionElement implements Serializable {
    private static final long serialVersionUID = 442342342512948352L;
    private String question;
    private QuestionType questionType;
    private List<AnswerElement> answerElements;
    private TableAnswerElement tableAnswerElements;
    private OpenAnswerElement openAnswerElement;

    public OpenAnswerElement getOpenAnswerElement() {
        return openAnswerElement;
    }

    public void setOpenAnswerElement(OpenAnswerElement openAnswerElement) {
        this.openAnswerElement = openAnswerElement;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<AnswerElement> getAnswerElements() {
        return answerElements;
    }

    public void setAnswerElements(List<AnswerElement> answerElements) {
        this.answerElements = answerElements;
    }

    public TableAnswerElement getTableAnswerElements() {
        return tableAnswerElements;
    }

    public void setTableAnswerElements(TableAnswerElement tableAnswerElements) {
        this.tableAnswerElements = tableAnswerElements;
    }

    @Override
    public String toString() {
        return "QuestionElement{" +
                "question='" + question + '\'' +
                ", questionType=" + questionType +
                ", answerElements=" + answerElements +
                ", tableAnswerElements=" + tableAnswerElements +
                ", openAnswerElement=" + openAnswerElement +
                '}';
    }

}
