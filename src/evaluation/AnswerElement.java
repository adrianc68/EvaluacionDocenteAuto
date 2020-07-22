package evaluation;

import java.io.Serializable;

public class AnswerElement implements Serializable {
    String answer;
    int elementPosition;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getElementPosition() {
        return elementPosition;
    }

    public void setElementPosition(int elementPosition) {
        this.elementPosition = elementPosition;
    }

    @Override
    public String toString() {
        return "AnswerElement{" +
                "answer='" + answer + '\'' +
                ", elementPosition=" + elementPosition +
                '}';
    }

}
