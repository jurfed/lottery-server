package entities;

import com.google.gson.Gson;

public class QuantumPrizesEntity
{

    private transient int quantumId;
    private Long value;
    private transient int idx;
    public QuantumPrizesEntity(Long value) {
        this.value = value;
    }

    public QuantumPrizesEntity() {
    }

    public int getQuantumId() {
        return quantumId;
    }

    public void setQuantumId(int quantumId) {
        this.quantumId = quantumId;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
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