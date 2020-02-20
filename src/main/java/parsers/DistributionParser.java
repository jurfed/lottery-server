package parsers;

import controllers.DBController;
import entities.PrizeDistributionEntity;
import entities.QuantumPrizesEntity;
import entities.RecordEntity;
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

public class DistributionParser {
    private static Map<Integer, List<PrizeDistributionEntity>> integerListMap;
    private static DistributionParser distributionParser;
    private DBController dbController;
    private List<PrizeDistributionEntity> prizeDistributionEntities;
    private static String OS = System.getProperty("os.name").toLowerCase();
    private DistributionParser() {
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        DistributionParser gamesParser = new DistributionParser();
        gamesParser.parse();
    }

    public static DistributionParser getInstance() {
        if (distributionParser == null) {
            distributionParser = new DistributionParser();
        }
        return distributionParser;
    }

    public String parse() throws ParserConfigurationException, SAXException, IOException {
        String path = this.getClass().getClassLoader().getResource("data").getPath();
        File fXmlFile;
        if(OS.contains("win")){
            fXmlFile = new File(path + "\\prize_distributions.xml");
        }else{
            fXmlFile = new File(path + "prize_distributions.xml");
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        return parseData(doc);
    }

    public String parse(InputStream data) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(data);
        doc.getDocumentElement().normalize();
        return parseData(doc);
    }

    public String parseFromMath(InputStream data) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(data);
        doc.getDocumentElement().normalize();
        return parseData(doc);
    }

    int addedPrizes = 0;
    public String parseData(Document document) {
        prizeDistributionEntities = new ArrayList<>();
        integerListMap = new HashMap<>();
        dbController = DBController.getInstance();
        System.out.println("Root element :" + document.getDocumentElement().getNodeName());
        NodeList nList = document.getElementsByTagName("prizeDistribution");
        System.out.println("----------------------------");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                int gameId = Integer.parseInt(eElement.getElementsByTagName("gameId").item(0).getTextContent());
                Long bet = Long.parseLong(eElement.getElementsByTagName("bet").item(0).getTextContent());
                Long max_prize = Long.parseLong(eElement.getElementsByTagName("max_prize").item(0).getTextContent());
                Double max_prize_hit = Double.parseDouble(eElement.getElementsByTagName("max_prize_hit").item(0).getTextContent());
                Double any_prize_hit = Double.parseDouble(eElement.getElementsByTagName("any_prize_hit").item(0).getTextContent());
                Long total_payout = Long.parseLong(eElement.getElementsByTagName("total_payout").item(0).getTextContent());
                Integer winning_tickets = Integer.parseInt(eElement.getElementsByTagName("winning_tickets").item(0).getTextContent());
                Integer total_tickets = Integer.parseInt(eElement.getElementsByTagName("total_tickets").item(0).getTextContent());
                List<RecordEntity> recordEntityList = new ArrayList<>();
                //records parser
                NodeList recordsList = eElement.getElementsByTagName("record");
                for (int temp2 = 0; temp2 < recordsList.getLength(); temp2++) {
                    Node nNode2 = recordsList.item(temp2);
                    if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                        Element recordElement = (Element) nNode2;
                        Long prize = Long.parseLong(recordElement.getElementsByTagName("prize").item(0).getTextContent());
                        int tickets = Integer.parseInt(recordElement.getElementsByTagName("tickets").item(0).getTextContent());
                        recordEntityList.add(new RecordEntity(prize, tickets));
                    }
                }
                //quantum prizes
                NodeList quantumsList = eElement.getElementsByTagName("value");
                List<QuantumPrizesEntity> quantumPrizesEntities = new ArrayList<>();
                if (quantumsList.getLength() > 0) {
                    for (int i = 0; i < quantumsList.getLength(); i++) {
                        quantumPrizesEntities.add(new QuantumPrizesEntity(Long.parseLong(quantumsList.item(i).getTextContent())));
                    }

                }
                prizeDistributionEntities.add(new PrizeDistributionEntity(bet, total_tickets, quantumPrizesEntities,
                        recordEntityList, max_prize_hit, any_prize_hit, winning_tickets, max_prize, total_payout, gameId));
            }
        }

        addedPrizes = 0;

        integerListMap = prizeDistributionEntities.stream().collect(Collectors.groupingBy(PrizeDistributionEntity::getGameId));
        integerListMap.forEach((integer, prizeDistributionEntities1) -> {
            addedPrizes += dbController.addPrizes(integer, prizeDistributionEntities1);
        });

        String incorrect = "";
        if(addedPrizes < prizeDistributionEntities.size()){
            incorrect = " Please check that all your prize distributions have the correct game index.";
        }

        return "{\"message\":\"Where added " + addedPrizes+" prize distributions out of "+ prizeDistributionEntities.size() + "."+ incorrect +"\"}";
    }

}
