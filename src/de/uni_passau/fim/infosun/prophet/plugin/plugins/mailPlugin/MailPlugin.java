package de.uni_passau.fim.infosun.prophet.plugin.plugins.mailPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.SpinnerNumberModel;

import de.uni_passau.fim.infosun.prophet.experimentViewer.EViewer;
import de.uni_passau.fim.infosun.prophet.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.util.Pair;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.SettingsList;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsComboBox;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsPasswordField;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsSpinner;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsTextField;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;
import static de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode.Type.EXPERIMENT;

/**
 * A <code>Plugin</code> that can send the folder that results from an experiment run (containing answers.xml and other
 * files) as an attachment to a predefined Email address.
 */
public class MailPlugin implements Plugin {

    private static final String KEY = "sendmail";
    private static final String SMTP_SERVER = "smtp_server";
    private static final String SMTP_USER = "smtp_user";
    private static final String SMTP_PASS = "smtp_pass";
    private static final String SMTP_SENDER = "smtp_sender";
    private static final String SMTP_RECEIVER = "smtp_receiver";
    private static final String SMTP_SERVER_PORT = "smtp_server_port";
    private static final String SMTP_SERVER_SEC = "smtp_server_sec";

    private static final String SEC_NONE = "-";
    private static final String SEC_STARTTLS = "STARTTLS";
    private static final String SEC_SSL_TLS = "SSL/TLS";

    private boolean enabled;
    private String smtpServer;
    private String smtpUser;
    private String smtpPass;
    private String smtpSender;
    private String smtpReceiver;
    private String smtpSec;
    private int smtpPort;

    private EViewer experimentViewer;

    @Override
    public Setting getSetting(QTreeNode node) {

        if (node.getType() != EXPERIMENT) {
            return null;
        }

        Attribute mainAttribute = node.getAttribute(KEY);
        SettingsList settingsList = new SettingsList(mainAttribute, getClass().getSimpleName(), true);
        settingsList.setCaption(getLocalized("MAIL_SEND_MAIL"));

        Attribute subAttribute = mainAttribute.getSubAttribute(SMTP_SENDER);
        Setting subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(getLocalized("MAIL_SMTP_SENDER") + ':');
        settingsList.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_RECEIVER);
        subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(getLocalized("MAIL_SMTP_RECIPIENT") + ':');
        settingsList.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_SERVER);
        subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(getLocalized("MAIL_SMTP_SERVER") + ':');
        settingsList.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_SERVER_PORT);
        subSetting = new SettingsSpinner(subAttribute, null, new SpinnerNumberModel(587, 0, 65535, 1));
        subSetting.setCaption(getLocalized("MAIL_SMTP_SERVER_PORT") + ':');
        settingsList.addSetting(subSetting);

        List<Pair<String, String>> items = new ArrayList<>();
        items.add(Pair.of(SEC_NONE, SEC_NONE));
        items.add(Pair.of(SEC_STARTTLS, SEC_STARTTLS));
        items.add(Pair.of(SEC_SSL_TLS, SEC_SSL_TLS));

        subAttribute = mainAttribute.getSubAttribute(SMTP_SERVER_SEC);
        subSetting = new SettingsComboBox(subAttribute, null, items);
        subSetting.setCaption(getLocalized("MAIL_SMTP_SECURITY") + ':');
        settingsList.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_USER);
        subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(getLocalized("MAIL_SMTP_USER") + ':');
        settingsList.addSetting(subSetting);

        subAttribute = mainAttribute.getSubAttribute(SMTP_PASS);
        subSetting = new SettingsPasswordField(subAttribute, null);
        subSetting.setCaption(getLocalized("MAIL_SMTP_PASSWORD") + ':');
        settingsList.addSetting(subSetting);

        return settingsList;
    }

    @Override
    public void experimentViewerRun(EViewer experimentViewer) {
        this.experimentViewer = experimentViewer;
    }

    @Override
    public boolean denyEnterNode(QTreeNode node) {
        return false;
    }

    @Override
    public void enterNode(QTreeNode node) {

        if (node.getType() != EXPERIMENT) {
            return;
        }

        enabled = node.containsAttribute(KEY) && Boolean.parseBoolean(node.getAttribute(KEY).getValue());

        if (enabled) {
            Attribute attributes = node.getAttribute(KEY);

            smtpServer = attributes.getSubAttribute(SMTP_SERVER).getValue();
            smtpUser = attributes.getSubAttribute(SMTP_USER).getValue();
            smtpPass = SettingsPasswordField.decode(attributes.getSubAttribute(SMTP_PASS).getValue());
            smtpSender = attributes.getSubAttribute(SMTP_SENDER).getValue();
            smtpReceiver = attributes.getSubAttribute(SMTP_RECEIVER).getValue();
            smtpSec = attributes.getSubAttribute(SMTP_SERVER_SEC).getValue();
            smtpPort = Integer.parseInt(attributes.getSubAttribute(SMTP_SERVER_PORT).getValue());
        }
    }

    @Override
    public String denyNextNode(QTreeNode currentNode) {
        return null;
    }

    @Override
    public void exitNode(QTreeNode node) {

    }

    @Override
    public String finishExperiment() {

        if (enabled) {
            ZFile attachment = new ZFile(experimentViewer.getSaveDir().toURI());

            try {
                sendEMail(experimentViewer.getSaveDir().getName(), "", attachment.zip().orElse(null));
            } catch (MessagingException e) {
                System.err.println("Could not send the EMail containing the zipped experiment results.");
                System.err.println(e.getMessage());

                return getLocalized("MAIL_MESSAGE_COULD_NOT_SEND_MAIL");
            }
        }

        return null;
    }

    /**
     * Sends an EMail with the given <code>subject</code>, <code>text</code> and optionally an attachment file to
     * the address configured via the <code>Plugin</code> settings.
     *
     * @param subject
     *         the subject line for the EMail
     * @param text
     *         the text for the EMail
     * @param attachmentFile
     *         the attachment file or <code>null</code> for no attachment file
     *
     * @throws MessagingException
     *         if there is an error creating or sending the EMail
     */
    private void sendEMail(String subject, String text, File attachmentFile) throws MessagingException {
        Properties properties = new Properties();

        properties.setProperty("mail.smtp.host", smtpServer);
        properties.setProperty("mail.smtp.port", String.valueOf(smtpPort));
        properties.setProperty("mail.smtp.auth", "true");

        if (SEC_STARTTLS.equals(smtpSec)) {
            properties.setProperty("mail.smtp.starttls.enable", "true");
        } else if (SEC_SSL_TLS.equals(smtpSec)) {
            properties.setProperty("mail.smtp.ssl.enable", "true");
        }

        Session session = Session.getInstance(properties);
        Transport transport = session.getTransport("smtp");
        Message message = new MimeMessage(session);

        MimeMultipart content = new MimeMultipart();
        MimeBodyPart textPart = new MimeBodyPart();

        textPart.setText(text);
        content.addBodyPart(textPart);

        if (attachmentFile != null) {
            DataSource fileDataSource = new FileDataSource(attachmentFile);
            BodyPart attachmentPart = new MimeBodyPart();

            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
            attachmentPart.setFileName(attachmentFile.getName());
            content.addBodyPart(attachmentPart);
        }

        message.setContent(content);
        message.setSentDate(new Date());
        message.setFrom(new InternetAddress(smtpSender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(smtpReceiver, false));
        message.setSubject(subject);

        try {
            transport.connect(smtpUser, smtpPass);
            transport.sendMessage(message, message.getAllRecipients());
        } finally {
            transport.close();
        }
    }
}
