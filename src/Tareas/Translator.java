package Tareas;
import java.util.List;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import puertoscafe.*;

public class Translator{

    private final Slot entrada;
    private final Document xslt;

    public Translator(Slot e,Document Dxslt){
        this.entrada = e;
        this.xslt= Dxslt;
    }
    public Slot traducir() throws TransformerException{
        Slot aux = new Slot();
        List<Document> auxOrdenes = entrada.getComandas();
        int tamBebidasC = auxOrdenes.size();
        for (int i = 0; i < tamBebidasC; i++) {
            try{
                //leemos el documento xml y el xslt
                Source xmlSource = new DOMSource(auxOrdenes.get(i));
                Source xsltSource = new DOMSource(xslt);
                DOMResult result = new DOMResult();

                //Transformamos
                TransformerFactory transFact = TransformerFactory.newInstance();
                Transformer trans = transFact.newTransformer(xsltSource);
                trans.transform(xmlSource, result);
                Document resultDoc = (Document) result.getNode();
                aux.introducirComanda(resultDoc);
            }catch(TransformerException e){
                System.out.println("Error en el translator de tipo: " + e.getMessage());
            }
         
        }
        return aux;
    }
        public static void printXmlDocument(Document document) {
            System.out.println("-----LA PARTE DE LA TRADUCCION");
        DOMImplementationLS domImplementationLS
                = (DOMImplementationLS) document.getImplementation();
        LSSerializer lsSerializer
                = domImplementationLS.createLSSerializer();
        String string = lsSerializer.writeToString(document);
        System.out.println(string + "\n");
    }
}