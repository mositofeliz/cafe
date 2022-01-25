package Tareas;

import java.util.List;
import org.w3c.dom.Document;
import puertoscafe.Slot;

/**
 *
 * @author 
 */
public class TaskCorrelator {
    Slot replicator, consulta;

    public TaskCorrelator(Slot repli, Slot consul) {
        this.replicator = repli;
        this.consulta=consul;
    }
    public boolean Correlator()
    {
        boolean encontrado = true;
        List<Document> comandasR = replicator.getComandas();
        List<Document> comandasC = consulta.getComandas();
        if (comandasC.size()==comandasR.size()) {//tenemos el mismo numero de comandas
            for (int i = 0; i < comandasC.size(); i++) {
                if (!comandasC.get(i).getElementsByTagName("name").item(0).getTextContent().equals(comandasR.get(i).getElementsByTagName("name").item(0).getTextContent())) {
                    encontrado = false;
                }
            }
        }
        return encontrado;
    }
    public Slot slotRepE1()
    {
        return this.replicator;
    }
      public Slot slotConsE2()
    {
        return this.consulta;
    }
}
