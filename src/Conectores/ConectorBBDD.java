package Conectores;

import com.sun.jdi.connect.Connector;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import puertoscafe.Slot;

/**
 *
 * @author Adrian y Jonathan
 */
public class ConectorBBDD {

    private static java.sql.Connection conn;
    private final List<Document> bebidas;
    private final String tipo;

    public ConectorBBDD(List<Document> b, String type) throws InstantiationException, IllegalAccessException, SQLException {
        this.bebidas = b;
        this.tipo = type;
    }

    //Comprueba el stock de una de terminada bebida de un tipo;
    public Slot consultar() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println(" Estableciendo conexion con la base de datos.....");
        System.out.println("Realizar Consulta");
        Slot aux = new Slot();
        conn = DriverManager.getConnection("jdbc:mysql://db4free.net:3306/bdadrian", "bdadrian", "bdadrian1");
        System.out.println("Conexión establecida.");
        String name;
        boolean stock = false;
        NodeList drinks;
        //Statement stmt = conn.createStatement();
        try {
            for (int i = 0; i < bebidas.size(); i++) {
                drinks = bebidas.get(i).getElementsByTagName("name");
                name = drinks.item(0).getTextContent();
                String st = "";
                if (tipo.equals("cold")) {
                    st = "SELECT STOCK FROM BEBIDAS_FRIAS WHERE nombre='" + name + "'";
                } else if (tipo.equals("hot")) {
                    st = "SELECT STOCK FROM BEBIDAS_CALIENTES WHERE nombre='" + name + "'";
                }
                try (PreparedStatement s = conn.prepareStatement(st); ResultSet r = s.executeQuery()) {
                    //Esta en la carta, si no esta en carta no comprueba
                    if (r.next()) {
                        stock = r.getBoolean("stock");
                    }
                }
                //Anadimos campo stock segun valor
                Element raiz = bebidas.get(i).getDocumentElement(); //nodo raíz
                Element keyNode = bebidas.get(i).createElement("stock");
                Text nodeKeyValue;
                if (stock) {
                    nodeKeyValue = bebidas.get(i).createTextNode("true");
                } else {
                    nodeKeyValue = bebidas.get(i).createTextNode("false");
                }
                keyNode.appendChild(nodeKeyValue);
                raiz.appendChild(keyNode);
                aux.introducirComanda(bebidas.get(i));

            }
        } catch (SQLException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
        conn.close();
        System.out.println("Conexión CERRADA \n");
        return aux;
    }
}
