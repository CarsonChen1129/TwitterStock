package FetchTweets;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * 
 * @author carsonchen
 *
 */
public class Combine {
	
	private static final CsvPreference DELIMITED1 = new CsvPreference.Builder('|', ' ', "\n").build();
	
	private static final CsvPreference DELIMITED2 = new CsvPreference.Builder(' ', '|', "\n").build();
	
	private static Set<String> set;

	public static void main(String[] args) throws Exception {
		
		
		String[] companies = {"$VZ","$CMCSA","$F","$GT","$MPAA","$SMP","$GPRO","$COKE","$DPS","$MNST","$PEP","$CPB","$GIS","$K","$KRFT",
                "$TSN","$SJM","$CL","$PG","$REV","$MORN","$LOGI","$HPQ","$AMND","$AAPL","$BBRY","$GRMN","$MSI","$COF",
                "$CIT","$BAC","$WFC","$ANF","$ARO","$AEO","$BURL","$DSW","$EXPR","$FOSL","$URBN","$JCP","$KSS","$M",
                "$DG","$DLTR","$TGT","$WMT","$CVS","$WAG","$BBY","$HHG","$RSH","$SHOS","$SWY","$WFM","$LOW","$HD","$AMZN",
                "$OSTK","$ADBE","$CSCO","$YHOO","$FB","$GOOG","$MSFT"};
        
		int count = 0;
		
		for (int i =0;i<companies.length;i++) {
			
			set = new HashSet<String>();
						
			count++;
			write(companies[i]);
		}
		 System.out.println("Length is: "+companies.length);
		 System.out.println("Count is: "+count);
		
		 System.out.println("Done");

	}
	
	public static void read(String path) throws Exception {
		
		List<String> row = null;
	    CsvListReader csvReader = new CsvListReader(new FileReader(path),DELIMITED1);
	    System.out.println(path);
	    
	    
	    while ((row=csvReader.read())!=null) {
	    	String str = row.get(0);
	    	set.add(str);
	    }	   
	}
	
	public static void write(String company) throws Exception{
		
		
	    FileWriter fw = new FileWriter("/Users/carsonchen/Desktop/tweetsData/Dec-02/"+company+".csv",true);
		CsvListWriter writer = new CsvListWriter(fw,DELIMITED2);
		
		for(Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String tweet = iterator.next();
			writer.write(tweet);
		}
		writer.close();
	    System.out.println("Company "+company+" done");
	}

}
