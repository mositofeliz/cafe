package Tareas;

import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import puertoscafe.Slot;

/**
 *
 * @author
 */
public class TaskMerger {

    private final Slot s;

    public TaskMerger() {
        s = new Slot();
    }

    public Slot Merge(Slot BebidasFriaS, Slot BebidasCalienteS) throws XPathExpressionException, ParserConfigurationException {
        List<Document> bebidasF = BebidasFriaS.getComandas();
        List<Document> bebidasC = BebidasCalienteS.getComandas();
        int tamBebidasF = bebidasF.size();
        int tamBebidasC = bebidasC.size();
        String raiz = bebidasC.get(0).getFirstChild().getNodeName();
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document xmldoc = docBuilder.newDocument();
        Node root, bebida, order = xmldoc.importNode(bebidasC.get(0).getElementsByTagName("order_id").item(0), true);
        NodeList drinks;
        root = xmldoc.createElement(raiz);
        xmldoc.appendChild(root);
        //Cada comanda tiene el mismo id
        root.appendChild(order);
        //bebidas frias
        for (int i = 0; i < tamBebidasF; i++) {
            drinks = bebidasF.get(i).getElementsByTagName("drinks");
            for (int j = 0; j < drinks.getLength(); j++) {
                bebida = xmldoc.importNode(drinks.item(j), true);
                 root.appendChild(bebida);
            }
           
        }
        //bebidas calientes
        for (int k = 0; k < tamBebidasC; k++) {
            drinks = bebidasC.get(k).getElementsByTagName("drinks");
            for (int c = 0; c < drinks.getLength(); c++) {
                bebida = xmldoc.importNode(drinks.item(c), true);
                root.appendChild(bebida);
            }
        }
        s.introducirComanda(xmldoc);
        printXmlDocument(xmldoc);
        return s;
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
