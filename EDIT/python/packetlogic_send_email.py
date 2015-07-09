# 이메일을 보내기 위한 smtplib 모듈을 import 한다
import smtplib
 
# 이메일을 보내기 위한 email 모듈을 import 한다
from email.mime.text import MIMEText
 
me = 'snowcug@gmail.com'
you = 'uhurue@openbase.co.kr'

msg = MIMEText('AAAAAAAAAAAAA')
# you == 받는 사람의 이메일 주소
msg['Subject'] = 'CPS LIMIT REACHED: client: %s' % '10.10.10.100'
msg['From'] = me
msg['To'] = you 
 

# 로컬 SMTP 서버가 없을 경우 계정이 있는 다른 서버를 사용하면 된다.
s = smtplib.SMTP_SSL('smtp.gmail.com', 465)
s.login('snowcug@gmail.com', 'gndlswj9')
s.sendmail(me, [you], 'CPS LIMIT REACHED')
s.quit()
