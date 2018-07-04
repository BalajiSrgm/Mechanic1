package mechanic.com.mechanic.BusinessHelper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Sailesh GB on 4/17/2017.
 */

public class NetworkHelper extends AsyncTask {

    public static String password;

    public static void sendEmail(String password){
        final String from = "balajisrgm007@gmail.com";
        String to = "balaje.srgm@gmail.com";
        Properties properties = new Properties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port","465");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.port","465");

        Session session = Session.getDefaultInstance(properties,new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(from,"********************");
            }
        });
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            mimeMessage.setSubject("Password of BS Enterprise Login");
            mimeMessage.setText(password);
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }


    @Override
    protected Object doInBackground(Object[] params) {
        final String from = "balajisrgm007@gmail.com";
        String to = "balaje.srgm@gmail.com";
        Properties properties = new Properties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port","465");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.port","465");

        Session session = Session.getInstance(properties,new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(from,"9600Balashan181851@1");
            }
        });
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            mimeMessage.setSubject("Password of BS Enterprise Login");
            mimeMessage.setText(password);
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


        return null;
    }
}
