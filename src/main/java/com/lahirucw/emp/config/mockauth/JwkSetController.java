package com.lahirucw.emp.config.mockauth;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class JwkSetController {
    
    private final RsaKeyProperties rsaKeyProperties;
    private final Map<String, Object> jwkSet;

    public JwkSetController(RsaKeyProperties rsaKeyProperties) {
        this.rsaKeyProperties = rsaKeyProperties;
        this.jwkSet = buildJwkSet();
        log.warn("!!! Mock JWK Set endpoint enabled at /.well-known/jwks.json - FOR DEVELOPMENT ONLY !!!");
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> jwks() {
        return this.jwkSet;
    }

    private Map<String, Object> buildJwkSet(){
        RSAKey.Builder builder = new RSAKey.Builder(rsaKeyProperties.getPublicKey())
        .keyID("mock-key-id")
        .algorithm(com.nimbusds.jose.JWSAlgorithm.RS256)
        .keyUse(com.nimbusds.jose.jwk.KeyUse.SIGNATURE);

        JWKSet set = new JWKSet(builder.build());
        return set.toJSONObject();
    }
}
