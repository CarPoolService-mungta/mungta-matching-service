package com.carpool.partyMatch.repository;

import com.carpool.partyMatch.domain.MatchStatus;
import com.carpool.partyMatch.domain.PartyStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MatchInfoRepositorySupport {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Long> findPartyIdByUserIdAndMatchStatusAndPartyStatuses(String userId, MatchStatus matchStatus, List<PartyStatus> partyStatuses){
        return entityManager.createQuery("select" +
                        " p.partyInfoId FROM MatchInfo m inner join Party p on m.partyInfoId=p.partyInfoId where m.userId= :userId and m.matchStatus=:matchStatus and p.partyStatus in (:partyStatuses)")
                .setParameter("userId", userId)
                .setParameter("matchStatus", matchStatus)
                .setParameter("partyStatuses", partyStatuses)
                .getResultList();
    }
}
