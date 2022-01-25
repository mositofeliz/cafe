package prueba;

import Tareas.TaskEnricher;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import Conectores.*;
import puertoscafe.*;
import Tareas.*;
import java.io.IOException;
import java.sql.SQLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class atendercomandas {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, InstantiationException, IllegalAccessException, SQLException, ClassNotFoundException {

        JOptionPane.showMessageDialog(null, "Introduzca el fichero con las comandas.");
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        String file = f.getAbsolutePath();
        //Leemos la comanda en formato xml haciendo un parser
        ConectorEntrada conector = new ConectorEntrada();
        conector.cargar_comanda(file);
        // Cargamos la comanda en formato document en nuestro proceso
        puertoEntrada pE = new puertoEntrada();
        pE.write(conector.getDocument());
        TaskSplitter tSplit = new TaskSplitter(pE.getSlotE());
        //Creamos el conector de Salida
        ConectorSalida conectorS = new ConectorSalida();
        // Se aplican las tareas
        try {

            String XpathExpression = JOptionPane.showInputDialog("Introduzca el XPathExpression: ");//cafe_order/drinks/drink
            //Dividimos los mensajes
            System.out.println("El taskSplitter se esta ejecutando......");
            System.out.println("Las comandas tras ejecutarse el Task Splitter: \n");
            Slot spliter = tSplit.Split(XpathExpression);
            //Distrbuimos las bebidas en func del tipo
            System.out.println("El taskDistributor se esta ejecutando.....");
            TaskDistributor tDis = new TaskDistributor(spliter);
            //Si son bebidas calientes
            Slot BebidasCalienteS = tDis.TDis("caliente");
            //Si son bebidas frías
            Slot BebidasFriaS = tDis.TDis("fria");
            System.out.println("Hay actualmente un total de  " + BebidasFriaS.getComandas().size() + " bebidas frias.");
            System.out.println("Hay actualmente un total de  " + BebidasCalienteS.getComandas().size() + " bebidas calientes.");
            //Ejecutamos el replicator
            System.out.println("El taskReplicator se esta ejecutando.");
            TaskReplicator tRepBC = new TaskReplicator(BebidasCalienteS);
            TaskReplicator tRepBF = new TaskReplicator(BebidasFriaS);
            Slot comandaTBC = tRepBC.replicate();
            Slot comandaCBC = tRepBC.replicate();
            //Bebidas Frías
            Slot comandaTBF = tRepBF.replicate();
            Slot comandaCBF = tRepBF.replicate();
            //Leemos el documento xslt
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xslt = db.parse("./src/tareas/translator.xslt");
            //Ejecutamos consulta a BD 
            System.out.println("El taskTranslator se esta ejecutando.");
            Translator transBC = new Translator(comandaTBC, xslt);
            Translator transBF = new Translator(comandaTBF, xslt);
            Slot docTransBC = transBC.traducir();
            puertoSolicitud pSBC = new puertoSolicitud(docTransBC);
            ConectorBBDD conexionBD = new ConectorBBDD(pSBC.obtenerComandas(), "hot");
            pSBC.SlotSalida(conexionBD.consultar());
            Slot docTransBF = transBF.traducir();
            puertoSolicitud pSBF = new puertoSolicitud(docTransBF);
            ConectorBBDD conexionBDBF = new ConectorBBDD(pSBF.obtenerComandas(), "cold");
            pSBF.SlotSalida(conexionBDBF.consultar());
            //Comprobamos que la consulta nos ha devuelto una solución relacionada a nuestro Slot
            System.out.println("El taskCorrelator se esta ejecutando.");
            TaskCorrelator tCorrBC = new TaskCorrelator(comandaCBC, pSBC.readPS());
            Slot comandasEnrBC = new Slot();
            if (tCorrBC.Correlator()) {
                //Añadimos la nueva información - Stock a su correspondiente bebida
                System.out.println("El taskEnricher se esta ejecutando.");
                TaskEnricher tEnrBC = new TaskEnricher(tCorrBC.slotRepE1(), tCorrBC.slotConsE2());
                comandasEnrBC = tEnrBC.enrich();
            } else {
                System.out.println("Se produjo un error en la consulta.");
            }
            //Comprobamos que la consulta nos ha devuelto una solución relacionada a nuestro Slot
            TaskCorrelator tCorrBF = new TaskCorrelator(comandaCBF, pSBF.readPS());//Inicializamos esta tarea con el Slot dado por Replicator
            Slot comandasEnrBF = new Slot();
            if (tCorrBF.Correlator()) {
                //Añadimos la nueva información - Stock a su correspondiente bebida
                System.out.println("El taskEnricher se esta ejecutando.");
                TaskEnricher tEnrBF = new TaskEnricher(tCorrBF.slotRepE1(), tCorrBF.slotConsE2());
                comandasEnrBF = tEnrBF.enrich();
            } else {
                System.out.println("Se produjo un error en la consulta.");
            }
            //Unimos los mensajes procediente de las bebidas Calientes y el de las frias.
            System.out.println("El taskMerger se esta ejecutando.");
            TaskMerger tMerge = new TaskMerger();
            Slot ComandaMerge = tMerge.Merge(comandasEnrBF, comandasEnrBC);
            //Reconstruimos el mensaje dividido previamente.
            System.out.println("El taskAgreggator se esta ejecutando.");
            TaskAgreggator tAgre = new TaskAgreggator(ComandaMerge);
            Document agregados = tAgre.Aggrega();
            //Leemos el documento final
            System.out.println("El resultado de las consultas es:");
            conectorS.Cargar_Comanda(agregados);
            conectorS.generarSolucion();
            System.out.println("El programa ha finalizado.");
        } catch (TransformerException | XPathExpressionException e) {
        }

    }

}
