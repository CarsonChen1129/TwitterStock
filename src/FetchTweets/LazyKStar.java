package FetchTweets;

import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.lazy.KStar;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 * Apply LazyKStar from weka to the data set
 * @author carsonchen
 *
 */
public class LazyKStar {

	/**
	 * class constructor
	 * @param path
	 */
	public LazyKStar(String path) {
		
			Instances trainIns = null;
			KStar star;
			try{
		          
				   File output 			= new File("output.txt");
		    	   FileWriter write 	= new FileWriter(output);
		    	   write.write("Applying Lazy KStar Algorithm......\n");
		           File file			= new File(path);
		           ArffLoader loader 	= new ArffLoader();
		           
		           loader.setFile(file);
		           trainIns = loader.getDataSet();
		          
		           
		           trainIns.setClassIndex(trainIns.numAttributes()-1);
		           
		           star = (KStar)Class.forName("weka.classifiers.lazy.KStar").newInstance();
		           
		           star.buildClassifier(trainIns);
		           
		           Evaluation eval = new Evaluation(trainIns);
		           
		           eval.crossValidateModel(star, trainIns, 10, new Random(1));
		           
		           
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
