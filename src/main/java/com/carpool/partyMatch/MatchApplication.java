package com.carpool.partyMatch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;


import com.carpool.partyMatch.domain.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.carpool.partyMatch.kafka.KafkaProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * http localhost:8080/customers id="jyjang@uengine.org" address:='{"zipcode":"123", "detail":"용인"}'
 */

@SpringBootApplication
@RestController
@EnableAspectJAutoProxy
@EnableFeignClients
@EnableBinding(KafkaProcessor.class)
@Transactional
public class MatchApplication {
	public static ApplicationContext applicationContext;
	public static void main(String[] args) {
		applicationContext = SpringApplication.run(MatchApplication.class, args);
	}

	// @Autowired
	// PartyInfoRepository partyInfoRepository;


	// @RequestMapping(method = RequestMethod.PUT, path="partyInfoes/{partyInfoId}/apply")
	// public String applyParty(@PathVariable(value = "partyInfoId") Long partyInfoId){

	// 	PartyInfo theParty = partyInfoRepository.findById(partyInfoId).get();

	// 	partyInfoRepository.save(theParty);

	// 	return "파티 신청이 완료되었습니다.";
	// }

}
