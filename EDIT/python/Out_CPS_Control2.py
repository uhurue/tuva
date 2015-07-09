"""
Adds the host to OBJECTNAME
"""

# 이메일을 보내기 위한 smtplib 모듈을 import 한다
import smtplib
 
# 이메일을 보내기 위한 email 모듈을 import 한다
from email.mime.text import MIMEText

me = 'snowcug@gmail.com'
you = 'uhurue@openbase.co.kr'

OBJECTNAME = "/NetObjects/Host_Trigger_Object/Out_CPS_Control"

class Trigger(HostTrigger):
    def trigger(self):
        obj = self.pldb.object_get(OBJECTNAME)
        if obj is None:
            print "Couldn't find object '%s'" % OBJECTNAME
            return
        
        self.pld.dyn_add(obj.id, self.ip)
        msg = MIMEText('AAAAAAAAAAAAA')

        msg['Subject'] = 'CPS LIMIT REACHED: client: %s' % '10.10.10.100'
        msg['From'] = me
        msg['To'] = you 

        s = smtplib.SMTP_SSL('smtp.gmail.com', 465)
        s.login('snowcug@gmail.com', 'gndlswj9')
        s.sendmail(me, [you], 'CPS LIMIT REACHED')
        s.quit()
        print "Email sent for CPS OVER"

    def reset(self):
        obj = self.pldb.object_get(OBJECTNAME)
        if obj is None:
            return
        self.pld.dyn_remove(obj.id, self.ip)
