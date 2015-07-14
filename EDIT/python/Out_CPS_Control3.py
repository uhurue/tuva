# Defining Python Source Code Encodings
# -*- coding: utf-8 -*-

import smtplib

from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

def sendEmail():
    mailFrom = 'snowcug@gmail.com'
    mailTo = 'uhurue@openbase.co.kr'
    serverIP = '173.194.72.108'
    serverPort = 465
    serverAccount = 'snowcug@gmail.com'
    serverPassword = 'rlekflek'
    subject = "\'OUT CPS\' HOST TRIGGER OCCURED"

    msg = MIMEMultipart()
    msg['Subject'] = subject
    msg['From'] = mailFrom
    msg['To'] = mailTo
    
    body = "\'OUT CPS\' HOST TRIGGER OCCURED\n * Host IP: %s" % "test test test 0000"
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

sendEmail()