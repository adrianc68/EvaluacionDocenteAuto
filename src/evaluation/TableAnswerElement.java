package evaluation;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

public class TableAnswerElement implements Serializable {
    private static final long serialVersionUID = 442342342512948352L;
    private LinkedHashMap<String, List<AnswerElement>> rows;

    public LinkedHashMap<String, List<AnswerElement>> getRows() {
        return rows;
    }

    public void setRows(LinkedHashMap<String, List<AnswerElement>> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "TableAnswerElement{" +
                "rows=" + rows +
                '}';
    }
}
