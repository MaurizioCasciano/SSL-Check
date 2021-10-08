import org.check.ssl.KeyStoreHelper;
import org.check.ssl.SSLPing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import java.security.*;

public class SSLPingTest {

    @BeforeAll
    public static void configureKeyStore(){
        KeyStore letsEncryptKeyStore = KeyStoreHelper.getLetsEncryptKeyStore();
        SSLContext sslContext = null;

        try {
            sslContext = KeyStoreHelper.getSSLContext(letsEncryptKeyStore, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SSLContext.setDefault(sslContext);
    }

    @Test
    public void testPingGoogle() {
        Assertions.assertTrue(SSLPing.ping("google.com", 443));
    }

    @Test
    public void testPingLetsEncryptRootX1(){
        Assertions.assertTrue(SSLPing.ping("valid-isrgrootx1.letsencrypt.org", 443));
    }

    @Test
    public void testPingLetsEncryptRootX2(){
        Assertions.assertTrue(SSLPing.ping("valid-isrgrootx2.letsencrypt.org", 443));
    }
}
