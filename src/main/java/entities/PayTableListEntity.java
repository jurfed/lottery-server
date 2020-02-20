package entities;

import com.google.gson.Gson;

import java.util.List;

public class PayTableListEntity {
    public PayTableListEntity(){}

    List<PrizeDistributionEntity> prizeDistributionEntities;

    public List<PrizeDistributionEntity> getPrizeDistributionEntities() {
        return prizeDistributionEntities;
    }

    public void setPrizeDistributionEntities(List<PrizeDistributionEntity> prizeDistributionEntities) {
        this.prizeDistributionEntities = prizeDistributionEntities;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
