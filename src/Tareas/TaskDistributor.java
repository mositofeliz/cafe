package Tareas;

import java.util.List;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import puertoscafe.Slot;

public class TaskDistributor {

    private final Slot s;

    public TaskDistributor(Slot slot) {
        this.s = slot;
    }

    public Slot TDis(String tipo) throws XPathExpressionException {
        Slot s2 = new Slot();
        int nEl = s.getComandas().size();
        List<Document> auxOrdenes = s.getComandas();
        String tipoDoc;
        if ("fria".equals(tipo)) {
            for (int i = 0; i < nEl; i++) {
                tipoDoc=auxOrdenes.get(i).getElementsByTagName("type").item(0).getTextContent();
                if (tipoDoc.equals("cold")) {
                    s2.introducirComanda(auxOrdenes.get(i));
                }
            }
        }
        if ("caliente".equals(tipo)) {
           for (int i = 0; i < nEl; i++) {
                tipoDoc=auxOrdenes.get(i).getElementsByTagName("type").item(0).getTextContent();
                if (tipoDoc.equals("hot")) {
                    s2.introducirComanda(auxOrdenes.get(i));
                }
            }
        }
        return s2;
    }
}
