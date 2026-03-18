package com.ellen.creditcard.onboard.integration.impl;

import com.ellen.creditcard.onboard.integration.IdentityServiceIntegrate;
import com.ellen.creditcard.onboard.util.MockUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IdentityServiceIntegrateImpl implements IdentityServiceIntegrate {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public Boolean verifyIdentity(String IdNumber) {
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
