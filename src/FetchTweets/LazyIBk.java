package FetchTweets;

import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 * Apply LazyIBk from weka to the data set
 * @author carsonchen
 *
 */
public class LazyIBk {

    /**
     * @param args
     */
    public LazyIBk(String path) {
       
	       Instances trainIns = null;
	       IBk cfs = null;
	      
	      
	       try {
	          
	    	   File output 			= new File("output.txt");
	    	   FileWriter write 	= new FileWriter(output);
	    	   write.write("Applying Lazy IBk Algorithm......\n");
	           File file			= new File(path);
	           ArffLoader loader 	= new ArffLoader();
	           
	           loader.setFile(file);
	           trainIns = loader.getDataSet();
	          
	           trainIns.setClassIndex(trainIns.numAttributes()-1);
	              
	           cfs = (IBk)Class.forName("weka.classifiers.lazy.IBk").newInstance();
	           cfs.setKNN(6);
	           cfs.buildClassifier(trainIns);
	          
	          
	           Evaluation eval = new Evaluation(trainIns);
	
	           eval.crossValidateModel(cfs, trainIns, 10, new Random(1));
	          
	              
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
