package puertoscafe;

import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;

public class puertoSalida {
 private final Slot s;
    public puertoSalida(Slot a) {
        this.s=a;
    }
    public Document read(Slot s) throws XPathExpressionException
    { 
       return s.obtenerDoc();
    }
    public Slot getSlotE()
    {
        return this.s;
    }    
}
