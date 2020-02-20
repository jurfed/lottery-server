import entities.*;
import org.junit.Test;
import controllers.DBController;

import java.util.ArrayList;
import java.util.List;

public class DBControllerTest {
    DBController dbController;

    @Test
    public void setParameters() {
        dbController = DBController.getInstance();
        dbController.setParameters(50000000, 60000000, "EU", 0.5, "EN", 100L, 11);
        System.out.println(dbController.getParameters().toString());
        dbController.setParameters(150, 999999, "RUB", 0.74, "RU", 100L,11);
        System.out.println(dbController.getParameters().toString());
    }

    @Test
    public void addgames() {
        dbController = DBController.getInstance();
        dbController.addGame(100, "new Game 1");
        System.out.println(dbController.getGame(100));
        dbController.addGame(101, "new Game 2");
        System.out.println(dbController.getGame(101));
    }

    @Test
    public void setTickets() {
        dbController = DBController.getInstance();
        dbController.addGame(100, "new Game 1");
        System.out.println(dbController.getGame(100));

        dbController.addGame(101, "new Game 2");
        System.out.println(dbController.getGame(101));
        List<TicketEntity> ticketEntities = new ArrayList<>();

        TicketEntity ticket1 = new TicketEntity(200L, 200L, "{sdfgsdfgsdfg}", 0);
        TicketEntity ticket2 = new TicketEntity(4500L, 5412L, "{sdfgsddfgsdfg}", 0);
        ticketEntities.add(ticket1);
        ticketEntities.add(ticket2);

        dbController.addTickets(100, ticketEntities);
        GameEntity gameEntity = dbController.getGame(100);

        System.out.println(gameEntity);

        dbController.updateTicketStatus(1, 1);
        System.out.println(dbController.getGame(100));

    }

    @Test
    public void addPrizes() {
        dbController = DBController.getInstance();
        dbController.addGame(100, "new Game 1");


        List<PrizeDistributionEntity> prizeDistributionEntities = new ArrayList<>();

        PrizeDistributionEntity prizeDistributionEntity1 = new PrizeDistributionEntity(200L, 5555, new ArrayList(), new ArrayList(), 0.222, 0.541, 1500, 5002L, 5412L);
        PrizeDistributionEntity prizeDistributionEntity2 = new PrizeDistributionEntity(100L, 4444, new ArrayList(), new ArrayList(), 0.111, 0.333, 300, 4004L, 3003L);

        prizeDistributionEntities.add(prizeDistributionEntity1);
        prizeDistributionEntities.add(prizeDistributionEntity2);

        dbController.addPrizes(100, prizeDistributionEntities);
        GameEntity gameEntity = dbController.getGame(100);
        System.out.println(gameEntity);

        System.out.println(dbController.getPrizes(100, 200));

    }


    @Test
    public void addRecords() {
        dbController = DBController.getInstance();
        List<RecordEntity> records = new ArrayList();
        records.add(new RecordEntity(400L,1000));
        records.add(new RecordEntity(500L,2000));
        records.add(new RecordEntity(700L,3000));
        records.add(new RecordEntity(800L,4000));
        records.add(new RecordEntity(900L,5000));

        PrizeDistributionEntity prizeDistributionEntity = dbController.getPrizes(100,200);

        dbController.addRecords(prizeDistributionEntity.getPrizeId(),records);

        System.out.println(dbController.getPrizes(100,200));
        GameEntity gameEntity = dbController.getGame(100);

        System.out.println(dbController.getGame(100));
    }

    @Test
    public void addQuantum() {
        dbController = DBController.getInstance();
        List<QuantumPrizesEntity> quantumPrizesEntities = new ArrayList();
        quantumPrizesEntities.add(new QuantumPrizesEntity(1000L));
        quantumPrizesEntities.add(new QuantumPrizesEntity(2000L));
        quantumPrizesEntities.add(new QuantumPrizesEntity(30000L));
        quantumPrizesEntities.add(new QuantumPrizesEntity(100000L));
        quantumPrizesEntities.add(new QuantumPrizesEntity(750000L));
        quantumPrizesEntities.add(new QuantumPrizesEntity(30000L));
        quantumPrizesEntities.add(new QuantumPrizesEntity(100000L));
        quantumPrizesEntities.add(new QuantumPrizesEntity(750000L));

        PrizeDistributionEntity prizeDistributionEntity = dbController.getPrizes(100,200);

        dbController.addQuantum(prizeDistributionEntity.getPrizeId(),quantumPrizesEntities);

        System.out.println(dbController.getPrizes(100,200));
        GameEntity gameEntity = dbController.getGame(100);

        System.out.println(dbController.getGame(100));
    }

    @Test
    public void readData() {
        dbController = DBController.getInstance();

        GameEntity game = dbController.getGame(21);
        System.out.println(game);
    }


}
