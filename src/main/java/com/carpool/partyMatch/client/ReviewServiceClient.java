package com.carpool.partyMatch.client;

import com.carpool.partyMatch.client.dto.ReviewResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "review", url = "http://localhost:8083/api/review")
public interface ReviewServiceClient {

    @GetMapping(path = "")
    List<ReviewResponse> getReviewList(@RequestParam List<String> userIds);

}
