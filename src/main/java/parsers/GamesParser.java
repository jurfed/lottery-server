package parsers;

import controllers.DBController;
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
import java.util.HashSet;
import java.util.Set;

public class GamesParser {
    private static GamesParser gamesParser;
    Set<Integer> gamesIds;
    DBController dbController;
    private static String OS = System.getProperty("os.name").toLowerCase();

    private GamesParser() {
    }

    public static GamesParser getInstance() {
        if (gamesParser == null) {
            gamesParser = new GamesParser();
        }
        return gamesParser;
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        GamesParser gamesParser = new GamesParser();
        gamesParser.parse();
    }

    public String parse() throws ParserConfigurationException, SAXException, IOException {
        gamesIds = new HashSet<>();
        dbController = DBController.getInstance();
        String path = this.getClass().getClassLoader().getResource("data").getPath();
        File fXmlFile;
        if(OS.contains("win")){
            fXmlFile = new File(path + "\\games.xml");
        }else{
            fXmlFile = new File(path + "games.xml");
        }
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        return parseData(doc);
    }

    public String parse(InputStream data) throws ParserConfigurationException, SAXException, IOException {
        gamesIds = new HashSet<>();
        dbController = DBController.getInstance();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(data);
        doc.getDocumentElement().normalize();
        return parseData(doc);
    }

    public String parseData(Document document) throws ParserConfigurationException, SAXException, IOException {
        gamesIds = new HashSet<>();
        System.out.println("Root element :" + document.getDocumentElement().getNodeName());
        NodeList nList = document.getElementsByTagName("game");
        System.out.println("----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                int gameId = Integer.parseInt(eElement.getElementsByTagName("gameId").item(0).getTextContent());
                String gameName = eElement.getElementsByTagName("gameName").item(0).getTextContent();
                gamesIds.add(gameId);
                dbController.addGame(gameId, gameName);
            }
        }
        return "{\"message\":\"Where added " + gamesIds.size() + " Games\"}";
    }

}
