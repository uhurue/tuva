"""
Adds the host to OBJECTNAME

"""
#import threading
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

OBJECTNAME = "/NetObjects/Host_Trigger_Object/Out_CPS_Control"

def sendEmail(ip):
    mailFrom = 'snowcug@gmail.com'
    mailTo = 'park0516@openbase.co.kr'
    serverIP = '173.194.72.108'
    serverPort = 465
    serverAccount = 'snowcug@gmail.com'
    serverPassword = 'rlekflek'
    subject = "\'OUT CPS\' HOST TRIGGER OCCURED [%s]" % ip

    msg = MIMEMultipart()
    msg['Subject'] = subject
    msg['From'] = mailFrom
    msg['To'] = mailTo
    
    body = "\'OUT CPS\' HOST TRIGGER OCCURED\n * Host IP: %s" % ip
    msg.attach(MIMEText(body, 'plain'))

    try:
        s = smtplib.SMTP_SSL(serverIP, serverPort)
        s.login(serverAccount, serverPassword)
        s.sendmail(mailFrom, [mailTo], msg.as_string())
        
        mailTo = 'uhurue@openbase.co.kr'
        msg['To'] = mailTo
        s.sendmail(mailFrom, [mailTo], msg.as_string())
        
        s.quit()
    except smtplib.SMTPAuthenticationError as e:
        print ("SMTP authentication error: %s" % e)
    except TimeoutError as e:
        print ("mail server connection timeout error: %s" % e)
    else:
        print (ip + ": out cps Emailing")
        
class Trigger(HostTrigger):
    def trigger(self):
        print (self.ip + ": out cps trigger on")
        obj = self.pldb.object_get(OBJECTNAME)
        if obj is None:
            print ("Couldn't find object '%s'" % OBJECTNAME)
            return
        obj2 = self.pldb.object_get(OBJECTNAME + "/" + self.ip)
        if obj2 is None: 
            print (self.ip + ": new host")
            sendEmail(self.ip)
        else:
            print (self.ip + ": existing host")

        self.pld.dyn_add(obj.id, self.ip)


    def reset(self):
        print (self.ip + ": out cps trigger off")
        obj = self.pldb.object_get(OBJECTNAME)
        if obj is None:
            return
        self.pld.dyn_remove(obj.id, self.ip)
