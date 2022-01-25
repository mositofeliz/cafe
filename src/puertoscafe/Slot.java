package puertoscafe;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;


/**
 *
 * @author Adrian y Jonathan
 */
public class Slot {
    private final List<Document> comandas;//¿donde creamos los documentos, es decir, hace falta hacer el: comandas = new ArrayList<>..

    public Slot() {
        comandas = new ArrayList<>();
    }
    
    public void introducirComanda(Document doc){
        comandas.add(doc);
    }
    public Document obtenerDoc() throws XPathExpressionException
    {
         return comandas.get(0);
    }
    public List<Document> getComandas()
    {
        return this.comandas;
    }
}
