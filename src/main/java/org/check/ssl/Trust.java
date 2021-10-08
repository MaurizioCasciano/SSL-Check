package org.check.ssl;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class Trust {
    // http://cr.openjdk.java.net/~martin/webrevs/jdk/CacertsExplorer/test/jdk/javax/net/ssl/sanity/CacertsExplorer.java.html
    // https://gist.github.com/devandanger/a870b223a08e865001ee22b5f72fc14e
    public static void exploreCACerts() {
        try {
            String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            checkDefaultAlghorithm(defaultAlgorithm);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(defaultAlgorithm);
            trustManagerFactory.init((KeyStore) null);



            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            checkTrustManagersLength(trustManagers);

            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            X509Certificate[] acceptedIssuers = trustManager.getAcceptedIssuers();
            checkAcceptedIssuersLenght(acceptedIssuers);

            Arrays.stream(acceptedIssuers)
                    .map(X509Certificate::getIssuerX500Principal)
                    .forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        exploreCACerts();
    }

    private static void checkDefaultAlghorithm(String defaultAlgorithm) {
        if (!defaultAlgorithm.equals("PKIX")) {
            throw new AssertionError(
                    "Expected default algorithm PKIX, got " + defaultAlgorithm);
        }
    }

    private static void checkTrustManagersLength(TrustManager[] trustManagers) {
        if (trustManagers.length != 1) {
            throw new AssertionError("Expected exactly one TrustManager, got "
                    + Arrays.toString(trustManagers));
        }
    }

    private static void checkAcceptedIssuersLenght(X509Certificate[] acceptedIssuers) {
        if (acceptedIssuers.length == 0) {
            throw new AssertionError("no accepted issuers - cacerts file configuration problem?");
        }
    }

    /*
 https://stackoverflow.com/questions/18889058/programmatically-import-ca-trust-cert-into-existing-keystore-file-without-using
 https://stackoverflow.com/questions/24043397/options-for-programmatically-adding-certificates-to-java-keystore
 https://www.baeldung.com/java-keystore
 */
    public static void trustCerts(){
        try(InputStream is = SSLPing.class.getResourceAsStream("certs/isrg-root-x1.pem");
            BufferedReader r = new BufferedReader(new InputStreamReader(is))) {

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(is, null);

            String line = null;
            while ((line = r.readLine()) != null){
                System.out.println(line);
            }

        }catch (Exception e){

        }
    }

    /*
    https://stackoverflow.com/a/17421397/8737144
     */
    public static void loadJavaDefaultKeystore(){
        // Load the JDK cacerts Keystore file
        String javaHome = System.getProperty("java.home");
//        String cacertsPath = ""

        /*// Load the JDK's cacerts keystore file
        String filename = System.getProperty("java.home") + "/lib/security/cacerts".replace('/', File.separatorChar);
        FileInputStream is = new FileInputStream(filename);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] password = "changeit".toCharArray();
        //keystore.load(is, password.toCharArray());
        keystore.load(is, password);

        // This class retrieves the most-trusted CAs from the keystore
        PKIXParameters params = new PKIXParameters(keystore);
        // Get the set of trust anchors, which contain the most-trusted CA certificates
        java.security.cert.Certificate sapcert = keystore.getCertificate("SAPNetCA");
        PublicKey sapcertKey =  sapcert.getPublicKey();
        System.out.println(sapcertKey);
        Enumeration<String> aliases = keystore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            //System.out.println("alias certificates :"+alias);
            if (keystore.isKeyEntry(alias)) {
                keystore.getKey(alias, password);
            }
        }*/
    }
}
