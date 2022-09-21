package com.carpool.partyMatch.domain;

//import java.util.List;

import lombok.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

import com.carpool.partyMatch.exception.ApiException;
import com.carpool.partyMatch.exception.ApiStatus;

@Entity
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Party extends BaseEntity {

    @Id @GeneratedValue
    private Long id;
    private Long partyInfoId;
    private int curNumberOfParty;
    private int maxNumberOfParty;

    @Embedded
    private Driver driver;

    private PartyStatus partyStatus;

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


    public static Party of(Long partyInfoId, int curNumberOfParty, int maxNumberOfParty, Driver driver, PartyStatus partyStatus){
        return Party.builder()
                .partyInfoId(partyInfoId)
                .curNumberOfParty(curNumberOfParty)
                .maxNumberOfParty(maxNumberOfParty)
                .driver(driver)
                .partyStatus(partyStatus)
                .build();
    }

}
