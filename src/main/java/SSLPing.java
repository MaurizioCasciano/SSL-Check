import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.OutputStream;

//https://github.com/dimalinux/SSLPing
public class SSLPing {
    public static boolean ping(String host, int port){

        try {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(host, port);

            // Hostname verification is not done by default in Java with raw SSL connections.
            // The next 3 lines enable it.
            SSLParameters sslParams = new SSLParameters();
            sslParams.setEndpointIdentificationAlgorithm("HTTPS");
            sslsocket.setSSLParameters(sslParams);

            // we only send 1 byte, so don't buffer
            sslsocket.setTcpNoDelay(true);

            // Write a test byte to trigger the SSL handshake
            OutputStream out = sslsocket.getOutputStream();
            out.write(1);

            // If no exception happened, we connected successfully
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
