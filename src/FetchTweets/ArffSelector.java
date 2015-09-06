package FetchTweets;

import java.awt.Frame;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

/**
 * A file selector GUI to select arff files
 * @author carsonchen
 *
 */
public class ArffSelector {

	private FileFilter acceptFileType ;
    private Frame frame = new Frame();
    public JFileChooser fileChooser;
    
    public ArffSelector() {
    	
        acceptFileType = new javax.swing.filechooser.FileFilter() {
            public boolean accept(File file) { 
                String  name = file.getName().toLowerCase();
                return  name.endsWith(".arff")||
                         file.isDirectory();
            }
            public String getDescription() { 
                return "arff file";
            }
        };
        
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
    
       
    }
    
    File openFile() {
    	
    	
    	File currentFile = null;
    	
    	fileChooser.setFileFilter(acceptFileType);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setDialogTitle("Please choose the file");
        
        int result = fileChooser.showOpenDialog(frame);
        
        
        if (result == JFileChooser.APPROVE_OPTION) {
        	
        	currentFile = fileChooser.getSelectedFile();
        	
        }
        
        return currentFile;
    }
}
