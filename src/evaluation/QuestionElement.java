package evaluation;

import java.io.Serializable;
import java.util.List;

public class QuestionElement implements Serializable {
    String question;
    List<AnswerElement> answerElements;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<AnswerElement> getAnswerElements() {
        return answerElements;
    }

    public void setAnswerElements(List<AnswerElement> answerElements) {
        this.answerElements = answerElements;
    }

    @Override
    public String toString() {
        return "QuestionWebElement{" +
                "question='" + question + '\'' +
                ", answerWebElements=" + answerElements +
                '}';
    }


}
