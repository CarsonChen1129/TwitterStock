__author__ = 'carsonchen'
#-*- coding: UTF-8 -*-
import twitter
import json
import csv
import datetime
import time
import string
import sys

today = datetime.date.today().strftime("%b-%d-%Y")

print today

CONSUMER_KEY = 'WvncVNAYwfC9Hh1KyQ1VAv3Yv'
CONSUMER_SECRET = 'n7gDcIypn7MfbwrQKXU4igmFo02TdgsWS14jiWqoNz9lztG36S'
OAUTH_TOKEN = '196184627-NzpAgCf6CD0Blm4EUozSmLLQBcdLLHJqNmRvpQku'
OAUTH_TOKEN_SECRET = 'uhx0blZY1AnnAOXnqfU5K2QhI4EBmVgbr4o5gdUWNNmGK'

auth = twitter.oauth.OAuth(OAUTH_TOKEN, OAUTH_TOKEN_SECRET, CONSUMER_KEY, CONSUMER_SECRET)

twitter_api = twitter.Twitter(auth=auth)

companies = ['$VZ','$CMCSA','$F','$GT','$MPAA','$SMP','$GPRO','$COKE','$DPS','$MNST','$PEP','$CPB','$GIS','$K','$KRFT',
                '$TSN','$SJM','$CL','$PG','$REV','$MORN','$LOGI','$HPQ','$AMND','$AAPL','$BBRY','$GRMN','$MSI','$COF',
                '$CIT','$BAC','$WFC','$ANF','$ARO','$AEO','$BURL','$DSW','$EXPR','$FOSL','$URBN','$JCP','$KSS','$M',
                '$DG','$DLTR','$TGT','$WMT','$CVS','$WAG','$BBY','$HHG','$RSH','$SHOS','$SWY','$WFM','$LOW','$HD','$AMZN',
                '$OSTK','$ADBE','$CSCO','$YHOO','$FB','$GOOG','$MSFT']

company_size =len(companies)

for j in range (0,company_size):

    count = 100

    language = 'en'

    date = '2014-11-19'
    d = 'Nov-30-2014'

    query = companies[j]

    print query

    search_results = twitter_api.search.tweets(q=query,count=count,lang='en')

    statuses = search_results['statuses']

    for _ in range(10):
        print "Length of statuses",len(statuses)
        try:
            next_results = search_results['search_metadata']['next_results']
        except KeyError, e:
            break


        kwargs = dict([kv.split('=') for kv in next_results[1:].split("&")])

        search_results = twitter_api.search.tweets(**kwargs)
        statuses +=search_results['statuses']

    #print json.dump(statuses[0],indent=1)

    texts = [status['text'] for status in statuses]

    created_time = [status['created_at'] for status in statuses]

    print dir(texts)
    print type(texts)
    print json.dumps(texts[0:],indent=1)
    print json.dumps(created_time[0:],indent=1)



    txtlength = len(texts)
    filename = query+'@'+today+'.csv'

    with open(filename,'wb') as csvfile:
        tweetfile = csv.writer(csvfile,delimiter=' ',quotechar='|', quoting=csv.QUOTE_MINIMAL)

        for i in range(0,txtlength):
            tweetfile.writerow([string.join([texts[i]]).encode('ascii','ignore')])


    timelength = len(created_time)
    timefilename = query+'-time@'+today+'.csv'

    with open(timefilename,'wb') as csvTime:
        tweettime = csv.writer(csvTime,delimiter=' ',quotechar='|', quoting=csv.QUOTE_MINIMAL)

        for i in range(0,timelength):
            tweettime.writerow([string.join([created_time[i]]).encode('ascii','ignore')])

#time.sleep(60)

print "Success"
