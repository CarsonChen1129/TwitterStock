package FetchTweets;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * 
 * @author carsonchen
 *
 */
public class FetchTweets {

	/* The parameters for connecting the Twitter API */
	private static String consumer_key 		= "WvncVNAYwfC9Hh1KyQ1VAv3Yv";
	private static String consumer_secret 	= "n7gDcIypn7MfbwrQKXU4igmFo02TdgsWS14jiWqoNz9lztG36S";
	private static String access_token		= "196184627-NzpAgCf6CD0Blm4EUozSmLLQBcdLLHJqNmRvpQku";
	private static String access_secret		= "uhx0blZY1AnnAOXnqfU5K2QhI4EBmVgbr4o5gdUWNNmGK";
	
	private static Set<String> set;
	private static String date;
	
	/* The delimiter settings for the csv writer */
	private static final CsvPreference DELIMITED = new CsvPreference.Builder(' ', '|', "\n").build();
		
	public FetchTweets() throws Exception  {		
		
		/* Initialize some calendar objects and a date formatter to transform the date format */
		SimpleDateFormat formatter 		= new SimpleDateFormat("dd-MM-yyyy");
		Calendar calendar 				= new GregorianCalendar();  
		int hourOfDay	  				= calendar.get(calendar.HOUR_OF_DAY);
				
		/* If the time when collecting the data is later than 14:00, 
		 * input those data into the diretory that predicts the stock price tomorrow  */
		if (hourOfDay > 16) {
			calendar.add(calendar.DAY_OF_MONTH, 1);
			date = formatter.format(calendar.getTime());
			calendar.add(calendar.DAY_OF_MONTH, -1);
		}
		
		date = formatter.format(calendar.getTime());
		
		System.out.println(calendar.get(calendar.DAY_OF_MONTH));

		/* Create a directory to store the tweets data if the directory does not exists */
		File tweetsData = new File("tweetsData");
		if (!(tweetsData.exists()) && !(tweetsData.isDirectory())) {
			tweetsData.mkdir();
		}
		String filePath = tweetsData.getAbsolutePath();
		/* Create a directory to store the tweets data based on the date if the directory does not exists */
		File dir = new File("tweetsData/"+date);
		if (!(dir.exists()) && !(dir.isDirectory())) {
			dir.mkdir();
		}
		
		/* Conenct to the Twitter API */
		Twitter twitter 		= TwitterFactory.getSingleton();	
		twitter.setOAuthConsumer(consumer_key, consumer_secret);	
		AccessToken accessToken = new AccessToken(access_token,access_secret);
		twitter.setOAuthAccessToken(accessToken);
		
		/* A list of company keywords */
		String[] companies = {"$VZ","$CMCSA","$F","$GT","$MPAA","$SMP","$GPRO","$COKE","$DPS","$MNST","$PEP","$CPB","$GIS","$K","$KRFT",
                "$TSN","$SJM","$CL","$PG","$REV","$MORN","$LOGI","$HPQ","$AMND","$AAPL","$BBRY","$GRMN","$MSI","$COF",
                "$CIT","$BAC","$WFC","$ANF","$ARO","$AEO","$BURL","$DSW","$EXPR","$FOSL","$URBN","$JCP","$KSS","$M",
                "$DG","$DLTR","$TGT","$WMT","$CVS","$WAG","$BBY","$HHG","$RSH","$SHOS","$SWY","$WFM","$LOW","$HD","$AMZN",
                "$OSTK","$ADBE","$CSCO","$YHOO","$FB","$GOOG","$MSFT"};
		
		/* Interate the list of company keywords, query the Tiwtter API and save the data */
		for (int i=0;i<companies.length;i++) {
			
			/* Initialize the HashSet to store the tweets and eliminate the duplicated data */
			set = new HashSet<String>();
			
			/* Initialize the query */
			Query query = new Query(companies[i]);
			query.count(100);
			query.lang("en");
			QueryResult result;
			int count = 0;
			
			/* Get query result from the Twitter API and store the tweets */
			result 				= twitter.search(query);
			List<Status> tweets = result.getTweets();
			
			/* A for loop to interate the tweets data and store them into the hashset */
			for (Status tweet : tweets) {
				
				System.out.println(tweet.getCreatedAt()+": @"+tweet.getUser().getScreenName()+"-"+tweet.getText());
				//writer.write(tweet.getCreatedAt(),tweet.getText());
				set.add(tweet.getText());
				System.out.println("Count is: "+count);
				count++;
			}
			
			/* Call the write method to write the data into a csv file */
			write(companies[i]);
				
			
		}	
		
		System.out.println("Done");
		JOptionPane.showMessageDialog(null, "Data from Twitter has been saved to the local directory:\n"+filePath);
		new Menu();
		Menu.main(null);
	}
	
	/**
	 * 
	 * @param company
	 * @throws Exception
	 */
	public static void write(String company) throws Exception {
		
		/* Initialize a csv writer */
	    FileWriter fw 			= new FileWriter("tweetsData/"+date+"/"+company+".csv",true);
		CsvListWriter writer 	= new CsvListWriter(fw,DELIMITED);
		
		/* Interate the HashSet and write the tweets into a csv file */
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String tweet = iterator.next();
			writer.write(tweet);
		}
		
		writer.close();
	    System.out.println("Company "+company+" done");
	}

}
