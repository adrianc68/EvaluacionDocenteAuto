package evaluation;

import java.io.Serializable;
import java.util.List;

public class QuestionElement implements Serializable {
    String question;
    QuestionType questionType;
    List<AnswerElement> answerElements;

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

    @Override
    public String toString() {
        return "QuestionElement{" +
                "question='" + question + '\'' +
                ", questionType=" + questionType +
                ", answerElements=" + answerElements +
                '}';
    }

}
