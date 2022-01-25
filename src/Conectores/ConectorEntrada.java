package Conectores;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Usuario
 */
public class ConectorEntrada {
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Document doc;

    /**
     * Parsea una comanda en XML
     *
     * @param fichero nombre del fichero XML que usa la comanda
     */
    public void cargar_comanda(String fichero) {
        try {
            File inputFile = new File(fichero);
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            this.doc = dBuilder.parse(inputFile);
            this.doc.getDocumentElement().normalize();
        } catch (IOException | ParserConfigurationException | DOMException | SAXException e) {
        }
    }

    public Document getDocument() {
        return doc;
    }
}
