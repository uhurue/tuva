package syslog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogConfigIF;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.impl.net.tcp.TCPNetSyslogConfig;
import org.productivity.java.syslog4j.impl.net.udp.UDPNetSyslogConfig;
import org.productivity.java.syslog4j.util.SyslogUtility;

public class Client
{
    private String name;
    private String host;
    private String protocol;
    private int port;
    
    public static final long DEFAULT_GAP_MSEC = 10L;
    public static final long DEFAULT_GAP_SHORT_MSEC = 10L;
    
    public String getHost()
    {
        return host;
    }
    public void setHost(String host)
    {
        this.host = host;
    }
    public String getProtocol()
    {
        return protocol;
    }
    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }
    public int getPort()
    {
        return port;
    }
    public void setPort(int port)
    {
        this.port = port;
    }
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    
    public Client(String name, String host, String protocol, int port)
    {
        this.setName(name);
        this.setHost(host);
        this.setProtocol(protocol);
        this.setPort(port);
    }

    public void send(ArrayList<MetaMessage> metaMessages)
    {
        SyslogConfigIF config = null;
        SyslogIF client = null;
        
        config = (this.getProtocol().equals("tcp")) ?
            new TCPNetSyslogConfig(this.getHost(), this.getPort())
            : new UDPNetSyslogConfig(this.getHost(), this.getPort());
        client = Syslog.createInstance(this.getName(), config);
        
        for(MetaMessage message: metaMessages)
        {
            //SyslogUtility.sleep(message.getGap());    
            SyslogUtility.sleep(10);
            client.info(message.getMessage());
            System.out.println("sent: " + message.getMessage());
        }
    }
    
    public static void main(String[] args)
    {
        Client syslogClient = new Client("YKKIM", "172.172.2.154", "UDP", 514);

        String messageFile = "C:\\Users\\ykkim\\Desktop\\Defensepro\\2015_06_outofpath\\0626testdata\\04\\syslog_test.log";
        String metaMessageFile = "C:\\Users\\ykkim\\Desktop\\Defensepro\\2015_06_outofpath\\0626testdata\\04\\syslog_test.meta";
        
//        String messageFile = "C:\\Users\\ykkim\\Desktop\\Defensepro\\테스트syslog\\dummy.log";
//        String metaMessageFile = "C:\\Users\\ykkim\\Desktop\\Defensepro\\테스트syslog\\dummy.meta";
        syslogClient.makeMetaMessage(messageFile, metaMessageFile);

        ArrayList<MetaMessage> messagesToSend = syslogClient.readMetaMessages(metaMessageFile);
        syslogClient.send(messagesToSend);
    }
    
    public ArrayList<MetaMessage> readMetaMessages(String file)
    {
        List<String> lines;
        String [] str;
        ArrayList<MetaMessage> ret = new ArrayList<MetaMessage>();
        MetaMessage message;
        try
        {
            lines = Files.readAllLines(Paths.get(file), Charset.forName("UTF-8"));
            for(String tline:lines)
            {
                str = tline.split(",", 2);
                if(str.length == 2)
                {
                    message = new MetaMessage();
                    message.setGap(Long.parseLong(str[0]));
                    message.setMessage(str[1]);
                    ret.add(message);
                }
            }
        }
        catch(IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
            
    }
    public void makeMetaMessage(String file, String metaMessageFile) //메시지 입력 파일을 읽어서 테스터 파일을 만듦
    {
        List<String> lines;
        String [] str;
        long prevGap = 0;
        long currGap = 0;
        long gap = 0;
        boolean formatCheckDone = false;
        boolean havingTime = false;
        //String outputFile = "";
        ArrayList<String> messages = new ArrayList<String>();
        
        try
        {
            lines = Files.readAllLines(Paths.get(file), Charset.forName("UTF-8"));
            for(String tline:lines)
            {//2015-06-12 10:25:25.028 70 Behavioral-DoS "network flood IPv4 UDP" UDP 0.0.0.0 0 0.0.0.0 0 0 Regular "Detection-Rule" start 0 0 N/A 0 N/A high forward FFFFFFFF-FFFF-FFFF-01B3-0000557A04FC
                if(tline==null || tline.isEmpty())
                {
                    continue;
                }
                if(formatCheckDone == false) //첫번째 줄만 시간이 있나 검사한다. 모든 줄이 같은 형식이어야 한다.
                {
                    if(tline.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3} .*"))
                    {
                        havingTime = true;
                    }
                    formatCheckDone = true;
                }
                if(havingTime == true)
                {
                    str = tline.split(" ", 3);
                    if(str.length == 3)
                    {
                        currGap = getTimeGapMsec(str[0] + " " + str[1]); //yyyy-MM-dd HH:mm:ss.SSS 파라미터를 넘겨 msec를 구한다.
                        if(currGap >= 0) // -1: 시간 parsing 오류
                        {
                            if(prevGap == 0L) //첫번째 로그 라인이면 이전 gap이 없다.
                            {
                                prevGap = currGap;
                            }
                            gap = currGap - prevGap;
                            if(gap >= 0) //시간이 음수면 사고, 건너뛴다.
                            {
                                messages.add(String.format("%d,%s", gap, str[2]));
                                prevGap = currGap; //다음 라인 계산 준비
                            }
                        }
                    }
                    else
                    {
                        //형태에 문제가 있는 라인이므로 건너뛴다.
                    }
                }
                else //시간 없이 메시지만 있는 파일인 경우
                {
                    if(tline.contains("\"Block-Rule\" ongoing")) // block ongoing은 짧게 준다.
                    {
                        messages.add(String.format("%d,%s", DEFAULT_GAP_SHORT_MSEC, tline));    
                    }
                    else if(tline.contains("\"Detection-Rule\" start"))
                    {
                        messages.add(String.format("%d,%s", DEFAULT_GAP_SHORT_MSEC, tline));    
                    }
                    else if(tline.contains("\"Detection-Rule\" ongoing")) // 짧게 준다.
                    {
                        messages.add(String.format("%d,%s", DEFAULT_GAP_SHORT_MSEC, tline));    
                    }
                    else
                    {
                        messages.add(String.format("%d,%s", DEFAULT_GAP_MSEC, tline));
                    }
                }
            }
            writeMessages(metaMessageFile, messages);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void writeMessages(String file, ArrayList<String> messages)
    {
        BufferedWriter report;
        try
        {
            report = new BufferedWriter(new FileWriter(file));
                    
            for(String msg: messages)
            {
                report.write(String.format("%s\n", msg));
            }
            report.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    public long getTimeGapMsec(String strDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("KST"));
        Date date = null;
        long ret = -1;
        try
        {
            date = (Date)sdf.parse(strDate);
            ret = date.getTime();
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
        return ret;
    }
}

class MetaMessage
{
    long gap;
    String message;
    
    public long getGap()
    {
        return gap;
    }
    public void setGap(long gap)
    {
        this.gap = gap;
    }
    public String getMessage()
    {
        return message;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }
}

