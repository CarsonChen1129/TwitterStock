package FetchTweets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author carsonchen
 *
 */
public class EmotionWeight {
	
	/* A Map to store the dictionary */
	private Map<String, Double> dictionary;

	/**
	 * 
	 * @param pathToSWN
	 * @throws IOException
	 */
	public EmotionWeight(String pathToSWN) throws IOException {
		
		/* Initialize a HashMap to store the dictionary */
		dictionary = new HashMap<String, Double>();

		/* From String to list of doubles */
		HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

		BufferedReader csv = null;
		
		try {
			
			csv 			= new BufferedReader(new FileReader(pathToSWN));
			int lineNumber 	= 0;
			String line;
			
			
			while ((line = csv.readLine()) != null) {
				
				lineNumber++;

				/* If it's a comment, skip this line */
				if (!line.trim().startsWith("#")) {
					
					/* Use tab as the separator */
					String[] data = line.split("\t");
					String wordTypeMarker = data[0];

					/* Through exception if the line is not valid */
					if (data.length != 6) {
						throw new IllegalArgumentException("Incorrect tabulation format in file, line: "+ lineNumber);
					}

					/* Calculate synset score as score = PosS - NegS */
					Double synsetScore = Double.parseDouble(data[2]) - Double.parseDouble(data[3]);

					/* Get all Synset terms */
					String[] synTermsSplit = data[4].split(" ");

					/* Go through all terms of current synset */
					for (String synTermSplit : synTermsSplit) {
						
						/* Get synterm and synterm rank */
						String[] synTermAndRank 	= synTermSplit.split("#");
						String synTerm 				= synTermAndRank[0] + "#"+ wordTypeMarker;

						int synTermRank = Integer.parseInt(synTermAndRank[1]);
						/* What we get here is a map of the type:  term -> {score of synset#1, score of synset#2...} */

						/* Add map to term if it doesn't have one */
						if (!tempDictionary.containsKey(synTerm)) {
							
							tempDictionary.put(synTerm,new HashMap<Integer, Double>());
							
						}

						/* Add synset link to synterm */
						tempDictionary.get(synTerm).put(synTermRank,synsetScore);
					}
				}
			}

			/* Go through all the terms */
			for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary.entrySet()) {
				
				String word 						= entry.getKey();
				Map<Integer, Double> synSetScoreMap = entry.getValue();

				/* Calculate weighted average. Weigh the synsets according to their rank */
				/* Score= 1/2*first + 1/3*second + 1/4*third ..... etc */
				/* Sum = 1/1 + 1/2 + 1/3 ... */
				double score = 0.0;
				double sum = 0.0;
				for (Map.Entry<Integer, Double> setScore : synSetScoreMap.entrySet()) {
					
					score += setScore.getValue() / (double) setScore.getKey();
					sum += 1.0 / (double) setScore.getKey();
				}
				score /= sum;

				dictionary.put(word, score);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (csv != null) {
				csv.close();
			}
		}
	}

	/**
	 * 
	 * @param word
	 * @param pos
	 * @return
	 */
	public double extract(String word, String pos) {
		
		try{
			
			/* Return the value if the word is found in the dictionary */
			return dictionary.get(word + "#" + pos);
			
		} catch(Exception e) {
			
			/* Return 0.0 if the word is not found in the dictionary */
			return 0.0;
		}
		
	}

}
