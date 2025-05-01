package com.lahirucw.emp;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import com.lahirucw.emp.config.mockauth.RsaKeyProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Component
@Import(RsaKeyProperties.class)
@ActiveProfiles("test")
public class MockTokenGenerator {

    @Autowired
    private RsaKeyProperties rsaKeyProperties;
    
    private String generateMockToken(

            String subject,
            String issuer,
            List<String> audience,
            Instant expirationTime,
            String scope,
            List<String> roles

    ) throws JOSEException {

        RSAKey rsaKey = new RSAKey.Builder(rsaKeyProperties.getPublicKey())
                .privateKey(rsaKeyProperties.getPrivateKey())
                .keyID("mock-key-id")
                .algorithm(JWSAlgorithm.RS256)
                .keyUse(KeyUse.SIGNATURE)
                .build();

        JWSSigner signer = new RSASSASigner(rsaKey);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer(issuer)
                .audience(audience)
                .issueTime(Date.from(Instant.now()))
                .expirationTime(Date.from(expirationTime))
                .claim("scope", scope)
                .claim("roles", roles)
                .build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
        .keyID(rsaKey.getKeyID())
        .type(JOSEObjectType.JWT)
        .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

}
