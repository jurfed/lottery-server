package entities;

import com.google.gson.Gson;

public class TicketEntity {

    private int ticketId;
    private transient int idx;

    private Long price;

    private Long win;

    private String context;

    private int gameId;

    private transient int played;

    private Long currentBalance;

    public TicketEntity(Long price, Long win, String context, int played, int gameId) {
        this.gameId = gameId;
        this.price = price;
        this.win = win;
        this.context = context;
        this.played = played;
    }

    public TicketEntity(Long price, Long win, String context, int played) {
        this.price = price;
        this.win = win;
        this.context = context;
        this.played = played;
    }


    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public TicketEntity() {
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getWin() {
        return win;
    }

    public void setWin(Long win) {
        this.win = win;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public Long getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Long currentBalance) {
        this.currentBalance = currentBalance;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}