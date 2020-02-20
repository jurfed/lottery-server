package entities;

import com.google.gson.Gson;

import java.util.List;

public class PrizeDistributionEntity
{

    private Long bet;

    private int gameId;

    private transient int prizeId;

    private List<QuantumPrizesEntity> quantum_prizesEntity;

    private List<RecordEntity> recordEntities;

    private int total_tickets;

    private Double max_prize_hit;

    private Double any_prize_hit;

    private int winning_tickets;

    private Long max_prize;

    private Long total_payout;

    public Long getBet ()
    {
        return bet;
    }

    public void setBet (Long bet)
    {
        this.bet = bet;
    }

    public int getTotal_tickets ()
    {
        return total_tickets;
    }

    public void setTotal_tickets (int total_tickets)
    {
        this.total_tickets = total_tickets;
    }

    private transient int idx;

    public PrizeDistributionEntity(Long bet, int total_tickets, List<QuantumPrizesEntity> quantum_prizesEntity, List<RecordEntity> recordEntities, Double max_prize_hit, Double any_prize_hit, int winning_tickets, Long max_prize, Long total_payout) {
        this.bet = bet;
        this.total_tickets = total_tickets;
        this.quantum_prizesEntity = quantum_prizesEntity;
        this.recordEntities = recordEntities;
        this.max_prize_hit = max_prize_hit;
        this.any_prize_hit = any_prize_hit;
        this.winning_tickets = winning_tickets;
        this.max_prize = max_prize;
        this.total_payout = total_payout;
    }

    public PrizeDistributionEntity(Long bet, int total_tickets, List<QuantumPrizesEntity> quantum_prizesEntity, List<RecordEntity> recordEntities, Double max_prize_hit, Double any_prize_hit, int winning_tickets, Long max_prize, Long total_payout, int gameId) {
        this.bet = bet;
        this.total_tickets = total_tickets;
        this.quantum_prizesEntity = quantum_prizesEntity;
        this.recordEntities = recordEntities;
        this.max_prize_hit = max_prize_hit;
        this.any_prize_hit = any_prize_hit;
        this.winning_tickets = winning_tickets;
        this.max_prize = max_prize;
        this.total_payout = total_payout;
        this.gameId = gameId;
    }

    public PrizeDistributionEntity() {
    }

    public int getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(int prizeId) {
        this.prizeId = prizeId;
    }

    public List<QuantumPrizesEntity> getQuantum_prizesEntity() {
        return quantum_prizesEntity;
    }

    public void setQuantum_prizesEntity(List<QuantumPrizesEntity> quantum_prizesEntity) {
        this.quantum_prizesEntity = quantum_prizesEntity;
    }

    public List<RecordEntity> getRecordEntities() {
        return recordEntities;
    }

    public void setRecordEntities(List<RecordEntity> recordEntities) {
        this.recordEntities = recordEntities;
    }

    public Double getMax_prize_hit ()
    {
        return max_prize_hit;
    }

    public void setMax_prize_hit (Double max_prize_hit)
    {
        this.max_prize_hit = max_prize_hit;
    }

    public Double getAny_prize_hit ()
    {
        return any_prize_hit;
    }

    public void setAny_prize_hit (Double any_prize_hit)
    {
        this.any_prize_hit = any_prize_hit;
    }

    public int getWinning_tickets ()
    {
        return winning_tickets;
    }

    public void setWinning_tickets (int winning_tickets)
    {
        this.winning_tickets = winning_tickets;
    }

    public Long getMax_prize ()
    {
        return max_prize;
    }

    public void setMax_prize (Long max_prize)
    {
        this.max_prize = max_prize;
    }

    public Long getTotal_payout ()
    {
        return total_payout;
    }

    public void setTotal_payout (Long total_payout)
    {
        this.total_payout = total_payout;
    }


    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}