package entities;

import com.google.gson.Gson;

import java.util.List;

public class GameEntity {

    private transient List<TicketEntity> tickets;
    private transient List<PrizeDistributionEntity> prizes;

    public GameEntity(int gameId, String gameName) {
        this.gameId = gameId;
        this.gameName = gameName;
    }

    public GameEntity() {
    }

    private int gameId;
    private String gameName;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public List<TicketEntity> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketEntity> tickets) {
        this.tickets = tickets;
    }

    public List<PrizeDistributionEntity> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<PrizeDistributionEntity> prizes) {
        this.prizes = prizes;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
