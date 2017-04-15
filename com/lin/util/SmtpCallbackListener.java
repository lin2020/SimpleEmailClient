package com.lin.util;
// smtp 发送邮件的回调接口
public interface SmtpCallbackListener {
    // 建立连接
    void onConnect();
    // 认证登录
    void onCheck();
    // 发送邮件
    void onSend(long send_email_size, long total_email_size);
    // 发送成功
    void onFinish();
    // 发送失败
    void onError();
}
