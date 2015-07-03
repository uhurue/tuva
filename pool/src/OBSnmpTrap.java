import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;


public class OBSnmpTrap
{
    private Integer    snmpVersion = SnmpConstants.version2c; //default version
    private String     host;
    private String     protocol    = "UDP";
    private Integer    trapPort    = 162;
    private String     community   = "public";
    private Integer    timeout     = 1000;
    private Integer    retry       = 2;

    @Override
    public String toString()
    {
        return "OBSnmpTrap [snmpVersion=" + snmpVersion + ", host=" + host + ", protocol=" + protocol + ", trapPort=" + trapPort + ", community=" + community + ", timeout=" + timeout + ", retry=" + retry + "]";
    }

    public Integer getSnmpVersion()
    {
        return snmpVersion;
    }
    public void setSnmpVersion(Integer snmpVersion)
    {
        this.snmpVersion = snmpVersion;
    }
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
    public Integer getTrapPort()
    {
        return trapPort;
    }
    public void setTrapPort(Integer trapPort)
    {
        this.trapPort = trapPort;
    }
    public String getCommunity()
    {
        return community;
    }
    public void setCommunity(String community)
    {
        this.community = community;
    }
    public Integer getTimeout()
    {
        return timeout;
    }
    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }
    public Integer getRetry()
    {
        return retry;
    }
    public void setRetry(Integer retry)
    {
        this.retry = retry;
    }
    // end of getters/setters
    
//    public OBSnmpTrap()
//    {
//    }
    public OBSnmpTrap(Integer version, String host, Integer port, String community)
    {
        this.setSnmpVersion(version);
        this.setHost(host);
        this.setTrapPort(port);
        this.setCommunity(community);
    }

    public static void main(String[] args) throws Exception
    {
        OBSnmpTrap trap = new OBSnmpTrap(SnmpConstants.version2c, "172.172.2.153", 162, "public");
        //OBSnmpTrap trap = new OBSnmpTrap(SnmpConstants.version1, "172.172.2.153", 162, "public");
        trap.send("1.2.3.4.5", "ADC_ALTEON1", "system reboot");
    }

    public void send(String trapOid, String object, String message)
    {
        if(this.getSnmpVersion().equals(SnmpConstants.version2c))
        {
            sendV2(trapOid, "1.2.3.4.111", object, "1.2.3.4.222", message);
        }
        else if(this.getSnmpVersion().equals(SnmpConstants.version1))
        {
            sendV1(trapOid, "1.2.3.4.111", object, "1.2.3.4.222", message);
        }
        else //v3?
        {
            //todo
        }
    }
//  http://techdive.in/snmp/snmp4j-trap-sender
    public void sendV1(String trapOid, String oid1, String message1, String oid2, String message2)
    {
        try
        {
            //Create Transport Mapping
            TransportMapping transport = new DefaultUdpTransportMapping();
            //transport.listen();
            
            // 수신측 설정 
            Address targetAddress = new UdpAddress(this.getHost() + "/" + this.getTrapPort());
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(this.getCommunity()));
            target.setVersion(SnmpConstants.version1);
            target.setAddress(targetAddress);
            target.setRetries(this.getRetry());
            target.setTimeout(this.getTimeout());
            
            //Create PDU for V1
            PDUv1 pdu = new PDUv1();
            pdu.setType(PDU.V1TRAP);
            pdu.setEnterprise(new OID(trapOid));
            pdu.setGenericTrap(PDUv1.ENTERPRISE_SPECIFIC);
            pdu.setSpecificTrap(1);
            pdu.setAgentAddress(new IpAddress(this.getHost()));
            
            //Add Payload
            OID oid = new OID(oid1);
            pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, oid));
            Variable var = new OctetString(message1);          
            pdu.add(new VariableBinding(oid, var));
            
            oid = new OID(oid2);
            var = new OctetString(message2);
            pdu.add(new VariableBinding(oid, var));
            
            //Send the PDU
            Snmp snmp = new Snmp(transport);
            //System.out.println("Sending V1 Trap to " + ipAddress + " on Port " + port);
            snmp.send(pdu, target);
            snmp.close();
        }
        catch (Exception e)
        {
//            System.err.println("Error in Sending V1 Trap to " + ipAddress + " on Port " + port);
//            System.err.println("Exception Message = " + e.getMessage());
        }
    }

    public void sendV2(String trapOid, String oid1, String message1, String oid2, String message2)
    { 
        // Create v2 common PDU           
        PDU pdu = new PDU();
        pdu.setType(PDU.TRAP); //pdu.setType(PDU.NOTIFICATION)??

        TimeTicks uptime = new TimeTicks((Long)(System.currentTimeMillis()/1000));

        // 수신측 설정
        Address targetAddress = new UdpAddress(this.getHost() + "/" + this.getTrapPort());
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(this.getCommunity()));
        target.setVersion(SnmpConstants.version2c);
        target.setAddress(targetAddress);
        target.setRetries(this.getRetry());
        target.setTimeout(this.getTimeout());

        pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(trapOid)));
        pdu.add(new VariableBinding(SnmpConstants.sysUpTime, uptime)); // put your uptime here 

        //Add Payload
        OID oid = new OID(oid1);
        pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, oid));
        Variable var = new OctetString(message1);          
        pdu.add(new VariableBinding(oid, var));
        
        oid = new OID(oid2);
        var = new OctetString(message2);
        pdu.add(new VariableBinding(oid, var));

        // Send
        Snmp snmp;
        try
        {
            snmp = new Snmp(new DefaultUdpTransportMapping());
            snmp.send(pdu, target, null, null);
            snmp.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

//    CommunityTarget target = new CommunityTarget();
//    target.setCommunity(new OctetString("public"));
//    target.setAddress(targetAddress);
//    target.setVersion(SnmpConstants.version1);
}
