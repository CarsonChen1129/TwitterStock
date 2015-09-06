package FetchTweets;

import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 * Apply J48Tree from weka to the data set
 * @author carsonchen
 *
 */
public class J48Tree {

	/**
	 * J48Tree class constructor
	 * @param path
	 */
	public J48Tree(String path) {
			
				Instances trainIns = null;
				J48 j;
				
				try	{
			          
			           
					   File output 			= new File("output.txt");
			    	   FileWriter write 	= new FileWriter(output);
			    	   write.write("Applying J48 Algorithm......\n");
			           File file			= new File(path);
			           ArffLoader loader 	= new ArffLoader();
			           loader.setFile(file);
			           trainIns 			= loader.getDataSet();
			          
			           
			           trainIns.setClassIndex(trainIns.numAttributes()-1);
			           
			           j = (J48)Class.forName("weka.classifiers.trees.J48").newInstance();
			           
			           j.buildClassifier(trainIns);
			           
			           Evaluation eval = new Evaluation(trainIns);
			           
			           eval.crossValidateModel(j, trainIns, 10, new Random(1));
			           
			           
			              try {
			            	  write.write(eval.toClassDetailsString());
			            	  write.write("\n");
			            	  write.write(eval.toCumulativeMarginDistributionString());
			            	  write.write("\n");
			            	  write.write(eval.toSummaryString());
			            	  write.write("\n");
			            	  write.write(eval.toMatrixString());
			            	  write.write("\n");
			            	  
			              } catch(Exception e) {
			            	  e.getMessage();
			              }
			              write.close();
			           
			} catch(Exception e) {
		        e.printStackTrace();
		    }
	}
}
