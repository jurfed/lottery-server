package entities;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ParametersEntity {
    private long currentBalance;
    private long maxWin;
    private String currencyCode;
    private double soundValue;
    private String languageCode;
    private transient int parameterId;
    private Long currentBet;
    private int currentGame;
    private List<Long> allBets = new ArrayList<>();

    public ParametersEntity(long currentBalance, long maxWin, String currencyCode, double soundValue, String languageCode, Long currentBet, int currentGame) {
        this.currentBalance = currentBalance;
        this.maxWin = maxWin;
        this.currencyCode = currencyCode;
        this.soundValue = soundValue;
        this.languageCode = languageCode;
        this.parameterId = parameterId;
        this.currentBet = currentBet;
        this.currentGame = currentGame;
    }

    public ParametersEntity() {
    }

    public long getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(long currentBalance) {
        this.currentBalance = currentBalance;
    }

    public long getMaxWin() {
        return maxWin;
    }

    public void setMaxWin(long maxWin) {
        this.maxWin = maxWin;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getSoundValue() {
        return soundValue;
    }

    public void setSoundValue(double soundValue) {
        this.soundValue = soundValue;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public int getParameterId() {
        return parameterId;
    }

    public void setParameterId(int parameterId) {
        this.parameterId = parameterId;
    }

    public Long getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(Long currentBet) {
        this.currentBet = currentBet;
    }

    public int getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(int currentGame) {
        this.currentGame = currentGame;
    }

    public List<Long> getAllBets() {
        return allBets;
    }

    public void setAllBets(List<Long> allBets) {
        this.allBets = allBets;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
