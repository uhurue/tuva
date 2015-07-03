package yktest;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.io.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class httpsClient
{
    static {
        disableSslVerification();
    }
    // 인증 문제를 해결한 소스페이지: http://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
    private static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]
            {
                new X509TrustManager()
                {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers()
                    {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType)
                    {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType)
                    {
                    }
                }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier()
            {
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception
    {
        String httpsURL = "https://61.82.88.214/login/login";

        String query = "user[account]="+URLEncoder.encode("admin","UTF-8"); 
        query += "&";
        query += "user[password]="+URLEncoder.encode("openbase","UTF-8") ;

//        String httpsURL = "https://172.172.2.220/member/login.action";
//
//        String query = "id="+URLEncoder.encode("admin","UTF-8"); 
//        query += "&";
//        query += "pw="+URLEncoder.encode("smartadmin01","UTF-8") ;
        
        URL myurl = new URL(httpsURL);
        HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-length", String.valueOf(query.length())); 
        con.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
        con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)"); 
        con.setDoOutput(true); 
        con.setDoInput(true); 

        DataOutputStream output = new DataOutputStream(con.getOutputStream());  
        output.writeBytes(query);
        output.close();

        DataInputStream input = new DataInputStream( con.getInputStream() ); 

        for( int c = input.read(); c != -1; c = input.read() ) 
        System.out.print( (char)c ); 
        input.close(); 

//        System.out.println("Resp Code:"+con .getResponseCode()); 
//        System.out.println("Resp Message:"+ con .getResponseMessage()); 

        httpsURL = "https://61.82.88.214/malware_analysis/analyses?ma_filter_text=247&ma_filter_col";
        myurl = new URL(httpsURL);
        HttpsURLConnection con1 = (HttpsURLConnection)myurl.openConnection();
        con1 = (HttpsURLConnection)myurl.openConnection();
        con1.setRequestMethod("GET");
        con1.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
        con1.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)"); 
        con1.setDoOutput(true); 
        con1.setDoInput(true); 
        
        output = new DataOutputStream(con1.getOutputStream());
        output.close();
        input = new DataInputStream( con1.getInputStream() ); 

        for( int c = input.read(); c != -1; c = input.read() ) 
        System.out.print( (char)c ); 
        input.close(); 

        System.out.println("Resp Code:"+con1.getResponseCode()); 
        System.out.println("Resp Message:"+ con1.getResponseMessage()); 

    }
}
