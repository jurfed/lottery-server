package controllers;

import entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.Query;
import org.hibernate.service.spi.ServiceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ticketfactory.TicketGeneratorRunner;

import javax.persistence.PersistenceException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DBController {
    private static SessionFactory ourSessionFactory;
    private static DBController dBController;
    private static Session session;
    private static Configuration configuration;

    static {
        try {
            configuration = new Configuration();
            configuration.configure();

        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private ParametersEntity parameters;


    private DBController() {
    }

    public static DBController getInstance() {
        try {
            if (ourSessionFactory == null) {
                ourSessionFactory = configuration.buildSessionFactory();
            }
        } catch (ServiceException e) {
            System.err.println("no connection");

        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
        if (dBController == null) {
            dBController = new DBController();
        }
        return dBController;
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public void createBD() {
        session = getSession();
        Transaction tx = session.beginTransaction();
        try {
            FileInputStream fis = new FileInputStream(this.getClass().getClassLoader().getResource("schema.sql").getFile());
            try (BufferedReader br =
                         new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8.name()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                Query query = session.createSQLQuery(sb.toString());
                query.executeUpdate();
            } catch (JDBCConnectionException f) {
            } catch (SQLGrammarException f) {
            } finally {
                fis.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tx.commit();
        session.close();
    }

    public void setParameters(long currentBalance, long maxWin, String currencyCode, double soundValue, String languageCode, Long currentBet, int currentGame) {
        session = getSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from ParametersEntity");
        if (query.iterate().hasNext()) {
            parameters = (ParametersEntity) query.iterate().next();
            parameters.setCurrentBalance(currentBalance);
            parameters.setMaxWin(maxWin);
            parameters.setCurrencyCode(currencyCode);
            parameters.setSoundValue(soundValue);
            parameters.setLanguageCode(languageCode);
            parameters.setCurrentBet(currentBet);
            parameters.setCurrentGame(currentGame);
            session.update(parameters);
        } else {
            parameters = new ParametersEntity(currentBalance, maxWin, currencyCode, soundValue, languageCode, currentBet, currentGame);
            session.save(parameters);
        }
        tx.commit();
        session.close();
    }

    public void setParameterCurrentBalance(long currentBalance) {
        session = getSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from ParametersEntity");
        if (query.iterate().hasNext()) {
            parameters = (ParametersEntity) query.iterate().next();
            parameters.setCurrentBalance(currentBalance);

            session.update(parameters);
        } else {
            parameters = new ParametersEntity(currentBalance, 2000000, "EU", 0.5, "EN", 100L, 11);
            session.save(parameters);
        }
        tx.commit();
        session.close();
    }

    public void setParameterMaxWin(long maxWin) {
        session = getSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from ParametersEntity");
        if (query.iterate().hasNext()) {
            parameters = (ParametersEntity) query.iterate().next();
            parameters.setMaxWin(maxWin);

            session.update(parameters);
        } else {
            parameters = new ParametersEntity(0, maxWin, "EU", 0.5, "EN", 100L, 11);
            session.save(parameters);
        }
        tx.commit();
        session.close();
    }


    public void setParameterCurrencyCode(String currencyCode) {
        session = getSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from ParametersEntity");
        if (query.iterate().hasNext()) {
            parameters = (ParametersEntity) query.iterate().next();
            parameters.setCurrencyCode(currencyCode);

            session.update(parameters);
        } else {
            parameters = new ParametersEntity(0, 2000000, currencyCode, 0.5, "EN", 100L, 11);
            session.save(parameters);
        }
        tx.commit();
        session.close();
    }

    public void setParameterSoundValue(Double soundValue) {
        session = getSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from ParametersEntity");
        if (query.iterate().hasNext()) {
            parameters = (ParametersEntity) query.iterate().next();
            parameters.setSoundValue(soundValue);

            session.update(parameters);
        } else {
            parameters = new ParametersEntity(0, 2000000, "EU", soundValue, "EN", 100L, 11);
            session.save(parameters);
        }
        tx.commit();
        session.close();
    }

    public void setParameterLanguageCode(String languageCode) {
        session = getSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from ParametersEntity");
        if (query.iterate().hasNext()) {
            parameters = (ParametersEntity) query.iterate().next();
            parameters.setLanguageCode(languageCode);
            session.update(parameters);
        } else {
            parameters = new ParametersEntity(0, 2000000, "EU", 0.5, languageCode, 100L, 11);
            session.save(parameters);
        }
        tx.commit();
        session.close();
    }

    public void setParameterCurrentBet(Long currentBet) {
        session = getSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from ParametersEntity");
        if (query.iterate().hasNext()) {
            parameters = (ParametersEntity) query.iterate().next();
            parameters.setCurrentBet(currentBet);
            session.update(parameters);
        } else {
            parameters = new ParametersEntity(0, 2000000, "EU", 0.5, "EN", currentBet, 11);
            session.save(parameters);
        }
        tx.commit();
        session.close();
    }

    public void setParameterCurrentGame(int currentGame) {
        session = getSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from ParametersEntity");
        if (query.iterate().hasNext()) {
            parameters = (ParametersEntity) query.iterate().next();
            parameters.setCurrentGame(currentGame);
            session.update(parameters);
        } else {
            parameters = new ParametersEntity(0, 2000000, "EU", 0.5, "EN", 100L, currentGame);
            session.save(parameters);
        }
        tx.commit();
        session.close();
    }


    public ParametersEntity getParameters() {
        session = getSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from ParametersEntity");

        if (query.iterate().hasNext()) {
            parameters = (ParametersEntity) query.list().get(0);
        }


        query = session.createQuery("from GameEntity where gameId =:id");
        query.setParameter("id", parameters.getCurrentGame());
        if (query.iterate().hasNext()) {

            String getBetsQuery = "select bet from prizedistribution where gameid=" + parameters.getCurrentGame();
            query = session.createSQLQuery(getBetsQuery);

            List<Long> bets = new ArrayList<>();
            query.list().forEach(o -> bets.add(Long.parseLong(o.toString())));
            parameters.setAllBets(bets);
        }
        tx.commit();
        session.close();
        return parameters;
    }

    public ParametersEntity getParameters2() {
        session = getSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from ParametersEntity");

        if (query.iterate().hasNext()) {
            parameters = (ParametersEntity) query.list().get(0);
        }

        tx.commit();
        session.close();
        return parameters;
    }

    public void addGame(int gameId, String gameName) {
        session = getSession();
        Transaction tx = session.beginTransaction();

        GameEntity gameEntity = new GameEntity(gameId, gameName);
        session.saveOrUpdate(gameEntity);
        tx.commit();
        session.close();
    }

    public GameEntity getGame(int id) {
        session = getSession();
        Transaction tx = session.beginTransaction();
        GameEntity gameEntity = null;

        Query query = session.createQuery("from GameEntity where gameId =:id");
        query.setParameter("id", id);
        if (query.iterate().hasNext()) {
            gameEntity = (GameEntity) query.iterate().next();
        }
        tx.commit();
        session.close();
        return gameEntity;

    }

    public void addAllTickets(List<TicketEntity> ticketEntities) {
        session = getSession();
        Transaction tx = session.beginTransaction();
        GameEntity gameEntity = null;

        ticketEntities.forEach(ticketEntity -> {
            session.saveOrUpdate(ticketEntity);
        });

        tx.commit();
        session.close();
    }

    public void addTickets(int gameId, List<TicketEntity> ticketEntities) {
        session = getSession();
        Transaction tx = session.beginTransaction();
        GameEntity gameEntity = null;

        Query query = session.createQuery("from GameEntity where gameId =:gameId");
        query.setParameter("gameId", gameId);
        if (query.iterate().hasNext()) {
            gameEntity = (GameEntity) query.iterate().next();
            gameEntity.setTickets(ticketEntities);
            session.update(gameEntity);
        }
        tx.commit();
        session.close();
    }

    int ticketsCount = 0;

    public int addTickets(Map<Integer, List<TicketEntity>> integerListMap) {
        ticketsCount = 0;
        session = getSession();
        Transaction tx = session.beginTransaction();

        integerListMap.forEach((integer, ticketEntities) -> {
            Query query = session.createQuery("from GameEntity where gameId =:gameId");
            query.setParameter("gameId", integer);
            GameEntity gameEntity = null;
            if (query.iterate().hasNext()) {
                gameEntity = (GameEntity) query.iterate().next();
//                List<TicketEntity> newList = new ArrayList<>();
//                newList.addAll(gameEntity.getTickets());
//                newList.addAll(ticketEntities);
                List<TicketEntity> oldTickets = gameEntity.getTickets();
                if (oldTickets != null) {
                    oldTickets.addAll(ticketEntities);
                } else {
                    gameEntity.setTickets(ticketEntities);
                }
                session.update(gameEntity);
                ticketsCount += ticketEntities.size();
            }
        });

        tx.commit();
        session.close();
        return ticketsCount;
    }

    public List<TicketEntity> getTickets(int id) {
        session = getSession();
        Transaction tx = session.beginTransaction();
        GameEntity gameEntity = null;
        List<TicketEntity> tickets = null;

        Query query = session.createQuery("from GameEntity where gameId =:id");
        query.setParameter("id", id);
        if (query.iterate().hasNext()) {
            gameEntity = (GameEntity) query.iterate().next();
            tickets = gameEntity.getTickets();
        }
        tx.commit();
        session.close();
        return tickets;
    }

    public String getTicketData(int id) {
        String jsonAnswer = "";
        session = getSession();
        Transaction tx = session.beginTransaction();
        Query queryParameters = session.createQuery("from ParametersEntity");
        ParametersEntity parametersEntity = null;
        Long currentBet = 0L;
        if (queryParameters.iterate().hasNext()) {
            parametersEntity = (ParametersEntity) queryParameters.iterate().next();
            currentBet = parametersEntity.getCurrentBet();
        }

        Query query = session.createQuery("from TicketEntity where gameId =:id and price=:bet");
        query.setParameter("id", id);
        query.setParameter("bet", currentBet);
        List ticketList = query.list();
        int tickets = query.list().size();
        if (tickets > 0) {
            Random rnd = new Random(System.currentTimeMillis());
            int number = rnd.nextInt(tickets);
            TicketEntity ticketNumber = (TicketEntity) ticketList.get(number);
            ticketNumber.setPlayed(1);
            session.update(ticketNumber);
            Long currentBalance = parametersEntity.getCurrentBalance() - ticketNumber.getPrice() + ticketNumber.getWin();
            ticketNumber.setCurrentBalance(currentBalance);
            jsonAnswer = ticketNumber.toString();
            parametersEntity.setCurrentBalance(currentBalance);
            session.update(parametersEntity);
        } else {
            jsonAnswer = "{\"message\":\"There aren't tickets for gameid=" + id + " or there isn't such game.\"}";
        }

        tx.commit();
        session.close();
        return jsonAnswer;
    }

    public String getTicketDataFromMath(int id) {
        String jsonAnswer = "";
        session = getSession();
        Transaction tx = session.beginTransaction();
        Query query;

        int ticketId;

        String hql = " with getPrice as(" +
                "select currentbet from parameters" +
                ")SELECT *" +
                "  FROM ticket" +
                "  where random() < 0.01 and gameid = " + id + " and price = (select currentbet from getPrice)" +
//                "  ORDER BY random()" +
                "  LIMIT 1;";

        query = session.createSQLQuery(hql);
        Long newBalance = 0L;

        query.getResultList().get(0);
        List rows = query.list();
        if (rows != null && rows.size() > 0) {
            Object[] row = (Object[]) rows.get(0);
            Long bet = Long.parseLong(row[3].toString());
            Long win = Long.parseLong(row[4].toString());
            int played = Integer.parseInt(row[6].toString());
            ticketId = Integer.parseInt(row[0].toString());
            String contex = row[5].toString();
            TicketEntity ticketEntity = new TicketEntity(bet, win, contex, played, id);

            hql = "select currentbalance from parameters";
            query = session.createSQLQuery(hql);

            List balanceList = query.getResultList();
            if (balanceList != null && balanceList.size() > 0) {
                newBalance = Long.parseLong(balanceList.get(0).toString()) - bet + win;
                hql = "update parameters set currentbalance = " + newBalance + ";";
                query = session.createSQLQuery(hql);
                ticketEntity.setCurrentBalance(newBalance);
                jsonAnswer = ticketEntity.toString();
                query.executeUpdate();

                hql = "update ticket set played = " + 1 + "where ticketid = " + ticketId + ";";
                query = session.createSQLQuery(hql);
                query.executeUpdate();
            }


        } else {
            jsonAnswer = "{\"message\":\"There aren't tickets for gameid=" + id + " or there isn't such game.\"}";
        }
        tx.commit();
        session.close();


        return jsonAnswer;
    }

    public String getMathRandomTicketData(TicketEntity ticketEntity) {
        String jsonAnswer = "";
        session = getSession();
        Transaction tx = session.beginTransaction();
        Query queryParameters = session.createQuery("from ParametersEntity");
        ParametersEntity parametersEntity = null;
        if (queryParameters.iterate().hasNext()) {
            parametersEntity = (ParametersEntity) queryParameters.iterate().next();
        }
        Long currentBalance = parametersEntity.getCurrentBalance() - ticketEntity.getPrice() + ticketEntity.getWin();
        ticketEntity.setCurrentBalance(currentBalance);
        jsonAnswer = ticketEntity.toString();
        parametersEntity.setCurrentBalance(currentBalance);
        session.update(parametersEntity);

        tx.commit();
        session.close();
        return jsonAnswer;
    }

    public void updateTicketStatus(int ticketId, int played) {
        session = getSession();
        Transaction tx = session.beginTransaction();
        TicketEntity ticketEntity = null;

        Query query = session.createQuery("from TicketEntity where ticketId =:ticketId");
        query.setParameter("ticketId", ticketId);
        if (query.iterate().hasNext()) {
            ticketEntity = (TicketEntity) query.iterate().next();
            ticketEntity.setPlayed(played);
            session.update(ticketEntity);
        }
        tx.commit();
        session.close();
    }


    String getBets(int id) {
        String betsMessage = "";
        session = getSession();
        Transaction tx = session.beginTransaction();
        GameEntity gameEntity = null;
        List<PrizeDistributionEntity> prizeDistributionEntities = null;
        Query query = session.createQuery("from GameEntity where gameId =:id");
        query.setParameter("id", id);
        if (query.iterate().hasNext()) {
            gameEntity = (GameEntity) query.iterate().next();
            prizeDistributionEntities = gameEntity.getPrizes();

            if (prizeDistributionEntities.size() > 0) {
                betsMessage = "{\n\"bets\":[";
                int betsSize = prizeDistributionEntities.size();
                for (int i = 0; i < betsSize; i++) {
                    betsMessage += "\"" + prizeDistributionEntities.get(i).getBet() + "\"";
                    if (i < betsSize - 1) {
                        betsMessage += ",";
                    }
                }
                prizeDistributionEntities.forEach(prizeDistributionEntity -> {

                });
                betsMessage += "]\n}";
            }
        } else {
            betsMessage = "{\"message\":\"There aren't any bets for this game\"}";
        }
        tx.commit();
        session.close();
        return betsMessage;
    }

    public String getGames() {
        String message = "";
        session = getSession();
        Transaction tx = session.beginTransaction();

        List<GameEntity> games = (List<GameEntity>) session.createQuery("from GameEntity").list();
        message += "{\"gameNames\":[";
        for (int i = 0; i < games.size(); i++) {
/*            message+="\""+games.get(i).getGameName()+"\"";
            if(i<games.size()-1){
                message+=",";
            }*/
            message += games.get(i).toString();
        }


        message += "]}";
        tx.commit();
        session.close();
        return message;
    }

    public String getPaytable() {
        String message = "";
        session = getSession();
        Transaction tx = session.beginTransaction();
        PrizeDistributionEntity prizeDistributionEntity = null;

        Query queryParameters = session.createQuery("from ParametersEntity");
        ParametersEntity parametersEntity = null;
        int gameId = 0;
        if (queryParameters.iterate().hasNext()) {
            parametersEntity = (ParametersEntity) queryParameters.iterate().next();
            gameId = parametersEntity.getCurrentGame();

        }

        Query query = session.createQuery("from PrizeDistributionEntity where gameId =:gameId");
        query.setParameter("gameId", gameId);
        try {

            List<PrizeDistributionEntity> prizeDistributionEntities = query.list();
            PayTableListEntity payTableListEntity = new PayTableListEntity();
            if (query.iterate().hasNext()) {
                payTableListEntity.setPrizeDistributionEntities(prizeDistributionEntities);
                message = "{ \"paytable\":";
                message += payTableListEntity.getPrizeDistributionEntities().toString();
                message += "}";
            } else {
                message = "{\"message\":\"There aren't paytables for gameid=" + gameId + "\"}";
            }

        } catch (PersistenceException e) {
            message = "{\"message\":\"Please fill the all paytable values before get them for gameid=" + gameId + "\"}";
        }


        tx.commit();
        session.close();
        return message;
    }

    public int addPrizes(int gameId, List<PrizeDistributionEntity> prizeDistributionEntities) {
        int prizesWasAdded = 0;
        session = getSession();
        Transaction tx = session.beginTransaction();
        GameEntity gameEntity = null;

        Query query = session.createQuery("from GameEntity where gameId =:gameId");
        query.setParameter("gameId", gameId);
        if (query.iterate().hasNext()) {
            gameEntity = (GameEntity) query.iterate().next();

            String deleteQuery = "delete from PrizeDistribution where gameid=" + gameId;
            query = session.createSQLQuery(deleteQuery);
            query.executeUpdate();

            gameEntity.setPrizes(prizeDistributionEntities);

            session.update(gameEntity);
            prizesWasAdded = prizeDistributionEntities.size();
        }
        tx.commit();
        session.close();
        return prizesWasAdded;
    }

    public PrizeDistributionEntity getPrizes(int gameId, long betStep) {
        session = getSession();
        Transaction tx = session.beginTransaction();
        PrizeDistributionEntity prizeDistributionEntity = null;


        Query query = session.createQuery("from PrizeDistributionEntity where gameId =:gameId and bet=:betStep");
        query.setParameter("gameId", gameId);
        query.setParameter("betStep", betStep);
        if (query.iterate().hasNext()) {
            prizeDistributionEntity = (PrizeDistributionEntity) query.iterate().next();

        }
        tx.commit();
        session.close();
        return prizeDistributionEntity;
    }

    public void addRecords(int prizeId, List<RecordEntity> recordEntities) {
        session = getSession();
        Transaction tx = session.beginTransaction();
        PrizeDistributionEntity prizeDistributionEntity = null;

        Query query = session.createQuery("from PrizeDistributionEntity where prizeId =:prizeId");
        query.setParameter("prizeId", prizeId);
        if (query.iterate().hasNext()) {
            prizeDistributionEntity = (PrizeDistributionEntity) query.iterate().next();
            prizeDistributionEntity.setRecordEntities(recordEntities);
            session.update(prizeDistributionEntity);
        }
        tx.commit();
        session.close();
    }

    public List<RecordEntity> getRecords(int prizeId) {
        session = getSession();
        Transaction tx = session.beginTransaction();
        List<RecordEntity> records = null;


        Query query = session.createQuery("from PrizeDistributionEntity where prizeId =:prizeId");
        query.setParameter("prizeId", prizeId);
        if (query.iterate().hasNext()) {
            records = ((PrizeDistributionEntity) query.iterate().next()).getRecordEntities();

        }
        tx.commit();
        session.close();
        return records;
    }

    public void addQuantum(int prizeId, List<QuantumPrizesEntity> quantumPrizesEntities) {
        session = getSession();
        Transaction tx = session.beginTransaction();
        PrizeDistributionEntity prizeDistributionEntity = null;

        Query query = session.createQuery("from PrizeDistributionEntity where prizeId =:prizeId");
        query.setParameter("prizeId", prizeId);
        if (query.iterate().hasNext()) {
            prizeDistributionEntity = (PrizeDistributionEntity) query.iterate().next();
            prizeDistributionEntity.setQuantum_prizesEntity(quantumPrizesEntities);
            session.update(prizeDistributionEntity);
        }
        tx.commit();
        session.close();
    }

    public List<QuantumPrizesEntity> getQuantum(int prizeId) {
        session = getSession();
        Transaction tx = session.beginTransaction();
        List<QuantumPrizesEntity> quantumPrizesEntities = null;


        Query query = session.createQuery("from PrizeDistributionEntity where prizeId =:prizeId");
        query.setParameter("prizeId", prizeId);
        if (query.iterate().hasNext()) {
            quantumPrizesEntities = ((PrizeDistributionEntity) query.iterate().next()).getQuantum_prizesEntity();

        }
        tx.commit();
        session.close();
        return quantumPrizesEntities;
    }

    public void countAllTickets() {
        session = getSession();
        Transaction tx = session.beginTransaction();

        //count win tickets in recordentity
        String hql1 = "with alltickets as(\n" +
                "\tselect  t.gameid, t.price, t.win, count(t.win) tickets, sum(t.win) from ticket t group by t.gameid, t.price, t.win order by gameid, price, win\n" +
                "), dist as(\n" +
                "\tselect prizeid, gameid, bet from prizedistribution\n" +
                "), prizewithtickets as(\n" +
                "select al.gameid, al.price, al.win, al.tickets, sum, p.prizeid from alltickets al left join dist p on(al.gameid=p.gameid and al.price = p.bet)\n" +
                ")\n" +
                "update recordentity r set tickets = (select p.tickets from prizewithtickets p where r.prizeid=p.prizeid and r.prize = p.win) \n;";
        Query query = session.createSQLQuery(hql1);
        query.executeUpdate();

        //count max price in prizedistribution
        hql1 = "with maxprized as(\n" +
                "select prizeid, max(prize) max_prize from recordentity group by prizeid\n" +
                ")update prizedistribution p set max_prize = (select m.max_prize from maxprized m where m.prizeid = p.prizeid);";
        query = session.createSQLQuery(hql1);
        query.executeUpdate();

        //count total tickets
        hql1 = "with ticket_sum as(\n" +
                "\tselect p.gameId, p.bet, count(t.win) count_not_empty_tickets, sum(t.win) from ticket t\n" +
                "\tjoin prizedistribution p on (t.gameId = p.gameId and t.price = p.bet) \n" +
                "\tgroup by(p.gameId, p.bet) \n" +
                ")update prizedistribution p set total_tickets = (select t.count_not_empty_tickets from ticket_sum t where p.gameid=t.gameid and p.bet = t.bet);";
        query = session.createSQLQuery(hql1);
        query.executeUpdate();

        //count winning tickets
        hql1 = "with ticket_sum as(\n" +
                "\tselect p.gameId, p.bet, count(t.win) count_not_empty_tickets, sum(t.win) from ticket t\n" +
                "\tjoin prizedistribution p on (t.gameId = p.gameId and t.price = p.bet) \n" +
                "\twhere win>0\n" +
                "\tgroup by(p.gameId, p.bet) \n" +
                ")update prizedistribution p set winning_tickets = (select t.count_not_empty_tickets from ticket_sum t where p.gameid=t.gameid and p.bet = t.bet);";
        query = session.createSQLQuery(hql1);
        query.executeUpdate();

        //count total payout
        hql1 = "with ticket_sum as(\n" +
                "\tselect p.gameId, p.bet, count(t.win) count_not_empty_tickets, sum(t.win) from ticket t\n" +
                "\tjoin prizedistribution p on (t.gameId = p.gameId and t.price = p.bet)\n" +
                "\twhere win>0\n" +
                "\tgroup by(p.gameId, p.bet) \n" +
                ")update prizedistribution p set total_payout = (select t.sum from ticket_sum t where p.gameid=t.gameid and p.bet = t.bet);";
        query = session.createSQLQuery(hql1);
        query.executeUpdate();


        //count any_prize_hit
        hql1 = "update prizedistribution p set any_prize_hit = cast (winning_tickets as decimal )/total_tickets";
        query = session.createSQLQuery(hql1);
        query.executeUpdate();

        //count max_prize_hit
        hql1 = "with subquery as(\n" +
                "select prizeid, prize, tickets from recordentity where (prizeid, prize) in(\n" +
                "select prizeid, max(prize) from recordentity group by prizeid order by prizeid)\n" +
                "and tickets>0\n" +
                ") update prizedistribution p set max_prize_hit = cast((select s.tickets from subquery s where p.prizeid = s.prizeid) as decimal)/p.total_tickets;";
        query = session.createSQLQuery(hql1);
        query.executeUpdate();

        tx.commit();
        session.close();

        session = getSession();
        tx = session.beginTransaction();

        hql1 = "update PrizeDistribution set total_tickets = coalesce(total_tickets,0), max_prize_hit=coalesce(max_prize_hit,0), any_prize_hit=coalesce(any_prize_hit,0),winning_tickets=coalesce(winning_tickets,0),total_payout=coalesce(total_payout,0);";
        query = session.createSQLQuery(hql1);
        query.executeUpdate();

        tx.commit();
        session.close();

        session = getSession();
        tx = session.beginTransaction();
        hql1 = "update recordentity set tickets = coalesce(tickets,0);";
        query = session.createSQLQuery(hql1);
        query.executeUpdate();


        tx.commit();
        session.close();

    }


    public String saveMathTicket(InputStream data) throws ParserConfigurationException, IOException, SAXException {
        session = getSession();
        Transaction tx = session.beginTransaction();


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
                        String ticketSql = "insert into ticket(idx,gameId,price,win,context,played) values(" + idx + ", " + gameId + ", " + price + ", " + win + ", '" + context + "', " + 0 + ")";
                        Query query = session.createSQLQuery(ticketSql);
                        query.executeUpdate();
                        idx++;
                    }
                }

            }

        }


        tx.commit();
        session.close();
        return "{\"message\":\"Where added " + (idx - 1) + "\"}";
    }

    public void deleteTickets() {
        session = getSession();
        Transaction tx = session.beginTransaction();

        String hql = "delete from TicketEntity";
        Query query = session.createQuery(hql);
        query.executeUpdate();
        tx.commit();
        session.close();
    }

    public void deleteDistributions() {
        session = getSession();
        Transaction tx = session.beginTransaction();

        String hql = "delete from PrizeDistributionEntity";
        Query query = session.createQuery(hql);
        query.executeUpdate();
        tx.commit();
        session.close();
    }

    public void deleteGames() {
        session = getSession();
        Transaction tx = session.beginTransaction();

        String hql = "delete from GameEntity";
        Query query = session.createQuery(hql);
        query.executeUpdate();
        tx.commit();
        session.close();
    }


    String answerTicket = "";
    String gameName = "";

    String getRandomTicket(int id) {
        answerTicket = "";
        gameName = "";
        session = getSession();
        Transaction tx = session.beginTransaction();
        Map<Long, List<Integer>> prizes = new HashMap<>();

        Query query0 = session.createQuery("from GameEntity where gameId =:id");
        query0.setParameter("id", id);
        if (query0.iterate().hasNext()) {
            GameEntity gameEntity = (GameEntity) query0.iterate().next();
            gameName = gameEntity.getGameName();
        }

        Query query = session.createQuery("from ParametersEntity");

        if (query.iterate().hasNext()) {
            ParametersEntity parametersEntity = (ParametersEntity) query.iterate().next();

            Query query2 = session.createQuery("from PrizeDistributionEntity where gameId =:id and bet =:betId");
            query2.setParameter("id", id);
            query2.setParameter("betId", parametersEntity.getCurrentBet());

            if (query2.iterate().hasNext()) {
                PrizeDistributionEntity prizeDistributionEntity = (PrizeDistributionEntity) query2.iterate().next();

                int totalTickets = prizeDistributionEntity.getTotal_tickets();


                List<RecordEntity> records = prizeDistributionEntity.getRecordEntities();
                int newCount = 0;
                for (int i = 0; i < records.size(); i++) {
                    List<Integer> between = new ArrayList<>();
                    between.add(newCount);
                    RecordEntity recordEntity = records.get(i);
                    int tickets = recordEntity.getTickets();
                    between.add(tickets + newCount);
                    prizes.put(recordEntity.getPrize(), between);
                    newCount = newCount + tickets;
                }

                /**
                 * empty tickets
                 */
                {
                    List<Integer> between = new ArrayList<>();

                    between.add(newCount);
                    int emptyTickets = totalTickets - newCount;
                    between.add(newCount + emptyTickets);
                    prizes.put(0L, between);
                    newCount = newCount + emptyTickets;
                }

                int rng = new Random().nextInt(newCount) + 1;

                prizes.forEach((aLong, integers) -> {
                    if (rng > integers.get(0) && rng <= integers.get(1)) {
                        int prize = Math.toIntExact(aLong);
                        System.out.println(prize);
                        TicketGeneratorRunner ticketGeneratorRunner = TicketGeneratorRunner.getInstance();
                        answerTicket = ticketGeneratorRunner.getRandomTicket(gameName, Math.toIntExact(parametersEntity.getCurrentBet()), prize);
                    }
                });

            }

        }
        tx.commit();
        session.close();
        return answerTicket;
    }


    String getRandomTicket(int id, long bet) {
        answerTicket = "";
        gameName = "";
        session = getSession();
        Transaction tx = session.beginTransaction();
        Map<Long, List<Integer>> prizes = new HashMap<>();

        Query query0 = session.createQuery("from GameEntity where gameId =:id");
        query0.setParameter("id", id);
        if (query0.iterate().hasNext()) {
            GameEntity gameEntity = (GameEntity) query0.iterate().next();
            gameName = gameEntity.getGameName();
        }

        Query query2 = session.createQuery("from PrizeDistributionEntity where gameId =:id and bet =:betId");
        query2.setParameter("id", id);
        query2.setParameter("betId", bet);

        if (query2.iterate().hasNext()) {
            PrizeDistributionEntity prizeDistributionEntity = (PrizeDistributionEntity) query2.iterate().next();

            int totalTickets = prizeDistributionEntity.getTotal_tickets();


            List<RecordEntity> records = prizeDistributionEntity.getRecordEntities();
            int newCount = 0;
            for (int i = 0; i < records.size(); i++) {
                List<Integer> between = new ArrayList<>();
                between.add(newCount);
                RecordEntity recordEntity = records.get(i);
                int tickets = recordEntity.getTickets();
                between.add(tickets + newCount);
                prizes.put(recordEntity.getPrize(), between);
                newCount = newCount + tickets;
            }

            /**
             * empty tickets
             */
            {
                List<Integer> between = new ArrayList<>();

                between.add(newCount);
                int emptyTickets = totalTickets - newCount;
                between.add(newCount + emptyTickets);
                prizes.put(0L, between);
                newCount = newCount + emptyTickets;
            }

            int rng = new Random().nextInt(newCount) + 1;

            prizes.forEach((aLong, integers) -> {
                if (rng > integers.get(0) && rng <= integers.get(1)) {
                    int prize = Math.toIntExact(aLong);
                    System.out.println(prize);
                    TicketGeneratorRunner ticketGeneratorRunner = TicketGeneratorRunner.getInstance();
                    answerTicket = ticketGeneratorRunner.getRandomTicket(gameName, Math.toIntExact(bet), prize);
                }
            });

        }

        tx.commit();
        session.close();
        return answerTicket;
    }

    String getRandomTicket(int id, long bet, int prize) {
        answerTicket = "";
        gameName = "";
        session = getSession();
        Transaction tx = session.beginTransaction();
        Map<Long, List<Integer>> prizes = new HashMap<>();

        Query query0 = session.createQuery("from GameEntity where gameId =:id");
        query0.setParameter("id", id);
        if (query0.iterate().hasNext()) {
            GameEntity gameEntity = (GameEntity) query0.iterate().next();
            gameName = gameEntity.getGameName();
        }
        TicketGeneratorRunner ticketGeneratorRunner = TicketGeneratorRunner.getInstance();
        answerTicket = ticketGeneratorRunner.getRandomTicket(gameName, Math.toIntExact(bet), prize);
        tx.commit();
        session.close();
        return answerTicket;
    }

}
