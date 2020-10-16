package com.test.lei.tokenUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.Enumeration;

/**
 * @description: ClientCertificate
 * @author: leiming5
 * @date: 2020-10-16 08:40
 */
final class ClientCertificate implements IClientCertificate {
    private static final int MIN_KEY_SIZE_IN_BITS = 2048;
    private final PrivateKey key;
    private final X509Certificate publicCertificate;

    private ClientCertificate(PrivateKey key, X509Certificate publicCertificate) {
        if (key == null) {
            throw new NullPointerException("PrivateKey is null or empty");
        } else {
            this.key = key;
            if (key instanceof RSAPrivateKey) {
                if (((RSAPrivateKey)key).getModulus().bitLength() < 2048) {
                    throw new IllegalArgumentException("certificate key size must be at least 2048");
                }
            } else {
                if (!"sun.security.mscapi.RSAPrivateKey".equals(key.getClass().getName()) && !"sun.security.mscapi.CPrivateKey".equals(key.getClass().getName())) {
                    throw new IllegalArgumentException("certificate key must be an instance of java.security.interfaces.RSAPrivateKey or sun.security.mscapi.RSAPrivateKey");
                }

                try {
                    Method method = key.getClass().getMethod("length");
                    method.setAccessible(true);
                    if ((Integer)method.invoke(key) < 2048) {
                        throw new IllegalArgumentException("certificate key size must be at least 2048");
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException var4) {
                    throw new RuntimeException("error accessing sun.security.mscapi.RSAPrivateKey length: " + var4.getMessage());
                }
            }

            this.publicCertificate = publicCertificate;
        }
    }

    public String publicCertificateHash() throws CertificateEncodingException, NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(getHash(this.publicCertificate.getEncoded()));
    }

    public String publicCertificate() throws CertificateEncodingException {
        return Base64.getEncoder().encodeToString(this.publicCertificate.getEncoded());
    }

    static ClientCertificate create(InputStream pkcs12Certificate, String password) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
        KeyStore keystore = KeyStore.getInstance("PKCS12", "SunJSSE");
        keystore.load(pkcs12Certificate, password.toCharArray());
        Enumeration<String> aliases = keystore.aliases();
        String alias = (String)aliases.nextElement();
        PrivateKey key = (PrivateKey)keystore.getKey(alias, password.toCharArray());
        X509Certificate publicCertificate = (X509Certificate)keystore.getCertificate(alias);
        return create(key, publicCertificate);
    }

    static ClientCertificate create(PrivateKey key, X509Certificate publicCertificate) {
        return new ClientCertificate(key, publicCertificate);
    }

    private static byte[] getHash(byte[] inputBytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(inputBytes);
        return md.digest();
    }

    public PrivateKey key() {
        return this.key;
    }
}
