package gui.ui.list;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ClientGUI extends JPanel{

	Color text  = new Color(76,196,40);
	Color heightTx  = new Color(175,250,110);
	
	JList list = new JList();
	DefaultListModel model = new DefaultListModel();
	JTextField textField = new JTextField();
	JButton create = new JButton("Create");
	JButton exit = new JButton("Exit");
	
	
	public ClientGUI() {
		super();
		setLocation(10, 10);
		setSize(400, 200);
		//setBackground(Color.BLACK);
		//setBorder(BorderFactory.createLineBorder(Color.blue));
		setLayout(new BorderLayout());
		
		for (int i = 0; i < 15; i++) {
			model.addElement("sanyun"+i);
		}
		 
		 
		list.setModel(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCellRenderer(new StarListCellRender());
		list.setBackground(Color.BLACK);
		//list.setUI((StarListUI) StarListUI.createUI(list));
		list.setVisibleRowCount(5);
//		list.setBorder(new RoundBorder(Color.RED));
		list.setSelectedIndex(0);
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.getVerticalScrollBar().setUI((StarScrollBarUI)StarScrollBarUI.createUI(list));
		
		scrollPane.setBorder(new RoundBorder(Color.RED));
//		scrollPane.setBorder(null);
		scrollPane.setUI((StarScrollPaneUI)StarScrollPaneUI.createUI(scrollPane));
		
		textField.setBorder(BorderFactory.createLineBorder(Color.red));
		textField.setForeground(text);
		textField.setCaretColor(heightTx);
		textField.setBackground(Color.BLACK);
		
		JPanel buttonPane = new JPanel(); 
		buttonPane.setLayout(new FlowLayout());
		buttonPane.add(create);
		buttonPane.add(exit);
		
		add(textField, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.SOUTH);
		
		 
// list.setLayoutOrientation(JList.VERTICAL);
//		list.setVisibleRowCount(5);
		
//		new Thread(new Runnable(){
//
//			public void run() {
//				while(!model.isEmpty()){
//					model.remove(0);
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//			
//		}).start();
	}

	public static void main(String[] args) throws Exception, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JFrame frame = new JFrame();
		JPanel contentPanel = new JPanel();
		frame.setContentPane(contentPanel);
		contentPanel.add(new ClientGUI());
		contentPanel.setLayout(null);
//		contentPanel.setBackground(Color.BLACK);
		//contentPanel
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 400);
		frame.setVisible(true);
		
		
	}
}
