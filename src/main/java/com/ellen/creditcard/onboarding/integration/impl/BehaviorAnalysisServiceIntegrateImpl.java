package com.ellen.creditcard.onboard.integration.impl;

import com.ellen.creditcard.onboard.integration.BehaviorAnalysisServiceIntegrate;
import com.ellen.creditcard.onboard.util.MockUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BehaviorAnalysisServiceIntegrateImpl implements BehaviorAnalysisServiceIntegrate {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public Integer behaviorAnalysis(String IdNumber) {
        try {
            String url = "https://api.risk.com/data?city";
            MockUtil.mockReturnValue(restTemplate, "getForObject", "12");
            return Integer.valueOf(restTemplate.getForObject(url, String.class, IdNumber));
        } catch(Exception e) {
            //@todo 是否切换为调用异常
            return 0;
        }
    }

}
