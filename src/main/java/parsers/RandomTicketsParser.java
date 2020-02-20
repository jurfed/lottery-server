package parsers;

import controllers.DBController;
import entities.TicketEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RandomTicketsParser {
    private static Map<Integer, List<TicketEntity>> mapWithTickets;
    private static RandomTicketsParser ticketsParser;
    private static String OS = System.getProperty("os.name").toLowerCase();
    List<TicketEntity> tickets;
    DBController dbController;

    private RandomTicketsParser() {
    }

    public static RandomTicketsParser getInstance() {
        if (ticketsParser == null) {
            ticketsParser = new RandomTicketsParser();
        }
        return ticketsParser;
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
        NodeList nList = document.getElementsByTagName("game");
        System.out.println("----------------------------");

        for (int games = 0; games < nList.getLength(); games++) {

            NodeList nList2 = document.getElementsByTagName("bet");

            for (int bets = 0; bets < nList.getLength(); bets++) {

            }


            Node nNode = nList.item(games);
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


}
