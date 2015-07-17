package snmp_test;

import java.io.IOException;
import java.util.List;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

public class SnmpCheck
{
    //member variables & getter/setter s
    private String targetIp;
    private String oidString;
    private String community;
    private int snmpVersion;
    private String portNum;
    private Integer timeout;
    
    public String getTargetIp()
    {
        return targetIp;
    }
    public void setTargetIp(String targetIp)
    {
        this.targetIp = targetIp;
    }
    public String getOidString()
    {
        return oidString;
    }
    public void setOidString(String oidString)
    {
        this.oidString = oidString;
    }
    public String getCommunity()
    {
        return community;
    }
    public void setCommunity(String community)
    {
        this.community = community;
    }
    public int getSnmpVersion()
    {
        return snmpVersion;
    }
    public void setSnmpVersion(int snmpVersion)
    {
        this.snmpVersion = snmpVersion;
    }
    public String getPortNum()
    {
        return portNum;
    }
    public void setPortNum(String portNum)
    {
        this.portNum = portNum;
    }
    public Integer getTimeout()
    {
        return timeout;
    }
    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }

    //constructor
    SnmpCheck() // throws Exception
    {
        targetIp = null;
        oidString = null;
        community = "public";
        snmpVersion = SnmpConstants.version2c;
        portNum = "161";
        timeout = 1000*2;
    }
    
    public static void main(String[] args)
    {
        String oid;
        //oid = "1.3.6.1.4.1.2021.4.5.0";
        oid = "1.3.6.1.4.1.1872.2.5.4.1.1.4.2.1.2";
        //oid = "1.3.6.1.4.1.1872.2.5.4.1.1.3.3.1.2"; //pool member id, octet string
        //oid = "1.3.6.1.4.1.1872.2.5.1.2.3.1.1.1"; //port name
        //oid = "1.3.6.1.4.1.1872.2.5.1.3.1.29.2"; //power supply
        SnmpCheck me = new SnmpCheck();
        
        me.setOidString(oid);
        me.setTargetIp("192.168.100.12");
        me.setCommunity("openbase");
        try
        {
            me.snmpWalk();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    //TreeUtils를 써서 walk한다. SNMPv1 get-next, SNMP v2c/v3 get-bulk 자동 적용된다. 
    private void snmpWalk() throws IOException
    {
        Address targetAddress = GenericAddress.parse("UDP:" + this.targetIp + "/" + this.portNum);
        TransportMapping transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();
        
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(this.community));
        target.setAddress(targetAddress);
        target.setVersion(this.snmpVersion);
        target.setTimeout(this.timeout);
        target.setRetries(3);
        
        OID oid = null;
        try
        {
            oid = new OID(oidString);
        }
        catch(RuntimeException ex) 
        {
            System.out.println("OID is invalid. Stop test.");
            System.exit(1);
        }
        
        TreeUtils treeUtil = new TreeUtils(snmp, new DefaultPDUFactory());
        List<TreeEvent> events = treeUtil.getSubtree(target, oid);
        if(events==null || events.size()==0)
        {
            System.out.println("No data:event tree");
            System.exit(1);
        }
        
        int count=0;
        for(TreeEvent event: events)
        {
            System.out.println("event start ----- ");
            if(event != null)
            {
                if(event.isError()==true)
                {
                    System.err.println(String.format("oid [%s] error: %s\n", oid, event.getErrorMessage()));
                }
                
                VariableBinding[] varBindings = event.getVariableBindings();
                if(varBindings==null || varBindings.length==0)
                {
                    System.out.println("No data:variable bindings");
                }
                for(VariableBinding vb: varBindings)
                {
                    System.out.println(String.format("%s:\t%s:\t%s", vb.getOid(), vb.getVariable().getSyntaxString(), vb.getVariable()));
                    count++;
                }
            }
        }
        System.out.println("count = " + count);
        snmp.close();
    }
}
