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
public class TaskAgreggator {

    Slot s;

    public TaskAgreggator(Slot slot) {
        this.s = slot;
    }

    public Document Aggrega() throws XPathExpressionException, ParserConfigurationException {
        List<Document> auxOrdenes = s.getComandas();
        String raiz = s.obtenerDoc().getFirstChild().getNodeName();
        String etDrinks = auxOrdenes.get(0).getElementsByTagName("drinks").item(0).getNodeName();
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document xmldoc = docBuilder.newDocument();
        Node root, drinks, drink, order = xmldoc.importNode(s.obtenerDoc().getElementsByTagName("order_id").item(0), true);
        root = xmldoc.createElement(raiz);
        xmldoc.appendChild(root);
        //Cada comanda tiene el mismo id
        root.appendChild(order);
        drinks = xmldoc.createElement(etDrinks);
        root.appendChild(drinks);
        NodeList bebidas = auxOrdenes.get(0).getElementsByTagName("drink");  
        int noBebidas = bebidas.getLength();
        for (int i = 0; i < noBebidas; i++) {
            drink = xmldoc.importNode(bebidas.item(i), true);
            drinks.appendChild(drink);
        }
        printXmlDocument(xmldoc);
        return xmldoc;
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
