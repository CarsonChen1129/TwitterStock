package FetchTweets;

import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

/**
 * A file selector to open csv files from the computer
 * @author carsonchen
 *
 */
public class JFileSelector {

	private FileFilter acceptFileType ;
    private Frame frame = new Frame();
    public JFileChooser fileChooser;
    public static ArrayList<String> paths = new ArrayList<String>();
    
    public JFileSelector() {
    	
        acceptFileType = new javax.swing.filechooser.FileFilter() {
            public boolean accept(File file) { 
                String  name = file.getName().toLowerCase();
                return  name.endsWith(".csv")||
                         file.isDirectory();
            }
            public String getDescription() { 
                return "csv file";
            }
        };
        
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
    
       
    }
    
    File openFile(String title) {
    	
    	
    	File currentFile = null;
    	
    	fileChooser.setFileFilter(acceptFileType);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setDialogTitle(title);
        
        int result = fileChooser.showOpenDialog(frame);
        
        if (result == JFileChooser.APPROVE_OPTION) {
        	
        	currentFile = fileChooser.getSelectedFile();
        	
        }
        
        return currentFile;
    }
}
