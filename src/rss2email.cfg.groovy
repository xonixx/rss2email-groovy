/**
 * Map of form: rss_id -> rss_url
 */
urls = [
        'xonix': 'http://zlo.rt.mipt.ru:7500/search?rss&st=all&text=&topic=-1&inTitle=on&inBody=on&nick=xonix&host=&sep=&site=0&pageSize=0&sort=date&submitBtn=%D0%98%D1%81%D0%BA%D0%B0%D1%82%D1%8C%21',
        'тест': 'http://zlo.rt.mipt.ru:7500/search?rss&st=all&text=%D1%82%D0%B5%D1%81%D1%82&topic=-1&inTitle=on&inBody=on&nick=&host=&sep=&site=0&pageSize=0&sort=date&submitBtn=%D0%98%D1%81%D0%BA%D0%B0%D1%82%D1%8C%21',
]

rss2emaildb = '.rss2email.db'

emailTo = 'xonixx@gmail.com'
emailFrom = 'rss2email@example.com'

/**
 * Limit of new items processed per time
 * 0 - unlimited
 */
limitNewCnt = 3

smtpHost = 'localhost'
smtpPort = 10025

/**
 * log file location - optional
 */
logFile='rss2email.log'