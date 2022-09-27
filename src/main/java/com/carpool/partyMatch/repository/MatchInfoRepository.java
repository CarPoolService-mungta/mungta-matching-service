package com.carpool.partyMatch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import com.carpool.partyMatch.domain.MatchInfo;
import com.carpool.partyMatch.domain.MatchStatus;

public interface MatchInfoRepository extends JpaRepository<MatchInfo, Long>{    // Repository Pattern Interface

  List<MatchInfo> findAllByPartyInfoIdAndUserIdOrderByIdDesc(Long partyInfoId, String userId);
  List<MatchInfo> findByPartyInfoIdAndMatchStatus(Long partyInfoId, MatchStatus matchStatus);

  List<MatchInfo> findByPartyInfoIdAndMatchStatusIsIn(Long partyInfoId, List<MatchStatus> matchStatus);
  List<MatchInfo> findByUserIdAndMatchStatus(String userId, MatchStatus matchStatus);
  Optional<MatchInfo> findByPartyInfoIdAndUserIdAndMatchStatusIsIn(Long partyInfoId, String userId, List<MatchStatus> matchStatus);
  void deleteByPartyInfoIdAndUserId(Long partyInfoId, String userId);
}
