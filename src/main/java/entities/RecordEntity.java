package entities;

import com.google.gson.Gson;

public class RecordEntity {

    private transient int recordId;
    private Long prize;
    private int tickets;
    private transient int idx;

    public RecordEntity(Long prize, int tickets) {
        this.prize = prize;
        this.tickets = tickets;
    }

    public RecordEntity() {
    }

    public Long getPrize() {
        return prize;
    }

    public void setPrize(Long prize) {
        this.prize = prize;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}