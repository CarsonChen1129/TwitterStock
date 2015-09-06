package FetchTweets;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import java.awt.Font;

import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * The Menu interface
 * @author carsonchen
 *
 */
public class Menu extends JFrame {

	private JPanel contentPane;
	private static Menu frame = null;
	private final JPanel panel = new JPanel();
	private final JButton btnNewButton = new JButton("Fetch Stock Data");
	private final JLabel lblNewLabel = new JLabel("");
	private final JButton btnFetchTweetsData = new JButton("Fetch Tweets Data");
	private final JButton btnAnalyzeEmotion = new JButton("Analyze & Generate");
	private final JButton btnApplyAlgorithms = new JButton("Apply Algorithms");
	private final JButton btnExit = new JButton("Exit");
	private final JLabel lblEenData = new JLabel("EEN 577 Data Mining Project 2 --Jiajun Chen & Joe Chakko");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Menu();
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
	public Menu() {
		initGUI();
	}
	private void initGUI() {
		setFont(new Font("Geneva", Font.ITALIC, 12));
		setTitle("TwitterStock");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 438, 402);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			    frame.dispose();
				new StockData();
			}
		});
		btnNewButton.setBounds(17, 118, 170, 64);
		
		panel.add(btnNewButton);
		lblNewLabel.setBounds(159, 24, 100, 82);
		lblNewLabel.setIcon(new ImageIcon("icon/Twitter-Cashtags_meitu_2.jpg"));
		
		panel.add(lblNewLabel);
		btnFetchTweetsData.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			    frame.dispose();
				try {
					new FetchTweets();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, "For some reasons, errors appear x_x");
					new Menu();
					Menu.main(null);
				}
			}
		});
		btnFetchTweetsData.setBounds(241, 118, 170, 64);
		
		panel.add(btnFetchTweetsData);
		btnAnalyzeEmotion.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			    frame.dispose();
				try {
					new CreateArff();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, "For some reasons, errors appear x_x");
					new Menu();
					Menu.main(null);
				}
			}
		});
		btnAnalyzeEmotion.setBounds(17, 207, 170, 64);
		
		panel.add(btnAnalyzeEmotion);
		btnApplyAlgorithms.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			    frame.dispose();
				
			    try{
			    ArffSelector arffChoose = new ArffSelector();
				File file = arffChoose.openFile();
				String filePath = file.getAbsolutePath();
				new Output();
				Output.path = filePath;
				Output.main(null);
			    } catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame, "For some reasons, errors appear x_x");
					new Menu();
					Menu.main(null);
				}
			}
		});
		btnApplyAlgorithms.setBounds(241, 207, 170, 64);
		
		panel.add(btnApplyAlgorithms);
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
					JOptionPane.showMessageDialog(panel,"Thank you for using! :D");;
					System.exit(0);
			}
		});;
		btnExit.setBounds(131, 311, 170, 36);
		
		panel.add(btnExit);
		lblEenData.setHorizontalAlignment(SwingConstants.CENTER);
		lblEenData.setFont(new Font("Lucida Grande", Font.ITALIC, 10));
		lblEenData.setBounds(17, 348, 394, 16);
		
		panel.add(lblEenData);
	}
}
