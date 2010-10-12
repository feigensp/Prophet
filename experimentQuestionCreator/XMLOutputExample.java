package experimentQuestionCreator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
/**
 * @author Thomas.Darimont
 * 
 */
public class XMLOutputExample {
 
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Order order = new Order("Thomas Darimont");
        order.add(new LineItem("Salami Pizza", 3.0, 4.99)).add(
                new LineItem("Calzone", 5.0, 2.99)).add(
                new LineItem("Ginger Ale", 10.0, 1.99));
        //      
        // XMLEncoder encoder = new XMLEncoder(new
        // FileOutputStream("c:/order.xml"));
        // encoder.writeObject(order);
        // encoder.close();
 
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().newDocument();
        Element ordersRootElement = document.createElement("Fragebogen");
        document.appendChild(ordersRootElement);
        Element orderElement = document.createElement("order");
        ordersRootElement.appendChild(orderElement);
        orderElement.setTextContent("Testili");
        orderElement.setAttribute("id", String.valueOf(order.getId()));
        orderElement.setAttribute("customer", String.valueOf(order
                .getCustomer()));
        orderElement.setAttribute("date", String.valueOf(order.getDate()));
 
        TransformerFactory.newInstance().newTransformer().transform(
                new DOMSource(document), new StreamResult("test.xml"));
    }
}