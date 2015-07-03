# Defining Python Source Code Encodings
# -*- coding: utf-8 -*-

import urllib
import requests

"""
#간단 예제. http 웹페이지 다운로드하기 - 파일로 받기 버전
urlFile = urllib.request.URLopener()
urlFile.retrieve("http://www.daum.net", "aaaaaa.html") #스크립트와 같은 경로에 파일이 내려왔다.

"""


"""
# 요청을 다양하게 보내기
# 응답 항목도 다양하게 받기
r = requests.get('http://www.daum.net')
print(r.headers['content-type'])
print(r.encoding)
print(r.text)
#print (r.content)

#r = requests.put("http://httpbin.org/put")
#r = requests.delete("http://httpbin.org/delete")
#r = requests.head("http://httpbin.org/get")
#r = requests.options("http://httpbin.org/get")
"""

# ssl, 자동로그인
"""
<div id="login-container" class="container">
  <form accept-charset="UTF-8" action="/login/login" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="xmdshrpYltl5UVW+saFfasOE8Ntl456sfjnZDLIcC5w=" /></div>
    <div class="panel form-signin">
      <div class="panel-body">
        <table>
          <tr>
            <td class="form-column">
              <div class="form-container">
                <h2 class="form-signin-heading">Please Log In</h2>
                <div class="form-group input-group auth focus">
                  <label for="user_account" class="visuallyhidden">Username:</label>
                  <span class="input-group-addon"><i class="icon-user"></i></span>
                  <input autofocus="autofocus" class="form-control auth" id="user_account" maxlength="30" name="user[account]" placeholder="Enter User Name" size="30" type="text" />
                </div>
                <div class="form-group input-group auth">
                  <label for="user_password" class="visuallyhidden">Password:</label>
                  <span class="input-group-addon"><i class="icon-lock"></i></span>
                   <input autocomplete="off" class="form-control auth" id="user_password" maxlength="128" name="user[password]" placeholder="Enter Password" size="128" type="password" />
                </div>
                <input type="hidden" id="window_size" name="window_size"/>
                <button id="logInButton" class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
              </div>
            </td>
            <td class="banner-column">
              <div class="banner-container">
              </div>
            </td>
          </tr>
        </table>
      </div>
    </div>
</form>
"""
#import ClientCookie

form = { user[account]: 'admin', user[password]: 'openbase' } # 위 FireEye form 블록의 input 변수와 맞춰야 한다.
qstring = urllib.urlencode(form)

r = requests.get('https://61.82.88.214/login/login?%s' % qstring)
print(r.text)
