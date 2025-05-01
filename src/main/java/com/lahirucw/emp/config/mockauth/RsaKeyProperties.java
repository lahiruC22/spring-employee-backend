package com.lahirucw.emp.config.mockauth;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class RsaKeyProperties {
    
    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    public RsaKeyProperties(){
        KeyPair keyPair = generateKeyPair();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }

    private static KeyPair generateKeyPair() {
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Could not generate RSA key pair", e);
        }
    }
}
