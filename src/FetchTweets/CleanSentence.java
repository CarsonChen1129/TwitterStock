package FetchTweets;

/**
 * 
 * @author carsonchen
 *
 */
public class CleanSentence {

	public static void main(String[] args) {
		
		//String str = "Video: Understanding The 30-minute Market Update http://t.co/2MCk1cEiiS #trading #startups #stock $AAPL #money #investor #angel";
		String str = "RT @JPDesloges: Forget Mobile Banking, Wearable Banking Is Coming $AAPL #aapl http://t.co/Pp1UhA4ZUi";
		System.out.println(str.replaceAll("https?://\\S+\\s?", "").replaceAll("@\\S+\\s?", ""));

	}

}
