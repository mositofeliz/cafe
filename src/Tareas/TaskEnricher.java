package Tareas;

import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import puertoscafe.Slot;

public class TaskEnricher {

    Slot entrada1, entrada2;

    public TaskEnricher(Slot e1, Slot e2) {
        this.entrada1 = e1;
        this.entrada2 = e2;
        
    }

    public Slot enrich() {
        Slot aux = new Slot();
        List<Document> comandasE1 = entrada1.getComandas();
        List<Document> comandasE2 = entrada2.getComandas();
        Node stock ;
        
        for (int i = 0; i < comandasE1.size(); i++) {//no hace falta comprobar si e1 & e2 tienen la misma longitud pues eso ya se comprobo
            stock = comandasE2.get(i).getElementsByTagName("stock").item(0);
            comandasE1.get(i).getElementsByTagName("drink").item(0).appendChild(comandasE1.get(i).importNode(stock, true));
            aux.introducirComanda(comandasE1.get(i));
        }
        return aux;
    }
}
