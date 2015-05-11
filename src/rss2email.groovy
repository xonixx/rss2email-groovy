@Grapes(@Grab(group = 'log4j', module = 'log4j', version = '1.2.17'))
import groovy.util.logging.Log4j
@Grapes(@Grab(group = 'org.mapdb', module = 'mapdb', version = '1.0.7'))
import org.mapdb.*

import javax.mail.Session
import javax.mail.Transport
@Grapes(@Grab(group = 'org.apache.geronimo.javamail', module = 'geronimo-javamail_1.4_mail', version = '1.8.4'))
import javax.mail.internet.*

class MailSender {
    static void sendmail(String message, String subject, String toAddress, String fromAddress, String host, String port) {
        Properties mprops = new Properties();
        mprops.setProperty("mail.transport.protocol", "smtp");
        mprops.setProperty("mail.host", host);
        mprops.setProperty("mail.smtp.port", port);

        Session lSession = Session.getDefaultInstance(mprops, null);
        MimeMessage msg = new MimeMessage(lSession);

        StringTokenizer tok = new StringTokenizer(toAddress, ";");
        ArrayList emailTos = new ArrayList();
        while (tok.hasMoreElements()) {
            emailTos.add(new InternetAddress(tok.nextElement().toString()));
        }
        InternetAddress[] to = new InternetAddress[emailTos.size()];
        to = (InternetAddress[]) emailTos.toArray(to);
        msg.setRecipients(MimeMessage.RecipientType.TO, to);
        msg.setFrom(new InternetAddress(fromAddress));
        msg.setSubject(subject);
//        msg.setText(message)
        msg.setContent(message, "text/html")

        Transport transporter = lSession.getTransport("smtp");
        transporter.connect();
        transporter.send(msg);
    }
}

@Log4j
class Rss2Email {
    Rss2Email(ConfigObject config) {
        String url = config.url

        log.debug("Processing URL: $url")

        DB db = DBMaker.newFileDB(new File(config.rss2emaildb as String))
//                .closeOnJvmShutdown()
                .make()

        BTreeMap map = db.getTreeMap(url)

        def rss = new XmlSlurper().parse(url)

        List items = rss.channel.item.grep()

        List newItems = items.findAll {
            String uid = uid(it)
            !map.containsKey(uid)
        }

        newItems.each {
            println('------------------------')
            println("title: ${it.title}")
            println("creator: ${it.creator}")
            println("date: ${it.date}")

            map[uid(it)] = true
        }

        db.commit()
        db.close()
    }

    static String uid(def item) {
        item.guid ?: item.link
    }
}

@Log4j
class CfgParser {
    ConfigObject config

    CfgParser(String[] args) {
        if (args.length == 0) {
            log.error("Provide config file!")
            System.exit(1)
        }

        String fName = args[0]
        File cfgFile = new File(fName)

        if (!cfgFile.isFile()) {
            log.error("Not a valid config file: $fName")
            System.exit(1)
        }

        def configSlurper = new ConfigSlurper()
        config = configSlurper.parse(cfgFile.toURI().toURL())
    }
}

new Rss2Email(new CfgParser(args).config)
//MailSender.sendmail("<b>test</b> <i>body</i>", "test subj", "xonixx@gmail.com", "\"Иван@host.com\" <rss2email@example.com>", "localhost", "10025")