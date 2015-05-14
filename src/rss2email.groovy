@Grapes(@Grab(group = 'log4j', module = 'log4j', version = '1.2.17'))
import groovy.util.logging.Log4j
import org.apache.log4j.*
@Grapes(@Grab(group = 'org.mapdb', module = 'mapdb', version = '1.0.7'))
import org.mapdb.*

import javax.mail.Session
import javax.mail.Transport
@Grapes(@Grab(group = 'org.apache.geronimo.javamail', module = 'geronimo-javamail_1.4_mail', version = '1.8.4'))
import javax.mail.internet.*

class MailSender {
    private Session lSession
    private Transport transporter

    MailSender(String host, String port) {
        Properties mprops = new Properties();
        mprops.setProperty("mail.transport.protocol", "smtp");
        mprops.setProperty("mail.host", host);
        mprops.setProperty("mail.smtp.port", port);

        lSession = Session.getDefaultInstance(mprops, null)
        transporter = lSession.getTransport("smtp");
        transporter.connect();
    }

    void send(String subject, String message, String toAddress, String fromAddress) {
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
        msg.setContent(message, "text/html; charset=\"UTF-8\"")

        transporter.send(msg);
    }
}

@Log4j
class Rss2Email {
    Rss2Email(CfgParser cfgParser) {
        ConfigObject config = cfgParser.config
        Opts opts = cfgParser.opts

        DB db = DBMaker.newFileDB(new File(config.rss2emaildb as String))
                .make()

        Map<String, String> urls = config.urls

        urls.each { String rssId, String url ->
            log.debug("Processing RSS: $rssId -> $url")

            BTreeMap map = db.getTreeMap(rssId)

            def rss = new XmlSlurper().parse(url)

            List items = rss.channel.item.grep()

            List newItems = items.findAll {
                String uid = uid(it)
                !map.containsKey(uid)
            }

            MailSender mailSender = null
            if (!opts.doNotSend)
                mailSender = new MailSender(config.smtpHost as String, config.smtpPort as String)

            int limitNewCnt = config.limitNewCnt

            log.info("Encountered cnt=${items.size()}, new=${newItems.size()}, limiting to $limitNewCnt")

            if (limitNewCnt > 0 && newItems.size() > limitNewCnt) {
                newItems = newItems.subList(0, limitNewCnt)
            }

            newItems.each {
                log.debug("New: (${it.creator}) ${it.title}")

                map[uid(it)] = true

                String subj = "[$rssId] $it.title"

                StringBuilder body = new StringBuilder()
                body << '<h1><a href="' << it.link << '" target="_blank">' << it.title << '</a></h1>'
                body << it.description

                String from = '"' + (it.creator as String).replace('"', "'") + '" <' + config.emailFrom + '>'

                if (!opts.doNotSend)
                    mailSender.send(subj, body.toString(), config.emailTo as String, from)
                else
                    log.info('  Not sending.')
            }
        }

        db.commit()
        db.close()
    }

    static String uid(def item) {
        item.guid ?: item.link
    }
}

class Opts {
    boolean doNotSend
}

@Log4j
class CfgParser {
    ConfigObject config
    Opts opts

    CfgParser(String[] args) {
        def cliBuilder = new CliBuilder(usage: 'groovy rss2email.groovy [options] config_file')

        cliBuilder.d('do not send emails (useful for initial run)')

        OptionAccessor options = cliBuilder.parse(args)

        if (!options) {
            cliBuilder.usage()
            System.exit(1)
        }

        List<String> arguments = options.arguments()

        if (!arguments) {
            log.error("Provide config_file!")
            cliBuilder.usage()
            System.exit(1)
        }

        String fName = arguments[0]
        File cfgFile = new File(fName)

        if (!cfgFile.isFile()) {
            log.error("Not a valid config_file: $fName")
            cliBuilder.usage()
            System.exit(1)
        }

        opts = new Opts()
        if (options.d)
            opts.doNotSend = true

        def configSlurper = new ConfigSlurper()
        config = configSlurper.parse(cfgFile.toURI().toURL())
    }
}

class Log4jInit {
    public static String PATTERN = "%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1} - %m%n"

    static void init(String file) {
        LogManager.resetConfiguration()
        BasicConfigurator.configure(new ConsoleAppender(new PatternLayout(PATTERN)))
        LogManager.rootLogger.level = Level.DEBUG
    }
}

Log4jInit.init(null)
new Rss2Email(new CfgParser(args))
//new MailSender(config.smtpHost as String, config.smtpPort as String).send("test subj 1", "<b>test</b> <i>body</i>", "xonixx@gmail.com", "\"Иван@host.com\" <rss2email@example.com>")