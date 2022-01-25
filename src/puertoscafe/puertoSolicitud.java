package puertoscafe;

import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class puertoSolicitud {
    private final Slot slot;
    private Slot comandasConsultadas;
    public puertoSolicitud(Slot entrada){
        this.slot=entrada;
    }
    public List<Document>obtenerComandas()
    {
        return slot.getComandas();
    }

    public void SlotSalida(Slot consultar) {
        System.out.println("Resultados de la consulta:");
        this.comandasConsultadas = consultar;
        List<Document>aux = obtenerComandas();
        int tam = aux.size();
        for (int i = 0; i  < tam ; i++) {
            printXmlDocument(aux.get(i));
        }
    }
    public Slot readPS()
    {
        return this.comandasConsultadas;
    }
        public static void printXmlDocument(Document document) {
        DOMImplementationLS domImplementationLS
                = (DOMImplementationLS) document.getImplementation();
        LSSerializer lsSerializer
                = domImplementationLS.createLSSerializer();
        String string = lsSerializer.writeToString(document);
        System.out.println(string + "\n");
    }
}
