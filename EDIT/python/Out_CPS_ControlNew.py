"""
Adds the host to OBJECTNAME

"""
import smtplib
import threading

from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

mailFrom = 'snowcug@gmail.com'
mailTo = 'uhurue@openbase.co.kr'
serverIP = '173.194.72.108'
serverPort = 465
serverAccount = 'snowcug@gmail.com'
serverPassword = 'rlekflek'
subject = "\'OUT CPS\' HOST TRIGGER OCCURED"

OBJECTNAME = "/NetObjects/Host_Trigger_Object/Out_CPS_Control"

def sendEmail():
    msg = MIMEMultipart()
    msg['Subject'] = subject
    msg['From'] = mailFrom
    msg['To'] = mailTo
    
    body = "\'OUT CPS\' HOST TRIGGER OCCURED\n * Host IP: %s" % "test test test"
    msg.attach(MIMEText(body, 'plain'))

    try:
        s = smtplib.SMTP_SSL(serverIP, serverPort)
        s.login(serverAccount, serverPassword)
        s.sendmail(mailFrom, [mailTo], msg.as_string())
        s.quit()
    except smtplib.SMTPAuthenticationError as e:
        print ("SMTP authentication error: %s" % e)
    except TimeoutError as e:
        print ("mail server connection timeout error: %s" % e)
    else:
        print ("\'OUT CPS\' HOST TRIGGER emailing done.")
        
class Trigger(HostTrigger):
    def trigger(self):
        #print ("start - - - ")
        #print ("self.ip = " % self.ip)
        obj = self.pldb.object_get(OBJECTNAME)
        if obj is None:
            print ("Couldn't find object '%s'" % OBJECTNAME)
            return
        self.pld.dyn_add(obj.id, self.ip)
        #body = "\'OUT CPS\' HOST TRIGGER OCCURED\n * Host IP: %s" % self.ip
        sendEmail()
        #th = threading.Thread(target=sendEmail)
        #th.start()

    def reset(self):
        obj = self.pldb.object_get(OBJECTNAME)
        if obj is None:
            return
        self.pld.dyn_remove(obj.id, self.ip)
