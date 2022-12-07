package com.example.supernotes.utils

import android.util.Log
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class sendmail {

    object SendEmai{
        fun sendMail(user: String?, code: String) {
            val props = Properties()
            props.setProperty("mail.smtp.host","smtp.feishu.cn")
            props.setProperty("mail.transport.protocol", "smtp")
            props.setProperty("mail.smtp.address", "smtp.feishu.cn") // 发件人的邮箱的 SMTP 服务器地址
            props.setProperty("mail.smtp.auth", "true")

            val session = Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication("noteapp@csd.moe", "ABIaW5eLyaRb6eab")
                }
            })
            try {
                val message: Message = MimeMessage(session)
                // 3.1设置发件人
                message.setFrom(InternetAddress("noteapp@csd.moe"))
                // 3.2设置收件人
                message.setRecipient(Message.RecipientType.TO, InternetAddress(user))
                // 3.3设置邮件的主题
                message.subject = "便签APP"
                // 3.4设置邮件的正文
                message.setContent("您的验证码是：$code", "text/html;charset=UTF-8")
                // 4.发送邮件
                Transport.send(message)
                Log.d("adad", "_+_+_+_+_+_+_+_+_+")
            } catch (e: MessagingException) {
                e.printStackTrace()
            }
        }
    }
}