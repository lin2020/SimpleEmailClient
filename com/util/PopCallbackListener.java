package com.lin.util;

public interface PopCallbackListener {
    // 连接服务器
    void onConnect();
    // 检查邮件列表
    void onCheck();
    // 下载邮件
    void onDownLoad(long total_email_size, long download_email_size,
                    int total_email_count, int download_email_count,
                    long current_email_size, long current_download_size);
    // 下载完成
    void onFinish();
    // 下载出错
    void onError();
    // 取消下载
    boolean onCancel();
}
