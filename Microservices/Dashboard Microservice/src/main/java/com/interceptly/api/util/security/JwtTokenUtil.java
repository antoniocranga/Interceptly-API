package com.interceptly.api.util.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Slf4j
@Component
public class JwtTokenUtil {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtTokenUtil(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.algorithm = Algorithm.RSA256(publicKey, privateKey);
        this.verifier = JWT.require(algorithm).withIssuer("interceptly").build();
    }

    public boolean validateToken(String jwtToken) {
        try {
            verifier.verify(jwtToken);
            return true;
        } catch (JWTVerificationException e) {
            log.error(e.toString());
            return false;
        }
    }
}