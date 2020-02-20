package parsers;

import controllers.DBController;
import entities.TicketEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class TicketsParser {
    private static Map<Integer, List<TicketEntity>> mapWithTickets;
    private static TicketsParser ticketsParser;
    private static String OS = System.getProperty("os.name").toLowerCase();
    List<TicketEntity> tickets;
    DBController dbController;

    private TicketsParser() {
    }

    public static TicketsParser getInstance() {
        if (ticketsParser == null) {
            ticketsParser = new TicketsParser();
        }
        return ticketsParser;
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        TicketsParser ticketsParser = new TicketsParser();
        ticketsParser.parse();
    }

    public String parse() throws ParserConfigurationException, SAXException, IOException {
        tickets = new ArrayList<>();
        mapWithTickets = new HashMap<>();
        String path = this.getClass().getClassLoader().getResource("data").getPath();
        File fXmlFile;
        if (OS.contains("win")) {
            fXmlFile = new File(path + "\\tickets.xml");
        } else {
            fXmlFile = new File(path + "tickets.xml");
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        return parseData(doc);
    }


    public TicketEntity getRundomTicket(InputStream data) throws IOException, SAXException {
        tickets = new ArrayList<>();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document document = dBuilder.parse(data);

        document.getDocumentElement().normalize();
        System.out.println("Root element :" + document.getDocumentElement().getNodeName());
        NodeList nList = document.getElementsByTagName("ticket");
        System.out.println("----------------------------");

        TicketEntity ticketEntity = null;
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                int gameId = Integer.parseInt(eElement.getElementsByTagName("gameId").item(0).getTextContent());
                Long price = Long.parseLong(eElement.getElementsByTagName("price").item(0).getTextContent());
                Long win = Long.parseLong(eElement.getElementsByTagName("win").item(0).getTextContent());
                String context = eElement.getElementsByTagName("context").item(0).getTextContent();
                ticketEntity = new TicketEntity(price, win, context, 0, gameId);
            }
        }
        return ticketEntity;
    }

    public String parse(InputStream data) throws ParserConfigurationException, SAXException, IOException {
        tickets = new ArrayList<>();
        mapWithTickets = new HashMap<>();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(data);
        return parseData(doc);
    }

    public String parseData(Document document) {

        tickets = new ArrayList<>();
        mapWithTickets = new HashMap<>();
        document.getDocumentElement().normalize();
        System.out.println("Root element :" + document.getDocumentElement().getNodeName());
        NodeList nList = document.getElementsByTagName("ticket");
        System.out.println("----------------------------");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                Integer gameId = Integer.parseInt(eElement.getElementsByTagName("gameId").item(0).getTextContent());
                Long price = Long.parseLong(eElement.getElementsByTagName("price").item(0).getTextContent());

                Node item = eElement.getElementsByTagName("win").item(0);
                if (item != null) {
                    Long win = Long.parseLong(eElement.getElementsByTagName("win").item(0).getTextContent());
                    String context = eElement.getElementsByTagName("context").item(0).getTextContent();
                    if (gameId != null && price != null && win != null && context != null) {
                        TicketEntity ticketEntity = new TicketEntity(price, win, context, 0, gameId);
                        tickets.add(ticketEntity);
                    }
                }

            }
        }


        if (tickets.size() > 0) {
            dbController = DBController.getInstance();
            mapWithTickets = tickets.stream().collect(Collectors.groupingBy(TicketEntity::getGameId));
            int ticketsCount = dbController.addTickets(mapWithTickets);
            String incorrect = "";
            if (ticketsCount < tickets.size()) {
                incorrect = "Please check that all your tickets have the correct game index.";
            }
            return "{\"message\":\"Where added " + ticketsCount + " tickets out of " + tickets.size() + ". " + incorrect + "\"}";
        } else {
            return "{\"message\":\"Wrong parameters for ticket\"}";
        }


    }

    public String parseDataFromMath(InputStream data) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(data);

        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("ticket");
        System.out.println("----------------------------");

        int idx = 1;



        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                Integer gameId = Integer.parseInt(eElement.getElementsByTagName("gameId").item(0).getTextContent());
                Long price = Long.parseLong(eElement.getElementsByTagName("price").item(0).getTextContent());

                Node item = eElement.getElementsByTagName("win").item(0);
                if (item != null) {
                    Long win = Long.parseLong(eElement.getElementsByTagName("win").item(0).getTextContent());
                    String context = eElement.getElementsByTagName("context").item(0).getTextContent();
                    if (gameId != null && price != null && win != null && context != null) {
                        String  ticketSql = "insert into ticket(idx,gameId,price,win,context,played) values(" + idx + ", "+ gameId+", " +price+ ", " + win+", '"+ context +"', "+ 0 +")";
                        dbController = DBController.getInstance();
//                        dbController.saveMathTicket(ticketSql);
                        idx++;
                    }
                }

            }

        }
        return "{\"message\":\"Where added " + (idx-1) +  "\"}";


    }

}
