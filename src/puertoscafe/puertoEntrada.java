
package puertoscafe;

import org.w3c.dom.Document;

public class puertoEntrada {
    private final Slot s;
    public puertoEntrada() {
        s=new Slot();
    }
    public void write(Document doc)
    {
       s.introducirComanda(doc);
    }
    public Slot getSlotE()
    {
        return this.s;
    }
}
