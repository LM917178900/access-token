package com.test.lei.tokenUtil;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @description: JwtHelper
 * @author: leiming5
 * @date: 2020-10-16 08:42
 */
final class JwtHelper {
    JwtHelper() {
    }

    static ClientAssertion buildJwt(String clientId, ClientCertificate credential, String jwtAudience) throws MsalClientException {
        if (StringHelper.isBlank(clientId)) {
            throw new IllegalArgumentException("clientId is null or empty");
        } else if (credential == null) {
            throw new IllegalArgumentException("credential is null");
        } else {
            long time = System.currentTimeMillis();
            JWTClaimsSet claimsSet = (new JWTClaimsSet.Builder()).audience(Collections.singletonList(jwtAudience)).issuer(clientId).jwtID(UUID.randomUUID().toString()).notBeforeTime(new Date(time)).expirationTime(new Date(time + 600000L)).subject(clientId).build();

            SignedJWT jwt;
            try {
                List<Base64> certs = new ArrayList();
                certs.add(new Base64(credential.publicCertificate()));
                com.nimbusds.jose.JWSHeader.Builder builder = new com.nimbusds.jose.JWSHeader.Builder(JWSAlgorithm.RS256);
                builder.x509CertChain(certs);
                builder.x509CertThumbprint(new Base64URL(credential.publicCertificateHash()));
                jwt = new SignedJWT(builder.build(), claimsSet);
                RSASSASigner signer = new RSASSASigner(credential.key());
                jwt.sign(signer);
            } catch (Exception var10) {
                throw new MsalClientException(var10);
            }

            return new ClientAssertion(jwt.serialize());
        }
    }
}

