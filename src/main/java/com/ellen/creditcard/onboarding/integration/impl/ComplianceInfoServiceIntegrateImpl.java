package com.ellen.creditcard.onboard.integration.impl;

import com.ellen.creditcard.onboard.integration.ComplianceInfoServiceIntegrate;
import com.ellen.creditcard.onboard.util.MockUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ComplianceInfoServiceIntegrateImpl implements ComplianceInfoServiceIntegrate {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public Boolean complianceCheck() {
        try {
            String url = "https://api.risk.com/data?city";
            MockUtil.mockReturnValue(restTemplate, "getForObject", "true");
            return Boolean.valueOf(restTemplate.getForObject(url, String.class, ""));
        } catch(Exception e) {
            //@todo 是否切换为调用异常
            return false;
        }
    }
}
