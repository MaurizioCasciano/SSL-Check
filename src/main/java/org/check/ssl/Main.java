package org.check.ssl;

import javax.net.ssl.*;
import java.security.*;
import java.security.cert.X509Certificate;

public class Main {
    public static void main(String[] args) throws Exception{
        // Let'sEncrypt Certificates
        X509Certificate cert1 = CertificateLoader.loadCertificate("certs/isrg-root-x1.pem", "X509");
        X509Certificate cert2 = CertificateLoader.loadCertificate("certs/lets-encrypt-r3.pem", "X509");

        if(cert1 != null && cert2 != null){
            KeyStore letsEncryptKeyStore = KeyStoreHelper.getKeyStore(cert1, cert2);

            SSLContext sslContext = KeyStoreHelper.getSSLContext(letsEncryptKeyStore, null);
            SSLContext.setDefault(sslContext);
        }

        // Test Ping
        SSLPing.ping("oidc.7shield.eu", 32644);
    }
}
