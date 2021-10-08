import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SSLPingTest {

    @Test
    public void testPingGoogle() {
        Assertions.assertTrue(SSLPing.ping("google.com", 443));
    }
}
