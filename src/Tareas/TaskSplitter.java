package Tareas;

import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import puertoscafe.Slot;

public class TaskSplitter {

    private final Slot s;

    public TaskSplitter(Slot slotE) {
        this.s = slotE;
    }

    public Slot Split(String Expression) throws XPathExpressionException, TransformerConfigurationException, TransformerException, ParserConfigurationException {//debemos desarrollar el programa para que lea mas de un xml?¿, en funcion de eso deberia modificar  la forma de leer dicho xml para el splitter
        Slot auxSplit = new Slot();//Obtenemos las comandas actuales para sobrescribir la antigua por la nueva 
        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression XPathExpression = xPath.compile(Expression);
        NodeList nodes = (NodeList) XPathExpression.evaluate(s.obtenerDoc(), XPathConstants.NODESET);
        int cantidadBebidas = nodes.getLength(); // Calculamos las bebidas para saber el total de divsiones que deberemos aplicar
        String raiz = s.obtenerDoc().getFirstChild().getNodeName();
        String etDrinks = s.obtenerDoc().getElementsByTagName("drinks").item(0).getNodeName();
        NodeList idsOr = s.obtenerDoc().getElementsByTagName("order_id");
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Node root, order, n;
        Element drinksN;
        for (int i = 0; i < cantidadBebidas; i++) {
            Document xmldoc = docBuilder.newDocument();
            root = xmldoc.createElement(raiz);
            order = xmldoc.importNode(idsOr.item(0), true);
            drinksN = xmldoc.createElement(etDrinks);
            n = xmldoc.importNode(nodes.item(i), true);
            xmldoc.appendChild(root);
            root.appendChild(order);
            root.appendChild(drinksN);
            drinksN.appendChild(n);
            auxSplit.introducirComanda(xmldoc);
        }
        List<Document> auxOrdenes = auxSplit.getComandas();
        for (int j = 0; j < auxOrdenes.size(); j++) {
            printXmlDocument(auxOrdenes.get(j));
        }
        return auxSplit;
    }

    public Slot getSlotE() {
        return this.s;
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
