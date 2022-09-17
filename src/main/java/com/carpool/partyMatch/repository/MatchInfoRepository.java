package com.carpool.partyMatch.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import com.carpool.partyMatch.domain.MatchInfo;
import com.carpool.partyMatch.domain.MatchStatus;

public interface MatchInfoRepository extends JpaRepository<MatchInfo, Long>{    // Repository Pattern Interface

  MatchInfo findByPartyInfoIdAndUserId(Long partyInfoId, String userId);
  List<MatchInfo> findByPartyInfoIdAndMatchStatus(Long partyInfoId, MatchStatus matchStatus);
  List<MatchInfo> findByUserIdAndMatchStatus(String userId, MatchStatus matchStatus);
  void deleteByPartyInfoIdAndUserId(Long partyInfoId, String userId);
}
