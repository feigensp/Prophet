package test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
 
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
 
 
class SimpleMail {
 
    // login data
    private static final String TYPE = "smtp";
    private static final String HOST = "localhost";
    private static final String USER = "sysadmin";
    private static final String PASSWD = "password";
    private static final int PORT = 25;
    // send data
    private static final String SENDER = "sysadmin@localhost";
    private static final String RECIPIENT = "hasselbe@gmail.com";
 
    public static void main(String[] args) throws Exception {
        sendMail();
    }
 
    private static void sendMail() throws NoSuchProviderException, MessagingException,
            AddressException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);
        Transport tr = session.getTransport(new URLName(TYPE, HOST, PORT, null, USER, PASSWD));
        Message message = new MimeMessage(session);
        message.addRecipient(RecipientType.TO, new InternetAddress(RECIPIENT));
        message.addFrom(new InternetAddress[] { new InternetAddress(SENDER) });
        //subject
        message.setSubject("the subject");
        //the multipart
        MimeMultipart multiPart = new MimeMultipart("mixed");
        // a plaintext part
        MimeBodyPart body1 = new MimeBodyPart();
        body1.setContent("blub", "text/plain");
        multiPart.addBodyPart(body1);
        // a html part
        MimeBodyPart body2 = new MimeBodyPart();
        body2.setContent("<b>hallo welt</b>", "text/html");
        multiPart.addBodyPart(body2);
        // a picture
        MimeBodyPart body3 = new MimeBodyPart();
        body3.attachFile(new File("/home/user/Desktop/picture"));
        multiPart.addBodyPart(body3);
        // set the content to the multipart
        message.setContent(multiPart);
        //send
        tr.connect();
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }
}
