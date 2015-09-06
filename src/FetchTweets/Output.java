package FetchTweets;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * An output GUI to display output
 * @author carsonchen
 *
 */
public class Output extends JFrame {

	private JPanel contentPane;
	public static String path = null;
	public FileReader theFile;
	private static File f = new File("output.txt");
	private static Output frame = null;
	private final JPanel panel = new JPanel();
	private final JScrollPane scrollPane = new JScrollPane();
	private final JButton btnNewButton = new JButton("LazyIBk");
	private final JLabel label = new JLabel("");
	private final JButton btnLazykstar = new JButton("LazyKStar");
	private final JButton btnNeuralnetwork = new JButton("NeuralNetwork");
	private final JButton btnJ = new JButton("J48");
	private final JButton btnNewButton_1 = new JButton("BayesNetWork");
	private final JTextArea textArea = new JTextArea();
	private final JButton btnBackToMenu = new JButton("Back to Menu");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Output();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Output() {
		initGUI();
	}
	private void initGUI() {
		setTitle("Output");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		scrollPane.setBounds(19, 6, 544, 612);
		
		panel.add(scrollPane);
		
		scrollPane.setViewportView(textArea);
		
		
		try {
			if(!f.exists()){
				try {
					f.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			theFile = new FileReader(f);
			BufferedReader br = new BufferedReader(theFile);
			try {
				textArea.read(br, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btnNewButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			    frame.dispose();
				new LazyIBk(path);
				new Output();
				Output.main(null);
			}
		});
		
		btnNewButton.setBounds(603, 134, 164, 34);
		
		panel.add(btnNewButton);
		label.setIcon(new ImageIcon("icon/gospel-of-algorithms_meitu_1.jpg"));
		label.setBounds(624, 6, 100, 99);
		
		panel.add(label);
		btnLazykstar.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			    frame.dispose();
			    new LazyKStar(path);
				new Output();
				Output.main(null);
			}
		});
		btnLazykstar.setBounds(603, 206, 164, 34);
		
		panel.add(btnLazykstar);
		btnNeuralnetwork.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			    frame.dispose();
			    new NeuralNetwork(path);
				new Output();
				Output.main(null);
			}
		});
		btnNeuralnetwork.setBounds(603, 275, 164, 34);
		
		panel.add(btnNeuralnetwork);
		btnJ.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			    frame.dispose();
			    new J48Tree(path);
				new Output();
				Output.main(null);
			}
		});
		btnJ.setBounds(603, 342, 164, 34);
		
		panel.add(btnJ);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			    frame.dispose();
			    new BayesNetWork(path);
				new Output();
				Output.main(null);
			}
		});
		btnNewButton_1.setBounds(603, 413, 164, 34);
		
		panel.add(btnNewButton_1);
		btnBackToMenu.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				f.delete();
			    frame.dispose();
			    new Menu();
			    Menu.main(null);
			}
		});
		btnBackToMenu.setBounds(603, 565, 164, 34);
		
		panel.add(btnBackToMenu);
	}
	
	public void windowClosing(WindowEvent e) {
		
		JOptionPane.showMessageDialog(panel,"Thank you for using! :D");;
		System.exit(1);
		
	}
}
