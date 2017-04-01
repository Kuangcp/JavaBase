package cn.util.mail;


import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.util.Date;
import java.util.Properties;

/**
 * 使用QQ邮箱或者是163邮箱进行发送注册地址
 * @author Myth
 * 2017年1月1日 下午7:57:32
 */
public class Sendmail {
    /*
     * 使用QQ邮箱发送邮件
     */
    /*public static void sendMail(String to) {

        final String SendServer="smtp.qq.com";
        final String port = "465";
        final String username="1181951407@qq.com";
        final String passcode = "qunldwmpzdziifgd";
        String title="欢迎使用，请激活！";
        String contents = "<h2>你看到的是一封测试邮件</h2><br/>正如你所见，使用mail组件发送的";

        // 1,獲得session對象
        Properties props = new Properties();
        // props.setProperty("mail.host", "localhost");
        // 163邮箱服务器
        props.setProperty("mail.smtp.host", SendServer);
        // 发送服务需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");
        // 设置端口
        props.setProperty("mail.smtp.port", port);
        // 便于调试
        // props.put("mail.debug", "true");//便于调试

        //SSL加密
        MailSSLSocketFactory sf;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // Session session = Session.getDefaultInstance(props);
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //用户名，密码（授权码）
                return new PasswordAuthentication("1181951407@qq.com", passcode);// 邮箱和第三方登录授权码
            }
        });

        // 2， 创建代表邮件的对象Message
        Message message = new MimeMessage(session);

        try {
            // 设置发件人
            message.setFrom(new InternetAddress(username));
            // 设置收件人
            message.addRecipient(RecipientType.TO, new InternetAddress(to));
            // 设置标题
            message.setSubject(title);
            // 设置发送时间
            message.setSentDate(new Date());
            // 设置正文(有链接选择text/html;charset=utf-8)
            message.setContent(contents, "text/html;charset=utf-8");

            // 3，发送邮件Transport
            Transport.send(message);

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }*/

///////////////////////////////////////////////////////////////////////

    /**
     * 使用163邮箱发送邮件
     * @param to
     */
    public static void send163(String to,String title,String contents){

        final String SendServer="smtp.163.com";
        final String port = "25";
        final String username="15979940275@163.com";
        final String passcode = "asdf1429336";
//        title="请查收你的新年礼物 ^-^";
//        contents = "<h2>年轻人多看书少玩手机</h2><br/><a>http://blog.csdn.net/kcp606</a>";

        Properties props = new Properties();
        props.setProperty("mail.host", SendServer);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.port", port);

        Session session = Session.getDefaultInstance(props, new Authenticator() {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, passcode);// 邮箱和第三方登录授权码
        }

        });

        // 2，创建代表邮件的对象Message
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(username));
            message.addRecipient(RecipientType.TO, new InternetAddress(to));
            message.setSubject(title);
            message.setSentDate(new Date());
            message.setContent(contents, "text/html;charset=utf-8");
            // 3，发送邮件Transport
            Transport.send(message);

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    ////////////////////////////////////////////////////////
    public static void main(String[] a) {
        System.out.println("开始");
//		sendMail("15979940275@163.com");

//		String recieve = "kuangchengping@outlook.com";
        String recieve = "279035863@qq.com";
		send163(recieve,"title","contents");

        System.out.println("end");

    }
}

