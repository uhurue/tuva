package fireeye;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;






import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import fireeye.SFSSLSocketFactory;

@SuppressWarnings({ "deprecation" })
public class LinkFileDownloadWork // extends SimpleTask
{

//    @Override
//    protected ArrayList<OBAnalysisTask> work()
//    {
//        ArrayList<OBAnalysisTask> nextTaskList = new ArrayList<OBAnalysisTask>();
//        if(idList==null || idList.size()<0)
//            return nextTaskList;
//        
//        long threadId = new Thread().getId();
//        System.out.println(String.format("[daemon][%d] task start. param:%s",  threadId, fileInfoList));
//
//        if(OBCommon.isLockFileExist(lockFile)==true)
//        {
//            System.out.println(String.format("[daemon][%d] task end(already running)",  threadId));
//            return nextTaskList;
//        }
//        HttpClient httpClient = null;
//        try 
//        {
//            httpClient = getHttpClient();
//
//            // file ���옣 �쐞移� 異붿텧 諛� �깮�꽦.
//            String filePath = "";
////            URI urlDownload;
//            if(webLogin(httpClient) == false)
//            {
//                return nextTaskList;
//            }
//
//            // analysis �럹�씠吏� �씠�룞
//            String authenticityToken = gotoAnalysisPage(httpClient);
//            
//            System.out.println(String.format("success to move analysis page. token:%s\n", authenticityToken));
//
//            for(Integer id:idList)
//            {
//                String fileName = Integer.toString(id);
//                System.out.println(String.format("file name:%s\n", fileName));
//    
//                // Filter Set - update filter
//                setSearchFilter(httpClient, authenticityToken, id);
//                System.out.println(String.format("success to set filter\n"));
//
//                // link download
//                String xmlPathName = downloadXml(httpClient, filePath, fileName);
//
////                String pcapPathName = downloadPcapFile(httpClient, fileInfo.getEventID(), filePath, fileName);
////                System.out.println(String.format("success to download pcap file:%s\n", pcapPathName));
////
////                String textPathName = downloadPcapTextFile(httpClient, fileInfo.getEventID(), filePath, fileName);
////                System.out.println(String.format("success to download text file:%s\n", textPathName));
////
////                String clipPathName = downloadClipFile(httpClient, fileInfo.getEventID(), filePath, fileName);;
////                System.out.println(String.format("success to download clip file:%s\n", clipPathName));
//            }
//        }
//        catch(IllegalArgumentException e)
//        {
//            System.out.println(String.format("[daemon][%d] task error: illegal argument exception error: %s",  threadId, e.getMessage()));
//        }
//        catch(NullPointerException e)
//        {
//            System.out.println(String.format("[daemon][%d] task error: null pointer exception error: %s",  threadId, e.getMessage()));
//        }
//        catch(Exception e)
//        {
//            System.out.println(String.format("[daemon][%d] task error: general exception error: %s",  threadId, e.getMessage()));
//        }
//        finally
//        {
//            webLogout(httpClient);
//            try
//            {
//            	if(httpClient!=null) httpClient.getConnectionManager().shutdown();
//            }
//            catch(Exception e)
//            {
//                System.out.println(String.format("[daemon][%d] failed to close http session:%s",  threadId, e.getMessage()));
//            }
//        }
//        System.out.println(String.format("[daemon][%d] task end",  threadId));
//
//		return nextTaskList;
//    }
    
    private String downloadXml(HttpClient httpClient, String filePath, String fileName) throws Exception
    {
        String xmlPathName = filePath + fileName;
        String retVal = xmlPathName + ".pdf";
        try
        {
            String url = "https://61.82.88.214/malware_analysis/send_xml";
            URI urlDownload = new URI(url);
            HttpGet httpGet = new HttpGet(urlDownload);
            //a.  2. 由ы�섏뒪�듃
            HttpResponse resp = httpClient.execute(httpGet);
            //    a. 3. 寃곌낵 異붿텧
            HttpEntity entity = resp.getEntity();                  
            fileWrite(entity, xmlPathName + ".xml");
        }        
        finally
        {
        }  
        return retVal;
    }
    
    private static final String STR_LOGINPASSWD = "Incorrect user id or password.";
    private static final String STR_LOGINCONNCHECK = "Too many web logins, 225 allowed";
    
    private boolean webLogin(HttpClient httpClient) throws Exception
    {
        try
        {
            String urlString = "https://61.82.88.114/login/login";
//            String urlString = "https://61.82.88.214/login/login";
            String line = null;
            URI urlLogin = new URI(urlString);
            
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(urlLogin);
            
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("user[account]", "admin"));
            params.add(new BasicNameValuePair("user[password]", "openbase"));
            
            httpPost.setEntity(new UrlEncodedFormEntity(params));
    
            // login
            HttpResponse resp = httpClient.execute(httpPost);
            HttpEntity entity = resp.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            
            // login success check
            while ((line = reader.readLine()) != null)
            {
                if (line.contains(STR_LOGINPASSWD))
                {
                    System.out.println(String.format("[daemon] MAS login Fail : Password is wrong."));
                    return false;
                }
                if (line.contains(STR_LOGINCONNCHECK))
                {
                    System.out.println(String.format("[daemon] MAS login Fail : Too many web logins, 225 allowed"));
                    return false;
                }
            }
        }
        catch(UnsupportedEncodingException e)
        {
            throw new Exception("unsupported encoding exception: " +e.getMessage());
        }
        catch(ClientProtocolException e)
        {
            throw new Exception("client protocol exception: " +e.getMessage());
        }
        catch(IllegalStateException e)
        {
            throw new Exception("illegal state exception: " +e.getMessage());
        }
        catch(IOException e)
        {
            throw new Exception("io exception: " +e.getMessage());
        }
        catch(NullPointerException e)
        {
            throw new Exception("null pointer exception: " +e.getMessage());
        }
        catch(Exception e)
        {
            throw new Exception("general exception: " +e.getMessage());
        }
        finally
        {
            
        }  
        return true;
    }
    private void webLogout(HttpClient httpClient)
    {
        try
        {
            if(httpClient==null)
                return ;
            String url = "https://61.82.88.214/login/logout";
            URI logOut = new URI(url);
            HttpGet httpGet = new HttpGet(logOut);
        
            httpClient.execute(httpGet);
        }
        catch(UnsupportedEncodingException e)
        {
            System.out.println(String.format("[daemon] failed to web logout. unsupported encoding exception"));
        }
        catch(ClientProtocolException e)
        {
            System.out.println(String.format("[daemon] failed to web logout. client protocol exception"));
        }
        catch(IllegalStateException e)
        {
            System.out.println(String.format("[daemon] failed to web logout. illegal state exception"));
        }
        catch(IOException e)
        {
            System.out.println(String.format("[daemon] failed to web logout. io exception"));
        }
        catch(NullPointerException e)
        {
            System.out.println(String.format("[daemon] failed to web logout. null pointer exception"));
        }
        catch(Exception e)
        {
            System.out.println(String.format("[daemon] failed to web logout.%s",  e.getMessage()));
        }
        finally
        {
            
        }  
    }

    private static final String STR_AUTHENTICITY_TOKEN = "name=\"authenticity_token\"";
    
    private String gotoAnalysisPage(HttpClient httpClient) throws Exception
    {
        String authenticityToken = "";
        try
        {
            String urlString = "https://61.82.88.214/malware_analysis/analyses";;
//            String urlString = "https://61.82.88.214/malware_analysis/analyses";
         
            URI analysis = new URI(urlString);
            
            HttpGet httpGet = new HttpGet(analysis);
            HttpResponse resp = httpClient.execute(httpGet);
            HttpEntity entity = resp.getEntity();
            
            // a. 寃곌낵濡쒕��꽣 Authenticity_Token 異붿텧
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(entity.getContent()));

            String line = "";
            while ((line = reader.readLine()) != null)
            {
                if (line.contains(STR_AUTHENTICITY_TOKEN))
                {
                    authenticityToken = line.substring(line.indexOf("value=\"") + "value=\"".length(), line.lastIndexOf("\""));
                    break;
                }
            }
            Thread.sleep(500);
        }
        catch(UnsupportedEncodingException e)
        {
            throw new Exception("unsupported encoding exception: " +e.getMessage());
        }
        catch(ClientProtocolException e)
        {
            throw new Exception("client protocol exception: " +e.getMessage());
        }
        catch(IllegalStateException e)
        {
            throw new Exception("illegal state exception: " +e.getMessage());
        }
        catch(IOException e)
        {
            throw new Exception("io exception: " +e.getMessage());
        }
        catch(NullPointerException e)
        {
            throw new Exception("null pointer exception: " +e.getMessage());
        }
        catch(Exception e)
        {
            throw new Exception("general exception: " +e.getMessage());
        }
        finally
        {
        }  
        return authenticityToken;
    }
    
    private boolean setSearchFilter(HttpClient httpClient, String authenticityToken, int malwareID) throws Exception
    {
        try
        {
            String urlString = "https://61.82.88.214/malware_analysis/update_filter";
            
            URI filterSet = new URI(urlString);
          
            // a. 1. �뙆�씪誘명꽣 �뀑 �깮�꽦
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("utf8", "�쐯"));
            params.add(new BasicNameValuePair("authenticity_token", authenticityToken));
            params.add(new BasicNameValuePair("filter", "Set"));
            params.add(new BasicNameValuePair("ma_filter_text", "^" + malwareID + "$"));
//            params.add(new BasicNameValuePair("ma_filter_text", "^" + 507 + "$"));
            params.add(new BasicNameValuePair("ma_filter_col", "id"));
            params.add(new BasicNameValuePair("ma_username", "All"));
            params.add(new BasicNameValuePair("_", ""));
            
            // 2. post 媛앹껜 �깮�꽦 諛� �뙆�씪誘명꽣 遺�李�
            HttpPost httpPost = new HttpPost(filterSet);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            
            // a. 3. 由ы�섏뒪�듃
            HttpResponse resp = httpClient.execute(httpPost);
            
            // a.4. 寃곌낵 異붿텧
            HttpEntity entity = resp.getEntity();
            Thread.sleep(2000);

            EntityUtils.consume(entity);// 諛섎뱶�떆 �븘�슂. get �슂泥��뿉 ���븳 �쓳�떟�쓣 泥섎━�빐 以섏빞 �븿.
//            System.out.println("!!!!!!!!!!!!!!!!!!!!END OF UPDATE FILTER!!!!!!!!!!!!!!!!!!!");
        }
        catch(UnsupportedEncodingException e)
        {
            throw new Exception("unsupported encoding exception: " +e.getMessage());
        }
        catch(ClientProtocolException e)
        {
            throw new Exception("client protocol exception: " +e.getMessage());
        }
        catch(IllegalStateException e)
        {
            throw new Exception("illegal state exception: " +e.getMessage());
        }
        catch(IOException e)
        {
            throw new Exception("io exception: " +e.getMessage());
        }
        catch(NullPointerException e)
        {
            throw new Exception("null pointer exception: " +e.getMessage());
        }
        catch(Exception e)
        {
            throw new Exception("general exception: " +e.getMessage());
        }
        finally
        {
            
        }  
        return true;
    }
    
    private String downloadClipFile(HttpClient httpClient, int evnetID, String filePath, String fileName) throws Exception
    {
        if(evnetID==0)
            return "";
        
        String fullPathName = filePath + fileName + ".flv";
        try
        {
            String url = "https://61.82.88.214/event_stream/send_pcap_clip?ev_id="+evnetID;
            URI urlDownload = new URI(url);
            HttpGet httpGet = new HttpGet(urlDownload);
            //a.  2. 由ы�섏뒪�듃
            HttpResponse resp = httpClient.execute(httpGet);
            //    a. 3. 寃곌낵 異붿텧
            HttpEntity entity = resp.getEntity();                  
            fileWrite(entity, fullPathName);
        }        
        catch(UnsupportedEncodingException e)
        {
            throw new Exception("unsupported encoding exception: " +e.getMessage());
        }
        catch(ClientProtocolException e)
        {
            throw new Exception("client protocol exception: " +e.getMessage());
        }
        catch(IllegalStateException e)
        {
            throw new Exception("illegal state exception: " +e.getMessage());
        }
        catch(IOException e)
        {
            throw new Exception("io exception: " +e.getMessage());
        }
        catch(NullPointerException e)
        {
            throw new Exception("null pointer exception: " +e.getMessage());
        }
        catch(Exception e)
        {
            throw new Exception("general exception: " +e.getMessage());
        }
        finally
        {
            
        }  
        return fullPathName;
    }
    
    private String downloadPcapTextFile(HttpClient httpClient, int evnetID, String filePath, String fileName) throws Exception
    {
        if(evnetID==0)
            return "";
        
        String fullPathName = filePath + fileName + ".txt";
        try
        {
            String url = "https://61.82.88.214/event_stream/send_pcap_ascii?ev_id="+evnetID;
            URI urlDownload = new URI(url);
            HttpGet httpGet = new HttpGet(urlDownload);
            //a.  2. 由ы�섏뒪�듃
            HttpResponse resp = httpClient.execute(httpGet);
            //    a. 3. 寃곌낵 異붿텧
            HttpEntity entity = resp.getEntity();                  
            fileWrite(entity, fullPathName);
        }        
        catch(UnsupportedEncodingException e)
        {
            throw new Exception("unsupported encoding exception: " +e.getMessage());
        }
        catch(ClientProtocolException e)
        {
            throw new Exception("client protocol exception: " +e.getMessage());
        }
        catch(IllegalStateException e)
        {
            throw new Exception("illegal state exception: " +e.getMessage());
        }
        catch(IOException e)
        {
            throw new Exception("io exception: " +e.getMessage());
        }
        catch(NullPointerException e)
        {
            throw new Exception("null pointer exception: " +e.getMessage());
        }
        catch(Exception e)
        {
            throw new Exception("general exception: " +e.getMessage());
        }
        finally
        {
            
        }  
        return fullPathName;
    }
    
    /**
     * pcap file download
     * 
     * @param httpClient
     * @param evnetID
     * @param filePath
     * @param fileName
     * @throws OBException
     */
    private String downloadPcapFile(HttpClient httpClient, int evnetID, String filePath, String fileName) throws Exception
    {
        if(evnetID==0)
            return "";
        
        String fullPathName = filePath + fileName + ".pcap";
        try
        {
            String url = "https://61.82.88.214/event_stream/send_pcap_file?ev_id="+evnetID;
            URI urlDownload = new URI(url);
            HttpGet httpGet = new HttpGet(urlDownload);
            //a.  2. 由ы�섏뒪�듃
            HttpResponse resp = httpClient.execute(httpGet);
            //    a. 3. 寃곌낵 異붿텧
            HttpEntity entity = resp.getEntity();                  
            fileWrite(entity, fullPathName);
        }        
        catch(UnsupportedEncodingException e)
        {
            throw new Exception("unsupported encoding exception: " +e.getMessage());
        }
        catch(ClientProtocolException e)
        {
            throw new Exception("client protocol exception: " +e.getMessage());
        }
        catch(IllegalStateException e)
        {
            throw new Exception("illegal state exception: " +e.getMessage());
        }
        catch(IOException e)
        {
            throw new Exception("io exception: " +e.getMessage());
        }
        catch(NullPointerException e)
        {
            throw new Exception("null pointer exception: " +e.getMessage());
        }
        catch(Exception e)
        {
            throw new Exception("general exception: " +e.getMessage());
        }
        finally
        {
            
        }  
        return fullPathName;
    }

  
    private HttpClient getHttpClient() throws Exception 
    {
        try 
        {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SFSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } 
        catch(NoSuchAlgorithmException e)
        {
            throw new Exception("NoSuchAlgorithmException: " + e.getMessage());
        }
        catch(KeyManagementException e)
        {
            throw new Exception("KeyManagementException: " + e.getMessage());
        }
        catch(KeyStoreException e)
        {
            throw new Exception("KeyStoreException: " + e.getMessage());
        }
        catch(UnrecoverableKeyException e)
        {
            throw new Exception("UnrecoverableKeyException: " + e.getMessage());
        }
        catch(UnsupportedEncodingException e)
        {
            throw new Exception("unsupported encoding exception: " +e.getMessage());
        }
        catch(ClientProtocolException e)
        {
            throw new Exception("client protocol exception: " +e.getMessage());
        }
        catch(IllegalStateException e)
        {
            throw new Exception("illegal state exception: " +e.getMessage());
        }
        catch(IOException e)
        {
            throw new Exception("io exception: " +e.getMessage());
        }
        catch(NullPointerException e)
        {
            throw new Exception("null pointer exception: " +e.getMessage());
        }
        catch(Exception e)
        {
            throw new Exception("general exception: " +e.getMessage());
        }
    }
    
    private static String fileWrite(HttpEntity entity, String fileName) throws IOException
    {
    	String retVal = null;
        if (entity != null) 
        {
        	
            InputStream instream = entity.getContent();
            
            try 
            {
            	BufferedInputStream  bis = new BufferedInputStream(instream);
                String filePath = fileName;
                
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                int inByte;
                while ((inByte = bis.read()) != -1 ) 
                { 
                    bos.write(inByte);
                }
                bis.close();
                bos.close();
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();
            } 
            catch (RuntimeException ex) 
            {
                throw ex;
            } 
            finally 
            {
                instream.close();
            }
        }
        return retVal;
    }
    
    public static void shellCmd(String command) throws Exception 
    {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(command);
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while((line = br.readLine()) != null) 
        {
        	System.out.println(line);
        	System.out.println(String.format("command : %s", line));
        }
    }
    
    public LinkFileDownloadWork()
    {
    }
    
    /**
     * Analysis Set Filter �뀒�뒪�듃 肄붾뱶
     * 
     * @param args
     * @author sw.jung
     * @throws OBException 
     */
    public static void main(String[] args)
    {
        try
        {
            HttpClient httpClient = new LinkFileDownloadWork().getHttpClient();
            String url = null;
//            String sessionId = null;
            ArrayList<NameValuePair> params = null;
            HttpPost post = null;
            HttpGet get = null;
            HttpResponse resp = null;
            HttpEntity entity = null;
            
            // Login Process
            System.out.println("!!!!!!!!!!!!!!!!!!!!START LOGIN~!!!!!!!!!!!!!!!!!!!");
            String id = "admin";
            String pw = "openbase";
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user[account]", id));
            params.add(new BasicNameValuePair("user[password]", pw));
            
            url = "https://61.82.88.214/login/login";
            post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(params));
            
            System.out.println("send login Request");
            resp = httpClient.execute(post);
            
            // 4. 寃곌낵 異붿텧
            entity = resp.getEntity();
            System.out.println(EntityUtils.toString(entity));
            
            System.out.println("login done");
            
            // Filter Set - Authenticity_Toekn 異붿텧
            url = "https://61.82.88.214/malware_analysis/analyses";
            get = new HttpGet(url);
            
            resp =httpClient.execute(get);
            
            entity = resp.getEntity();
            
            // 응답에서 Authenticity_Token 구하기
            System.out.println("!!!!!!!!!!!!!!!!!!!!START EXTRACT AUTH~!!!!!!!!!!!!!!!!!!!");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            String line = null;
            String authenticityToken = null;
            while ((line = reader.readLine()) != null)
            {
                if (line.contains("name=\"authenticity_token\""))
                {
                    System.out.println(line);
                    authenticityToken = line.substring(line.indexOf("value=\"") + "value=\"".length(), line.lastIndexOf("\""));
                    break;
                }
            }
            System.out.println("authenticityToken = " + authenticityToken);
            

            System.out.println("!!!!!!!!!!!!!!!!!!!!START UPDATE FILTER!!!!!!!!!!!!!!!!!!!");
            // Filter Set - update filter
            List<String> lines=Files.readAllLines(Paths.get("C:\\Users\\ykkim\\Desktop\\IOC\\FireEye_MaliciousReport_지사샘플_정상\\id.txt"), Charset.forName("UTF-8"));
            for(String tmpId:lines)
            {
              System.out.println("id = " + tmpId);
              if(tmpId.isEmpty())
              {
                  continue;
              }
              params.clear();
              params.add(new BasicNameValuePair("utf8", "�쐯"));
              params.add(new BasicNameValuePair("authenticity_token", authenticityToken));
              params.add(new BasicNameValuePair("filter", "Set"));
              params.add(new BasicNameValuePair("ma_filter_text", tmpId));
              params.add(new BasicNameValuePair("ma_filter_col", "id"));
              params.add(new BasicNameValuePair("ma_username", "All"));
              params.add(new BasicNameValuePair("_", ""));
              
              url = "https://61.82.88.214/malware_analysis/update_filter";
              post = new HttpPost(url);
              post.setEntity(new UrlEncodedFormEntity(params));
              
              System.out.println("Send update-filter request");
              resp = httpClient.execute(post);
              
              // 4. 결과 구하기
              entity = resp.getEntity();
              System.out.println(EntityUtils.toString(entity));
              
              // Dump XML
              System.out.println("Start xml dump!");
              // 1. get 媛앹껜 �깮�꽦
              url = "https://61.82.88.214/malware_analysis/send_xml";
              get = new HttpGet(url);
              System.out.println("xml dddddddddddd");
              resp = httpClient.execute(get);
              System.out.println("xml request sent!");
              entity = resp.getEntity();
              System.out.println("xml response entity done.");
              fileWrite(entity, tmpId + ".xml");              System.out.println("finished id = " + tmpId); 
            }

            System.out.println("!!!!!!!!!!!!!!!!!!!!END OF XML DUMP!!!!!!!!!!!!!!!!!!!");
        }
//        catch(UnsupportedEncodingException e)
//        {
//            e.printStackTrace();
//        }
//        catch(ClientProtocolException e)
//        {
//            e.printStackTrace();
//        }
//        catch(IOException e)
//        {
//            e.printStackTrace();
//        }
//        catch(IllegalStateException e)
//        {
//            e.printStackTrace();
//        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
