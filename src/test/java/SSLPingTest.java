import org.check.ssl.SSLPing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SSLPingTest {

    @Test
    public void testPingGoogle() {
        Assertions.assertTrue(SSLPing.ping("google.com", 443));
    }


    @Test
    public void testPingLetsEncryptRootX1(){
        Assertions.assertTrue(SSLPing.ping("valid-isrgrootx1.letsencrypt.org", 443));
    }
}
