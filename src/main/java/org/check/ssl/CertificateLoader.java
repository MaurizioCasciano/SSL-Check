package org.check.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;

public class CertificateLoader {
    public static <T extends Certificate> T loadCertificate(String path, String type) {
        try (InputStream is = CertificateLoader.class.getClassLoader().getResourceAsStream(path)) {
            Collection<T> certs = (Collection<T>) CertificateFactory.getInstance(type)
                    .generateCertificates(is);

            return certs.iterator().next();
        } catch (IOException | CertificateException e) {
            e.printStackTrace();
            return null;
        }
    }
}
