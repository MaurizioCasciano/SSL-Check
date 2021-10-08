package org.check.ssl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class KeyStoreHelper {
    public static KeyStore getKeyStore(X509Certificate... certificates) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            // Init with default JVM certificates
            keyStore.load(null, null);

            for (X509Certificate certificate : certificates) {
                String alias = certificate.getSubjectX500Principal().getName();
                keyStore.setCertificateEntry(alias, certificate);
            }

            return keyStore;
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static X509KeyManager getKeyManager(String algorithm, KeyStore keystore, char[] password) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        KeyManagerFactory factory = KeyManagerFactory.getInstance(algorithm);
        factory.init(keystore, password);
        return Iterables.getFirst(Iterables.filter(
                Arrays.asList(factory.getKeyManagers()), X509KeyManager.class), null);
    }

    public static X509TrustManager getTrustManager(String algorithm, KeyStore keystore) throws KeyStoreException, NoSuchAlgorithmException {
        TrustManagerFactory factory = TrustManagerFactory.getInstance(algorithm);
        factory.init(keystore);
        return Iterables.getFirst(Iterables.filter(
                Arrays.asList(factory.getTrustManagers()), X509TrustManager.class), null);
    }

    public static SSLContext getSSLContext(KeyStore keystore, char[] password) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String defaultAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
        X509KeyManager customKeyManager = getKeyManager("SunX509", keystore, password);
        X509KeyManager jvmKeyManager = getKeyManager(defaultAlgorithm, null, null);
        X509TrustManager customTrustManager = getTrustManager("SunX509", keystore);
        X509TrustManager jvmTrustManager = getTrustManager(defaultAlgorithm, null);

        KeyManager[] keyManagers = {new CompositeX509KeyManager(ImmutableList.of(jvmKeyManager, customKeyManager))};
        TrustManager[] trustManagers = {new CompositeX509TrustManager(ImmutableList.of(jvmTrustManager, customTrustManager))};

        SSLContext context = SSLContext.getInstance("SSL");
        context.init(keyManagers, trustManagers, null);
        return context;
    }

    public static KeyStore getLetsEncryptKeyStore(){
        // Let'sEncrypt Certificates
        X509Certificate cert1 = CertificateLoader.loadCertificate("certs/isrg-root-x1.pem", "X509");
        X509Certificate cert2 = CertificateLoader.loadCertificate("certs/lets-encrypt-r3.pem", "X509");

        if(cert1 != null && cert2 != null) {
            KeyStore letsEncryptKeyStore = KeyStoreHelper.getKeyStore(cert1, cert2);
            return letsEncryptKeyStore;
        }else {
            return null;
        }
    }
}
