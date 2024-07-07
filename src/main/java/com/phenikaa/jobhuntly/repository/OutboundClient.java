package com.phenikaa.jobhuntly.repository;

import com.phenikaa.jobhuntly.dto.ExchangeTokenDTO;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;

@FeignClient(value = "outbound-job-huntyly", url = "https://oauth2.googleapis.com")
public interface OutboundClient {
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenDTO.ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenDTO.ExchangeTokenRequest request);

}
