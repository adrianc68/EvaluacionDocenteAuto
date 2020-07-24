package evaluation;

import java.io.Serializable;

public class ProfessorElement implements Serializable {
    private static final long serialVersionUID = 442342342512948352L;
    private String professor;
    private int positionWebElement;

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public int getPositionWebElement() {
        return positionWebElement;
    }

    public void setPositionWebElement(int positionWebElement) {
        this.positionWebElement = positionWebElement;
    }

    @Override
    public String toString() {
        return "ProfessorElement{" +
                "professor='" + professor + '\'' +
                ", positionElement=" + positionWebElement +
                '}';
    }

}
