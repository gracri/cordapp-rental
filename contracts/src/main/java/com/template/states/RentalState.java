package com.template.states;

import com.template.contracts.RentalContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@BelongsToContract(RentalContract.class)
public class RentalState implements ContractState {
    private final String dailyPrice;
    private final String startDay;
    private final String endDay;
    private final String startTime;
    private final String endTime;
    private final String vinNumber;
    private final Party renter;
    private final Party owner;
    private List<?> images;


    public RentalState(List<?> images, String vinNumber, String dailyPrice, String startDay, String endDay, String startTime, String endTime, Party renter, Party owner) {
        this.dailyPrice = dailyPrice;
        this.startDay = startDay;
        this.endDay = endDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.vinNumber = vinNumber;
        this.renter = renter;
        this.owner = owner;
        this.images = images;
    }

    public String getDailyPrice() {
        return dailyPrice;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public Party getOwner() {
        return owner;
    }

    public Party getRenter() {
        return renter;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getStartDay() {
        return startDay;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getEndDay() {
        return endDay;
    }

    public List<?> getImages() {
        return images;
    }

    //participants are the list of parties who store this shared fact
    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(owner, renter);
    }
}
