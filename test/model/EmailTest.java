package test.lin.model;

import java.util.*;

import com.lin.util.*;
import com.lin.model.*;

public class EmailTest {
    public static void main(String[] args) {
        // sohu user
        User user = new User(0, "lin", "abc_2020", "abc2020");

        // server response
        Vector<String> response = new Vector<String>();
        response.addElement("X-QQ-mid: webmail574t1491302434t3801917");
        response.addElement("From: \"=?gb18030?B?wda8zraw?=\" <1780615543@qq.com>");
        response.addElement("To: \"=?gb18030?B?YWJjXzIwMjA=?=\" <abc_2020@sohu.com>, \"=?gb18030?B?MTc4MDYxNTU0Mw==?=\" <1780615543@qq.com>");
        response.addElement("Subject: =?gb18030?B?wda8zraw?=");
        response.addElement("Mime-Version: 1.0");
        response.addElement("boundary=\"----=_NextPart_58E37821_0A60FCF8_0A80AE2D\"");
        response.addElement("Feedback-ID: webmail:qq.com:bgweb:bgweb135");
        response.addElement("X-Sohu-Antispam-Language: 0");
        response.addElement("X-Sohu-Antispam-Score: 0.0578011211473");
        response.addElement("This is a multi-part message in MIME format.");
        response.addElement("------=_NextPart_58E37821_0A60FCF8_0A80AE2D");
        response.addElement("Content-Type: text/plain;");
        response.addElement("charset=\"gb18030\"");
        response.addElement("Content-Transfer-Encoding: base64");
        response.addElement("suLK1NPKvP4=");
        response.addElement("------=_NextPart_58E37821_0A60FCF8_0A80AE2D");
        response.addElement("Content-Type: text/html;");
        response.addElement("charset=\"gb18030\"");
        response.addElement("Content-Transfer-Encoding: base64");
        response.addElement("PGRpdj6y4srU08q8/jwvZGl2Pg==");
        response.addElement("------=_NextPart_58E37821_0A60FCF8_0A80AE2D--");

        // test email
        Email email = new Email(user, response);
        LogUtil.i("From: " + email.getFrom());
        LogUtil.i("To_List: ");
        for (String s : email.getTo_list()) {
            LogUtil.i(s);
        }
        LogUtil.i("Subject: " + email.getTheme());
        LogUtil.i("Content:");
        LogUtil.i(email.getContent());
    }
}
