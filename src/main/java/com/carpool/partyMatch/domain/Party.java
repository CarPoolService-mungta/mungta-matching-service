package com.carpool.partyMatch.domain;

//import java.util.List;

import lombok.*;

import javax.persistence.*;

import com.carpool.partyMatch.exception.ApiException;
import com.carpool.partyMatch.exception.ApiStatus;

@Entity
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "party")
@Builder
public class Party extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "party_info_id")
    private Long partyInfoId;

    @Column(name = "cur_number_of_party")
    private int curNumberOfParty;
    @Column(name = "max_number_of_party")
    private int maxNumberOfParty;

    @Embedded
    private Driver driver;


    @Column(name = "party_status")
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
