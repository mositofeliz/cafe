/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conectores;


import java.io.File;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;


/**
 *
 * @author Usuario
 */
public class ConectorSalida {
    private Document document;

    public ConectorSalida() {
        this.document=null;
    }

    public void Cargar_Comanda(Document s) throws XPathExpressionException {
        this.document=s;
        printXmlDocument(document);
        
    }
        public static void printXmlDocument(Document document) {
        DOMImplementationLS domImplementationLS
                = (DOMImplementationLS) document.getImplementation();
        LSSerializer lsSerializer
                = domImplementationLS.createLSSerializer();
        String string = lsSerializer.writeToString(document);
        System.out.println(string + "\n");
    }

    public void generarSolucion() throws TransformerConfigurationException, TransformerException {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(this.document);
                StreamResult result = new StreamResult(new File("./src/prueba/resultado"+".xml"));
                transformer.transform(source, result);
    }
}
