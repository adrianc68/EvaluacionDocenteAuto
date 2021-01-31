package evaluation;

import java.io.Serializable;

public class AnswerElement implements Serializable {
    private static final long serialVersionUID = 442342342512948352L;
    private String subInstruction;
    private String instruction;
    private int elementPosition;

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public int getElementPosition() {
        return elementPosition;
    }

    public void setElementPosition(int elementPosition) {
        this.elementPosition = elementPosition;
    }

    public String getSubInstruction() {
        return subInstruction;
    }

    public void setSubInstruction(String subInstruction) {
        this.subInstruction = subInstruction;
    }

    @Override
    public String toString() {
        return "AnswerElement{" +
                "subInstruction='" + subInstruction + '\'' +
                ", instruction='" + instruction + '\'' +
                ", elementPosition=" + elementPosition +
                '}';
    }

}
