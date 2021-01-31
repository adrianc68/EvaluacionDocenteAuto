package evaluation;

import java.io.Serializable;

public class OpenAnswerElement implements Serializable {
    private static final long serialVersionUID = 442342342512948352L;
    private String instruction;

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    @Override
    public String toString() {
        return "OpenAnswerElement{" +
                "instruction='" + instruction + '\'' +
                '}';
    }


}
