/**
 * Map of form: rss_id -> rss_url
 */
urls = [
        'укр': 'http://zlo.rt.mipt.ru:7500/search?rss&st=adv&text=%D1%83%D1%80%D0%BA%D0%B0%D0%B8%D0%BD%D0%B0+%D1%83%D0%BA%D1%80*+%D1%81%D0%B2%D0%B8%D0%B4%D0%BE*+%D1%85%D0%BE%D1%85%D0%BE%D0%BB+%D1%85%D0%BE%D1%85%D0%BB*+%D0%BA%D1%80%D1%8B%D0%BC+%D0%BA%D1%80%D1%8B%D0%BC%D0%BD%D0%B0%D1%88+%D1%85%D1%83%D0%BD%D1%82%D0%B0+%D0%BA%D0%B0%D1%80%D0%B0%D1%82%D0%B5%D0%BB%D0%B8+%D0%B1%3F%D0%BD%D0%B4%D0%B5%D1%80*+%D0%B4%D0%BD%D1%80+%D0%BB%D0%BD%D1%80+%D0%BD%D0%BE%D0%B2%D0%BE%D1%80%D0%BE%D1%81%D1%81%D0%B8%D1%8F+%D0%B7%D0%B0%D1%85%D0%B0%D1%80%D1%87%D0%B5%D0%BD%D0%BA%D0%BE+%22%D1%80%D1%83%D1%81%D1%81%D0%BA%D0%B8%D0%B9+%D0%BC%D0%B8%D1%80%22+%D0%B3%D0%B8%D1%80%D0%BA%D0%B8%D0%BD+%D0%B1%D0%BE%D1%80%D0%BE%D0%B4%D0%B0%D0%B9+%D0%B3%D1%83%D0%B1%D0%B0%D1%80%D0%B5%D0%B2+%D0%BC%D0%B0%D0%B9%D0%B4%D0%B0%D0%BD+%D0%BC%D0%B0%D0%B9%D0%B4%D0%B0%D0%BD*+%D0%BC%D0%B0%D0%B9%D0%B4%D0%B0%D1%83%D0%BD+%D1%8F%D0%BD%D1%83%D0%BA%D0%BE%D0%B2%D0%B8%D1%87+%D0%BA%D0%BB%D0%B8%D1%87%D0%BA%D0%BE+%D0%BB%D1%8F%D1%88%D0%BA%D0%BE+%D0%BF%D0%BE%D1%80%D0%BE%D1%88%D0%B5%D0%BD%D0%BA%D0%BE+%D1%8F%D1%86%D0%B5%D0%BD%D1%8E%D0%BA+%D1%8F%D0%B9%D1%86%D0%B5%D0%BD%D1%8E%D1%85+%D0%B0%D0%B2%D0%B0%D0%BA%D0%BE%D0%B2+%D1%8F%D1%80%D0%BE%D1%88+%22%D0%BF%D1%80%D0%B0%D0%B2%D1%8B%D0%B9+%D1%81%D0%B5%D0%BA%D1%82%D0%BE%D1%80%22+%D0%B4%D0%BE%D0%BD%D0%B5%D1%86%D0%BA+%D0%BB%D1%83%D0%B3%D0%B0%D0%BD%D1%81%D0%BA+%D0%B4%D0%BE%D0%BD%D0%B1%D0%B0%D1%81%D1%81&topic=-1&inTitle=on&inBody=on&nick=&host=&sep=&site=0&pageSize=0&sort=date&submitBtn=%D0%98%D1%81%D0%BA%D0%B0%D1%82%D1%8C%21',
        'xonix': 'http://zlo.rt.mipt.ru:7500/search?rss&st=adv&text=%D0%BA%D1%81%D0%BE%D0%BD%D0%B8%D0%BA%D1%81+xonix+%D0%B1%D0%BE%D1%80%D0%B4%D0%BE%D0%BF%D0%BE%D0%B8%D1%81%D0%BA&topic=-1&inTitle=on&inBody=on&nick=&host=&sep=&site=0&pageSize=0&sort=date',
]

def FOLDER='/home/users/xonix/rss2email'
rss2emaildb = "$FOLDER/.rss2email.db"

emailTo = 'xonixx@gmail.com'
emailFrom = 'rss2email@example.com'

/**
 * Limit of new items processed per time
 * 0 - unlimited
 */
limitNewCnt = 0

smtpHost = 'localhost'
smtpPort = 25

/**
 * log file location - optional
 */
logFile="$FOLDER/rss2email.log"