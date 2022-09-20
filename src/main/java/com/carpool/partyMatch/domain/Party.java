package com.carpool.partyMatch.domain;

//import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

import com.carpool.partyMatch.exception.ApiException;
import com.carpool.partyMatch.exception.ApiStatus;

@Entity
@Data
@NoArgsConstructor
public class Party extends BaseEntity {

    @Id @GeneratedValue
    Long id;
    Long partyInfoId;
    int curNumberOfParty;
    int maxNumberOfParty;

    @Embedded
    Driver driver;

    PartyStatus partyStatus;

    public boolean isDriver(String userId) {
        return this.driver.getDriverId().equals(userId);
    }

    public void removePartyNumber() {
        int restNumber = this.curNumberOfParty - 1;
        if(restNumber < this.maxNumberOfParty){
            this.partyStatus = PartyStatus.OPEN;
        }
        else if (restNumber < 0) {
            throw new ApiException(ApiStatus.NOT_EXIST_MATCH);
        }
        this.curNumberOfParty = restNumber;
    }

    public void addPartyNumber() {
        int restNumber = this.curNumberOfParty + 1;
        if(restNumber == this.maxNumberOfParty){
            this.partyStatus = PartyStatus.FULL;
        }
        else if (restNumber > this.maxNumberOfParty) {
            throw new ApiException(ApiStatus.NOT_EXIST_MATCH);
        }
        this.curNumberOfParty = restNumber;
    }

}
