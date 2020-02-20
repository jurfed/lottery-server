package controllers;

import entities.ParametersEntity;
import entities.TicketEntity;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;
import org.xml.sax.SAXException;
import parsers.DistributionParser;
import parsers.GamesParser;
import parsers.RandomTicketsParser;
import parsers.TicketsParser;
import ticketfactory.TicketGeneratorRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.stream.Collectors;


public class ServerController {
    private static ServerController serverController;
    private static DBController dBController;
    private static TicketsParser ticketsParser;
    private static RandomTicketsParser randomTicketsParser;
    private static DistributionParser distributionParser;
    private static TicketGeneratorRunner ticketGeneratorRunner;
    private static GamesParser gamesParser;
    private String answer;
    private String webMessage;
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static ServerController getInstance() {
        if (serverController == null) {
            serverController = new ServerController();
            if (!OS.contains("win")) {
                ticketGeneratorRunner = TicketGeneratorRunner.getInstance();
            }

            ticketsParser = TicketsParser.getInstance();
            distributionParser = DistributionParser.getInstance();
            gamesParser = GamesParser.getInstance();
            randomTicketsParser = RandomTicketsParser.getInstance();
        }
        return serverController;
    }

    public void sendAnswer(HttpServletRequest req, HttpServletResponse resp) {
        try {
            dBController = DBController.getInstance();
            Map<String, String[]> parametersMap = req.getParameterMap();
            parametersMap.forEach((s, strings) -> {
                if(!s.equals("bet")){
                    switch (s) {
                        case "getParameters":
                            ParametersEntity parameters = dBController.getParameters();
                            if (parameters != null) {
                                answer = dBController.getParameters().toString();
                            } else {
                                answer = "{\"message\":\"there aren't any parameters\"}";
                            }
                            break;
                        case "getTicketData":
/*                        try {
                            answer = dBController.getTicketData(Integer.parseInt(strings[0]));
                        } catch (Exception e) {
                            answer = "{\"message\":\"Error finding ticket\"}";
                        }
                        break;*/
                            try {
                                answer = dBController.getTicketDataFromMath(Integer.parseInt(strings[0]));
                            } catch (Exception e) {
                                answer = "{\"message\":\"Error finding ticket\"}";
                            }
                            break;
                        case "getBets"://передавать bet
                            try {
                                answer = dBController.getBets(Integer.parseInt(strings[0]));
                            } catch (NumberFormatException e) {
                            }
                            break;
                        case "getGames"://передавать bet
                            try {
                                answer = dBController.getGames();
                            } catch (NumberFormatException e) {
                            }
                            break;
                        case "getPaytableData":
                            try {
                                answer = dBController.getPaytable();
                            } catch (NumberFormatException e) {
                                answer = "{\"message\":\"Error finding paytable data\"}";
                            }
                            break;
                        case "generateTickets":
                            if (!OS.contains("win")) {
                                String ticketsParameters = strings[0];
                                String[] subStr = ticketsParameters.split("/");
                                String gameName = subStr[0];
                                int bet = Integer.parseInt(subStr[1]);
                                int win = Integer.parseInt(subStr[2]);
                                int count = Integer.parseInt(subStr[3]);
                                if(count<=200000){
                                    String tickets = ticketGeneratorRunner.generateTickets(gameName, bet, win, count);
                                    InputStream inputStream = new ByteArrayInputStream(tickets.getBytes(Charset.forName("UTF-8")));
                                    try {
//                                    answer = ticketsParser.parseDataFromMath(inputStream);

                                        answer = dBController.saveMathTicket(inputStream);
                                    } catch (ParserConfigurationException e) {
                                        e.printStackTrace();
                                    } catch (SAXException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    answer = "{\"message\":\"Error - to big tickets count\"}";
                                }
                            }else{
                                answer = "{\"message\":\"OS Windows doesn't supported\"}";
                            }
                            break;
                        case "getRandomTicket":
                            if (!OS.contains("win")) {
                                String ticketsParameters = strings[0];
                                int gameId = Integer.parseInt(ticketsParameters);
                                String ticket = dBController.getRandomTicket(gameId);
                                ByteArrayInputStream inputStream = new ByteArrayInputStream(ticket.getBytes(Charset.forName("UTF-8")));
                                TicketEntity ticketEntity = null;
                                try {
                                    ticketEntity = ticketsParser.getRundomTicket(inputStream);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (SAXException e) {
                                    e.printStackTrace();
                                }
                                if(ticketEntity==null){
                                    answer = "{\"message\":\"Error ticket generation\", \"result\":404 }";
                                }else{
                                    answer = dBController.getMathRandomTicketData(ticketEntity);
                                }

                            }
                            break;

                        case "getGameTicket":
                            if (!OS.contains("win")) {
                                int gameId = Integer.parseInt(parametersMap.get("getGameTicket")[0]);
                                long betId = Long.parseLong(parametersMap.get("bet")[0]);
                                String ticket = dBController.getRandomTicket(gameId, betId);
                                ByteArrayInputStream inputStream = new ByteArrayInputStream(ticket.getBytes(Charset.forName("UTF-8")));
                                TicketEntity ticketEntity = null;
                                try {
                                    ticketEntity = ticketsParser.getRundomTicket(inputStream);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (SAXException e) {
                                    e.printStackTrace();
                                }
                                if(ticketEntity==null){
                                    answer = "{\"message\":\"Error ticket generation\", \"result\":404 }";
                                }else{
                                    answer = dBController.getMathRandomTicketData(ticketEntity);
                                }


                            }
                            break;
                        case "getDebugTicket":
                            String ticketsParameters = strings[0];
                            String [] debugParameters = ticketsParameters.split("/");
                            int gameId = Integer.parseInt(debugParameters[0]);
                            long betId = Long.parseLong(debugParameters[1]);
                            int pize = Integer.parseInt(debugParameters[2]);
                            String ticket = dBController.getRandomTicket(gameId, betId, pize);
                            ByteArrayInputStream inputStream2 = new ByteArrayInputStream(ticket.getBytes(Charset.forName("UTF-8")));
                            TicketEntity ticketEntity = null;
                            try {
                                ticketEntity = ticketsParser.getRundomTicket(inputStream2);

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (SAXException e) {
                                e.printStackTrace();
                            }
                            if (ticketEntity == null) {
                                answer = "{\"message\":\"Error ticket generation\", \"result\":404 }";
                            } else {
                                answer = dBController.getMathRandomTicketData(ticketEntity);
                            }
                            break;
                        case "savePaytableFomMath":
                            if (!OS.contains("win")) {
                                String payTable = ticketGeneratorRunner.getGamesInfo();
                                InputStream inputStream = new ByteArrayInputStream(payTable.getBytes(Charset.forName("UTF-8")));
                                try {
                                    answer = distributionParser.parse(inputStream);
                                    dBController.countAllTickets();
                                } catch (ParserConfigurationException e) {
                                    e.printStackTrace();
                                } catch (SAXException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case "destroyMath":
                            if (!OS.contains("win")) {
                                answer = ticketGeneratorRunner.destroy();
                            }
                            break;
                        default:
                            answer = "{\"message\":\"The request does not contain the necessary parameters\"}";
                            break;
                    }
                }


            });
            try {
                resp.getWriter().write(answer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ExceptionInInitializerError e) {
            try {
                resp.getWriter().write("{\"message\":\"No connection to data base\"}");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (JDBCConnectionException f) {
            answer = "{\"message\":\"No connection to data base\"}";
            try {
                resp.getWriter().write(answer);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (SQLGrammarException f) {
            webMessage = "Error: There aren't required tables in the database.";
            answer = "{\"message\":\"There aren't required tables in the database.\"}";
            try {
                resp.getWriter().write(answer);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            webMessage = "Error: Check database connection.";
            answer = "{\"message\":\"Check database connection.\"}";
            try {
                resp.getWriter().write(answer);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    public void showUserParameters(HttpServletRequest req, HttpServletResponse resp) {
        try {
            dBController = DBController.getInstance();
            ParametersEntity userParameters = dBController.getParameters();
            req.setAttribute("currentBalance", userParameters.getCurrentBalance());
            req.setAttribute("maxWin", userParameters.getMaxWin());
            req.setAttribute("currencyCode", userParameters.getCurrencyCode());
            req.setAttribute("soundValue", userParameters.getSoundValue());
            req.setAttribute("languageCode", userParameters.getLanguageCode());
            req.setAttribute("currentBet", userParameters.getCurrentBet());
            req.setAttribute("currentGameId", userParameters.getCurrentGame());
        } catch (ExceptionInInitializerError e) {
            req.setAttribute("message", "Error: No connection to data base");
        } catch (JDBCConnectionException f) {
            req.setAttribute("message", "Error: No connection to data base");
        } catch (SQLGrammarException f) {
            req.setAttribute("message", "Error: There aren't required tables in the database.");
        }
    }

    public void saveData(HttpServletRequest req, HttpServletResponse resp) {
        try {
            dBController = DBController.getInstance();

            Map<String, String[]> parameters = req.getParameterMap();
            if (parameters.size() > 0) {
                webMessage = "Parameters were successfully saved.";
                answer = "{\"message\":\"Parameters were successfully saved.\"}";
                parameters.forEach((s, strings) -> {
                    switch (s) {
                        case "createdb":
                            try {
                                dBController.createBD();
                                answer = "{\"message\":\"DB was successfully created\"}";
                            } catch (NumberFormatException e) {
                                answer = "{\"message\":\"Error creating db.\"}";
                                break;
                            }
                            break;

                        case "currentBalance":
                            try {
                                dBController.setParameterCurrentBalance(Long.parseLong(strings[0]));
                            } catch (NumberFormatException e) {
                                webMessage = "Error: wrong values.";
                                answer = "{\"message\":\"Error: wrong values.\"}";
                                break;
                            }
                            break;
                        case "maxWin":
                            try {
                                dBController.setParameterMaxWin(Long.parseLong(strings[0]));
                            } catch (NumberFormatException e) {
                                webMessage = "Error: wrong values.";
                                answer = "{\"message\":\"Error: wrong values.\"}";
                                break;
                            }
                            break;
                        case "currencyCode":
                            dBController.setParameterCurrencyCode(strings[0]);
                            break;
                        case "soundValue":
                            try {
                                dBController.setParameterSoundValue(Double.parseDouble(strings[0]));
                            } catch (NumberFormatException e) {
                                webMessage = "Error: wrong values.";
                                answer = "{\"message\":\"Error: wrong values.\"}";
                                break;
                            }
                            break;
                        case "languageCode":
                            dBController.setParameterLanguageCode(strings[0]);
                            break;
                        case "currentBet":
                            try {
                                dBController.setParameterCurrentBet(Long.parseLong(strings[0]));
                            } catch (NumberFormatException e) {
                                webMessage = "Error: wrong values.";
                                answer = "{\"message\":\"Error: wrong values.\"}";
                                break;
                            }
                            break;
                        case "currentGame":
                            try {
                                dBController.setParameterCurrentGame(Integer.parseInt(strings[0]));
                            } catch (NumberFormatException e) {
                                webMessage = "Error: wrong values.";
                                answer = "{\"message\":\"Error: wrong values.\"}";
                                break;
                            }
                            break;
                        case "deleteTickets":
                            dBController.deleteTickets();
                            answer = "{\"message\":\"All Tickets where successfully deleted\"}";
                            break;
                        case "deleteDistributions":
                            dBController.deleteDistributions();
                            answer = "{\"message\":\"All Distributions where successfully deleted\"}";
                            break;
                        case "deleteGames":
                            dBController.deleteGames();
                            answer = "{\"message\":\"All Games with Tickets and Distributions where successfully deleted\"}";
                            break;
                        case "parseTickets":
                            try {
                                answer = ticketsParser.parse();
                            } catch (ParserConfigurationException e) {
                                e.printStackTrace();
                            } catch (SAXException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                        answer = "{\"message\":\"All Tickets where successfully deleted\"}";
                            break;
                        case "parseDistributions":
                            try {
                                answer = distributionParser.parse();
                            } catch (ParserConfigurationException e) {
                                e.printStackTrace();
                            } catch (SAXException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "parseGames":
                            try {
                                answer = gamesParser.parse();
                            } catch (ParserConfigurationException e) {
                                e.printStackTrace();
                            } catch (SAXException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "sendTickets":
                            try {
                                InputStream inputStream = new ByteArrayInputStream(strings[0].getBytes(Charset.forName("UTF-8")));
                                answer = ticketsParser.parse(inputStream);
                            } catch (ParserConfigurationException e) {
                                e.printStackTrace();
                            } catch (SAXException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "sendGames":
                            try {
                                InputStream inputStream = new ByteArrayInputStream(strings[0].getBytes(Charset.forName("UTF-8")));
                                answer = gamesParser.parse(inputStream);
                            } catch (ParserConfigurationException e) {
                                e.printStackTrace();
                            } catch (SAXException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "sendDistributions":
                            try {
                                InputStream inputStream = new ByteArrayInputStream(strings[0].getBytes(Charset.forName("UTF-8")));
                                answer = distributionParser.parse(inputStream);
                            } catch (ParserConfigurationException e) {
                                e.printStackTrace();
                            } catch (SAXException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;

                        default:
                            answer = "{\"message\":\"The request does not contain the necessary parameters\"}";
                            break;
                    }
                });
            } else if (req.getHeader("Content-Type").equals("multipart/form-data")) {
                String test = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//                String test2 =test.replaceAll(".*\"tickets.xml\"","");


                String s = "[tag]jajasljalfjasljldjdasld[/tag][tag12]afalejljldjlefe[/tag12]";
                int startPosition = test.indexOf("<tickets>") + "<tickets>".length();
                int endPosition = test.indexOf("</tickets>", startPosition);
                String subS = "<tickets>" + test.substring(startPosition, endPosition) + "</tickets>";


                try {
                    InputStream inputStream = new ByteArrayInputStream(subS.getBytes(Charset.forName("UTF-8")));
                    answer = ticketsParser.parse(inputStream);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (req.getHeader("Content-Type").equals("multipart/form-data2")) {
                String test = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                String s = "[tag]jajasljalfjasljldjdasld[/tag][tag12]afalejljldjlefe[/tag12]";
                int startPosition = test.indexOf("<prizeDistributions>") + "<prizeDistributions>".length();
                int endPosition = test.indexOf("</prizeDistributions>", startPosition);
                String subS = "<prizeDistributions>" + test.substring(startPosition, endPosition) + "</prizeDistributions>";

                try {
                    InputStream inputStream = new ByteArrayInputStream(subS.getBytes(Charset.forName("UTF-8")));
                    answer = distributionParser.parse(inputStream);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (req.getHeader("Content-Type").equals("multipart/form-data3")) {
                String test = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//                String test2 =test.replaceAll(".*\"tickets.xml\"","");

                String s = "[tag]jajasljalfjasljldjdasld[/tag][tag12]afalejljldjlefe[/tag12]";
                int startPosition = test.indexOf("<games>") + "<games>".length();
                int endPosition = test.indexOf("</games>", startPosition);
                String subS = "<games>" + test.substring(startPosition, endPosition) + "</games>";

                try {
                    InputStream inputStream = new ByteArrayInputStream(subS.getBytes(Charset.forName("UTF-8")));
                    answer = gamesParser.parse(inputStream);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                System.out.println();
            } else if (req.getHeader("Content-Type").equals("multipart/form-data4")) {
                String test = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//                String test2 =test.replaceAll(".*\"tickets.xml\"","");

                String s = "[tag]jajasljalfjasljldjdasld[/tag][tag12]afalejljldjlefe[/tag12]";
                int startPosition = test.indexOf("<games>") + "<games>".length();
                int endPosition = test.indexOf("</games>", startPosition);
                String subS = "<games>" + test.substring(startPosition, endPosition) + "</games>";

                try {
                    InputStream inputStream = new ByteArrayInputStream(subS.getBytes(Charset.forName("UTF-8")));
                    answer = gamesParser.parse(inputStream);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                System.out.println();
            }else {
                answer = "{\"message\":\"The request does not contain any parameters\"}";
            }
            try {
                resp.getWriter().write(answer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ExceptionInInitializerError e) {
            try {
                webMessage = "Error: No connection to data base";
                resp.getWriter().write("{\"message\":\"No connection to data base\"}");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (JDBCConnectionException f) {
            webMessage = "Error: No connection to data base";
            answer = "{\"message\":\"No connection to data base\"}";
            try {
                resp.getWriter().write(answer);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (SQLGrammarException f) {
            webMessage = "Error: There aren't required tables in the database.";
            answer = "{\"message\":\"There aren't required tables in the database.\"}";
            try {
                resp.getWriter().write(answer);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            webMessage = "Error: Check database connection.";
            answer = "{\"message\":\"Check database connection.\"}";
            try {
                resp.getWriter().write(answer);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public String getWebMessage() {
        return webMessage;
    }
}
