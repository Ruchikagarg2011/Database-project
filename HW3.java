package edu.utexas.se.swing.sample;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Component;
import java.awt.Color;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.DefaultComboBoxModel;
import com.toedter.calendar.JDateChooser;
import oracle.jdbc.OracleTypes;
import javax.swing.table.DefaultTableModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class HW3 {
	Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String subQuery1 =" ", subQuery2 = " ",subQuery3 =" ",
    		subQuery7=" ",subQuery8=" ",subQuery9=" ",subQuery10=" ";
    String FinalQuery = " ";
    String UserFinalQuery = " ";
  //  String checkinFromDay =null;
	private JFrame frmYelpApplication;
	private JTextField txtCategory;
	private JTextField txtSubCategory;
	private JList Category;
	private JList SubCategory;
	ArrayList<String> catList= new ArrayList();
	ArrayList<String> subCatList= new ArrayList();
	ArrayList<String> selectedCatList = new ArrayList();
	ArrayList<String> selectedSubCatList = new ArrayList();
	ArrayList<String> finalSubCat = new ArrayList();
	DefaultListModel categoryList = new DefaultListModel<String>();
	DefaultListModel subCategoryList = new DefaultListModel<String>();
	String connectionString;
	DefaultTableModel resultTable;
	DefaultTableModel reviewTable;
	DefaultTableModel userResultTable;
	DefaultTableModel userReviewTable;
	HashMap<Integer,String> bIDMap;
	HashMap<Integer,String> UserIdMap;

	
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JPanel panel;
	private JScrollPane scrollPane_3;
	private JScrollBar scrollBar;
	private JTextField txtCheckin;
	private JTextField txtReview;
	private JTextField txtResults;
	private JTextField txtUsers;
	private JTable BusinessTable;
	private JTable BusinessReviewTable;
	private JTable UsersTable;
	private JTable UsersReview;
	private JComboBox DaysFromComboBox;
	private JComboBox HoursFromComboBox;
	private JComboBox ToDaysComboBox;
	private JComboBox ToHoursComboBox;
	private JComboBox CheckIncomboBox;
	private	JComboBox StarsComboBox;
	private JComboBox VotescomboBox;
	private JDateChooser Review_From_Date;
	private JDateChooser Review_To_Date;
	private JDateChooser MemberSince_Date;
	private JComboBox ReviewCountcomboBox;
	private JComboBox NumOfFriendscomboBox;
	private JComboBox AvgStarscomboBox;
	private JComboBox SelectAttributecomboBox;
	private JTextField VotestextPane;
	private JTextField StarValueTextPane;
	private JTextField CheckinValue;
	private JTextField ReviewCounttextPane;
	private JTextField AvgStarstextPane;
	private JTextField NumOfFriendstextPane;
	private JTextArea ShowQuerytextField;
	private JScrollPane scrollPane_5;
	private JScrollPane scrollPane_6;
	private JPanel BusinessTablePanel;
	private JPanel UserTablePanel;
	private JPanel UserDetailsPanel;
	private JPanel CategoryPanel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HW3 window = new HW3();
					window.frmYelpApplication.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public HW3() throws ClassNotFoundException, SQLException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	private void initialize() throws ClassNotFoundException, SQLException {
		bIDMap = new HashMap<Integer,String>();
		UserIdMap = new HashMap<Integer,String>();
		frmYelpApplication = new JFrame();
		frmYelpApplication.setTitle("YELP APPLICATION");
	//	frmYelpApplication.setBounds(100, 100, 997, 723);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frmYelpApplication.setSize(screenSize.width,screenSize.height);
		frmYelpApplication.setVisible(true);
		frmYelpApplication.setResizable(true);	

		frmYelpApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmYelpApplication.getContentPane().setLayout(null);
		
		txtSubCategory = new JTextField();
		txtSubCategory.setFont(new Font("Tahoma", Font.BOLD, 20));
		txtSubCategory.setBackground(Color.LIGHT_GRAY);
		txtSubCategory.setText("         Sub Category");
		txtSubCategory.setBounds(236, 29, 232, 36);
		frmYelpApplication.getContentPane().add(txtSubCategory);
		txtSubCategory.setColumns(10);
		
		txtCategory = new JTextField();
		txtCategory.setFont(new Font("Tahoma", Font.BOLD, 20));
		txtCategory.setBackground(Color.LIGHT_GRAY);
		txtCategory.setText("          Category");
		txtCategory.setBounds(0, 29, 232, 36);
		frmYelpApplication.getContentPane().add(txtCategory);
		txtCategory.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("         \t\t\t       \t\t              BUSINESS");
		lblNewLabel.setLabelFor(frmYelpApplication);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBackground(Color.BLUE);
		lblNewLabel.setBounds(0, 0, 468, 27);
		frmYelpApplication.getContentPane().add(lblNewLabel);
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
        conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/OracleDb","ruchika", "ruchika");
        String sql1="Select DISTINCT(CategoryName) from categories";
        stmt = conn.prepareStatement(sql1);
        rs=stmt.executeQuery();
       
         while (rs.next()) {
            String catg = rs.getString(1);
           // categoryList.addElement(catg);
            catList.add(catg);

        }
         stmt.close();
         rs.close();
		
		CategoryPanel = new JPanel();
		CategoryPanel.setBounds(0, 67, 232, 482);
		frmYelpApplication.getContentPane().add(CategoryPanel);
		CategoryPanel.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 232, 482);
		CategoryPanel.add(scrollPane);

		Category = new JList(categoryList);
		
		Category.setCellRenderer(new CheckListRenderer());
		scrollPane.setViewportView(Category);
		Category.setFont(new Font("Tahoma", Font.PLAIN, 15));
		Category.setBorder(new LineBorder(new Color(0, 0, 0), 4));		
		Category.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		for(int i =0; i < catList.size();i++){
			categoryList.addElement(new CheckListItem(catList.get(i)));
		}
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(236, 67, 232, 482);
		frmYelpApplication.getContentPane().add(scrollPane_1);
		
		SubCategory = new JList(subCategoryList);
		SubCategory.setCellRenderer(new CheckListRenderer());
		scrollPane_1.setViewportView(SubCategory);
		SubCategory.setFont(new Font("Tahoma", Font.PLAIN, 15));
		SubCategory.setBorder(new LineBorder(new Color(0, 0, 0), 4));
		SubCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		txtCheckin = new JTextField();
		txtCheckin.setText("         Checkin");
		txtCheckin.setFont(new Font("Tahoma", Font.BOLD, 20));
		txtCheckin.setColumns(10);
		txtCheckin.setBackground(Color.LIGHT_GRAY);
		txtCheckin.setBounds(473, 29, 232, 36);
		frmYelpApplication.getContentPane().add(txtCheckin);		
		
		DaysFromComboBox = new JComboBox();
		DaysFromComboBox.setModel(new DefaultComboBoxModel(new String[] {"Choose Day", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"}));
		DaysFromComboBox.setBounds(480, 104, 101, 22);
		frmYelpApplication.getContentPane().add(DaysFromComboBox);
		
		HoursFromComboBox = new JComboBox();
		HoursFromComboBox.setModel(new DefaultComboBoxModel(new String[] {"Choose Hour", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
		HoursFromComboBox.setBounds(593, 104, 101, 22);
		frmYelpApplication.getContentPane().add(HoursFromComboBox);
		
		ToDaysComboBox = new JComboBox();
		ToDaysComboBox.setModel(new DefaultComboBoxModel(new String[] {"Choose Day", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"}));
		ToDaysComboBox.setBounds(480, 205, 101, 22);
		frmYelpApplication.getContentPane().add(ToDaysComboBox);
		
		ToHoursComboBox = new JComboBox();
		ToHoursComboBox.setModel(new DefaultComboBoxModel(new String[] {"Choose Hour", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
		ToHoursComboBox.setBounds(593, 205, 101, 22);
		frmYelpApplication.getContentPane().add(ToHoursComboBox);
		
		CheckIncomboBox = new JComboBox();		
		CheckIncomboBox.setModel(new DefaultComboBoxModel(new String[] {"Choose Operator", "=", ">", "<"}));
		CheckIncomboBox.setBounds(550, 328, 132, 27);
		frmYelpApplication.getContentPane().add(CheckIncomboBox);
		
		CheckinValue = new JTextField();
		CheckinValue.setColumns(10);
		CheckinValue.setBounds(587, 415, 95, 27);
		frmYelpApplication.getContentPane().add(CheckinValue);
		
		JLabel lblFrom = new JLabel("From Day");
		lblFrom.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblFrom.setBounds(483, 75, 91, 22);
		frmYelpApplication.getContentPane().add(lblFrom);
		
		JLabel lblTo = new JLabel("To Day");
		lblTo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTo.setBounds(490, 182, 74, 16);
		frmYelpApplication.getContentPane().add(lblTo);
		
		JLabel lblNewLabel_1 = new JLabel("Num. of Checkins:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1.setBounds(490, 299, 177, 16);
		frmYelpApplication.getContentPane().add(lblNewLabel_1);
		
		JLabel lblValue = new JLabel("Value");
		lblValue.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblValue.setBounds(523, 422, 56, 16);
		frmYelpApplication.getContentPane().add(lblValue);
		
		txtReview = new JTextField();
		txtReview.setText("         Review");
		txtReview.setFont(new Font("Tahoma", Font.BOLD, 20));
		txtReview.setColumns(10);
		txtReview.setBackground(Color.LIGHT_GRAY);
		txtReview.setBounds(706, 29, 232, 36);
		frmYelpApplication.getContentPane().add(txtReview);
		
		StarsComboBox = new JComboBox();
		StarsComboBox.setModel(new DefaultComboBoxModel(new String[] {"Choose Operator", "=", "<", ">"}));
		StarsComboBox.setBounds(798, 205, 123, 22);
		frmYelpApplication.getContentPane().add(StarsComboBox);
		
		JLabel label = new JLabel("Value");
		label.setFont(new Font("Tahoma", Font.BOLD, 16));
		label.setBounds(759, 260, 56, 16);
		frmYelpApplication.getContentPane().add(label);
		
		VotescomboBox = new JComboBox();
		VotescomboBox.setModel(new DefaultComboBoxModel(new String[] {"Choose Operator", "=", "<", ">"}));
		VotescomboBox.setBounds(798, 329, 123, 22);
		frmYelpApplication.getContentPane().add(VotescomboBox);
		
		JLabel label_1 = new JLabel("Value");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		label_1.setBounds(759, 417, 56, 16);
		frmYelpApplication.getContentPane().add(label_1);
		
		JLabel lblVotes = new JLabel("Votes:");
		lblVotes.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblVotes.setBounds(728, 332, 56, 16);
		frmYelpApplication.getContentPane().add(lblVotes);
		
		JLabel label_2 = new JLabel("From");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		label_2.setBounds(728, 89, 56, 22);
		frmYelpApplication.getContentPane().add(label_2);
		
		JLabel label_3 = new JLabel("To");
		label_3.setFont(new Font("Tahoma", Font.BOLD, 16));
		label_3.setBounds(741, 150, 43, 16);
		frmYelpApplication.getContentPane().add(label_3);
		
		JLabel lblStar = new JLabel("Star:");
		lblStar.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblStar.setBounds(741, 205, 56, 22);
		frmYelpApplication.getContentPane().add(lblStar);
		
		txtResults = new JTextField();
		txtResults.setText("                                                                    Results");
		txtResults.setFont(new Font("Tahoma", Font.BOLD, 20));
		txtResults.setColumns(10);
		txtResults.setBackground(Color.LIGHT_GRAY);
		txtResults.setBounds(941, 29, 949, 36);
		frmYelpApplication.getContentPane().add(txtResults);
		
		txtUsers = new JTextField();
		txtUsers.setText("                                     Users");
		txtUsers.setFont(new Font("Tahoma", Font.BOLD, 20));
		txtUsers.setColumns(10);
		txtUsers.setBackground(Color.LIGHT_GRAY);
		txtUsers.setBounds(0, 554, 468, 36);
		frmYelpApplication.getContentPane().add(txtUsers);
		
		JLabel lblMemberSince = new JLabel("Member Since");
		lblMemberSince.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblMemberSince.setBounds(18, 603, 97, 22);
		frmYelpApplication.getContentPane().add(lblMemberSince);
		
		JLabel lblReviewCount = new JLabel("Review Count:");
		lblReviewCount.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblReviewCount.setBounds(18, 653, 97, 16);
		frmYelpApplication.getContentPane().add(lblReviewCount);
		
		UserDetailsPanel = new JPanel();
		UserDetailsPanel.setBounds(127, 603, 319, 239);
		frmYelpApplication.getContentPane().add(UserDetailsPanel);
		UserDetailsPanel.setLayout(null);
		
		ReviewCountcomboBox = new JComboBox();
		ReviewCountcomboBox.setBounds(0, 48, 119, 22);
		UserDetailsPanel.add(ReviewCountcomboBox);
		ReviewCountcomboBox.setModel(new DefaultComboBoxModel(new String[] {"Choose Operator", "=", ">", "<"}));
		
		NumOfFriendscomboBox = new JComboBox();
		NumOfFriendscomboBox.setBounds(0, 95, 119, 22);
		UserDetailsPanel.add(NumOfFriendscomboBox);
		NumOfFriendscomboBox.setModel(new DefaultComboBoxModel(new String[] {"Choose Operator", "=", ">", "<"}));
				
		AvgStarscomboBox = new JComboBox();
		AvgStarscomboBox.setBounds(0, 145, 119, 22);
		UserDetailsPanel.add(AvgStarscomboBox);
		AvgStarscomboBox.setModel(new DefaultComboBoxModel(new String[] {"Choose Operator", "=", ">", "<"}));
						
		SelectAttributecomboBox = new JComboBox();
		SelectAttributecomboBox.setBounds(63, 217, 220, 22);
		UserDetailsPanel.add(SelectAttributecomboBox);
		SelectAttributecomboBox.setModel(new DefaultComboBoxModel(new String[] {"AND", "OR"}));
								
		MemberSince_Date = new JDateChooser();
		MemberSince_Date.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				String yelping_check=  ((JTextField)MemberSince_Date.getDateEditor().getUiComponent()).getText();
				if(yelping_check.length()>0){
					Category.setEnabled(false);
				}
				else Category.setEnabled(true);
				
			}
		});
		
		MemberSince_Date.setBounds(0, 0, 119, 27);
		UserDetailsPanel.add(MemberSince_Date);
		MemberSince_Date.setDateFormatString("yyyy-MM");
		
								
		ReviewCounttextPane = new JTextField();
		ReviewCounttextPane.setBounds(218, 42, 101, 27);
		UserDetailsPanel.add(ReviewCounttextPane);
		ReviewCounttextPane.setColumns(10);
								
		AvgStarstextPane = new JTextField();
		AvgStarstextPane.setBounds(218, 142, 101, 27);
		UserDetailsPanel.add(AvgStarstextPane);
		AvgStarstextPane.setColumns(10);
								
		NumOfFriendstextPane = new JTextField();
		NumOfFriendstextPane.setBounds(218, 92, 101, 27);
		UserDetailsPanel.add(NumOfFriendstextPane);
		NumOfFriendstextPane.setColumns(10);
		
		JLabel lblNumOfFriends = new JLabel("Num. of Friends");
		lblNumOfFriends.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNumOfFriends.setBounds(18, 697, 97, 22);
		frmYelpApplication.getContentPane().add(lblNumOfFriends);
		
		JLabel lblAvgStars = new JLabel("Avg Stars");
		lblAvgStars.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAvgStars.setBounds(18, 750, 97, 16);
		frmYelpApplication.getContentPane().add(lblAvgStars);
		
		JLabel label_4 = new JLabel("Value");
		label_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_4.setBounds(277, 652, 56, 16);
		frmYelpApplication.getContentPane().add(label_4);
		
		JLabel label_5 = new JLabel("Value");
		label_5.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_5.setBounds(277, 702, 56, 16);
		frmYelpApplication.getContentPane().add(label_5);
		
		JLabel label_6 = new JLabel("Value");
		label_6.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_6.setBounds(277, 749, 56, 16);
		frmYelpApplication.getContentPane().add(label_6);
		
		JLabel lblSelect = new JLabel("Select");
		lblSelect.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSelect.setBounds(68, 823, 97, 16);
		frmYelpApplication.getContentPane().add(lblSelect);
		
		JButton btnNewButton = new JButton("Execute Query");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					actionOnExecuteQuery(arg0);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton.setBounds(740, 858, 177, 46);
		frmYelpApplication.getContentPane().add(btnNewButton);
		
		JLabel lblHours = new JLabel("To Hour");
		lblHours.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblHours.setBounds(596, 183, 71, 16);
		frmYelpApplication.getContentPane().add(lblHours);
		
		JLabel lblFromHour = new JLabel("From Hour");
		lblFromHour.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblFromHour.setBounds(596, 78, 98, 16);
		frmYelpApplication.getContentPane().add(lblFromHour);
		
		Review_From_Date = new JDateChooser();
		Review_From_Date.setDateFormatString("yyyy-MM-dd");
		Review_From_Date.setBounds(798, 89, 119, 27);
		frmYelpApplication.getContentPane().add(Review_From_Date);
		
		Review_To_Date = new JDateChooser();
		Review_To_Date.setDateFormatString("yyyy-MM-dd");
		Review_To_Date.setBounds(798, 144, 119, 27);
		frmYelpApplication.getContentPane().add(Review_To_Date);
		
		resultTable = new DefaultTableModel();
		resultTable.addColumn("Row Number");
		resultTable.addColumn("Business Name");
		resultTable.addColumn("City");
		resultTable.addColumn("State");
		resultTable.addColumn("Stars");
		
		BusinessTablePanel = new JPanel();
		BusinessTablePanel.setVisible(false);
		BusinessTablePanel.setBounds(951, 67, 939, 482);
		frmYelpApplication.getContentPane().add(BusinessTablePanel);
		BusinessTablePanel.setLayout(null);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(0, 0, 939, 482);
		BusinessTablePanel.add(scrollPane_4);
		BusinessTable = new JTable();
		scrollPane_4.setViewportView(BusinessTable);
		BusinessTable.setModel(resultTable);
		BusinessTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		userResultTable = new DefaultTableModel();
		userResultTable.addColumn("Row Number");
		userResultTable.addColumn("UserName");
		userResultTable.addColumn("Yelping_Since");
		userResultTable.addColumn("AverageStars");
		
		UserTablePanel = new JPanel();
		UserTablePanel.setVisible(false);
		UserTablePanel.setBounds(951, 67, 939, 482);
		frmYelpApplication.getContentPane().add(UserTablePanel);
		UserTablePanel.setLayout(null);
		
		scrollPane_6 = new JScrollPane();
		scrollPane_6.setBounds(0, 0, 939, 482);
		UserTablePanel.add(scrollPane_6);
		
		UsersTable = new JTable();
		scrollPane_6.setViewportView(UsersTable);
		UsersTable.setModel(userResultTable);
		UsersTable.setAutoCreateRowSorter(true);
		UsersTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		scrollPane_5 = new JScrollPane();
		scrollPane_5.setBounds(480, 564, 981, 278);
		frmYelpApplication.getContentPane().add(scrollPane_5);
		
		ShowQuerytextField = new JTextArea();
		ShowQuerytextField.setFont(new Font("Monospaced", Font.BOLD, 16));
		scrollPane_5.setViewportView(ShowQuerytextField);
		
		VotestextPane = new JTextField();
		VotestextPane.setBounds(822, 411, 95, 27);
		frmYelpApplication.getContentPane().add(VotestextPane);
		VotestextPane.setColumns(10);
		
		StarValueTextPane = new JTextField();
		StarValueTextPane.setColumns(10);
		StarValueTextPane.setBounds(822, 253, 95, 27);
		frmYelpApplication.getContentPane().add(StarValueTextPane);
				
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(473, 29, 232, 509);
		frmYelpApplication.getContentPane().add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBounds(706, 30, 234, 509);
		frmYelpApplication.getContentPane().add(panel_2);
		
		
		populateMainCategoryList();
		populateSubCategoryList();
        openReviewFrame();
     	openUserReviewFrame();
		}
	
	
	
	public void populateMainCategoryList() {
	Category.addMouseListener(new MouseAdapter() {		
	      public void mouseClicked(MouseEvent e) {
	    	  selectedSubCatList.clear();
	      int index = Category.locationToIndex(e.getPoint());
	      CheckListItem  item = (CheckListItem) Category.getModel().getElementAt(index);
	      if(item.isSelected())  { 
	    	  item.isSelected= false;
	    	  item.setSelected(item.isSelected);
	    	  selectedCatList.remove(item.toString()); }
	      else { item.setSelected(!item.isSelected());
	      		item.isSelected= true;}
	      Category.repaint(Category.getCellBounds(index, index));
	      if(selectedCatList.size() ==0) 
	    	  subCategoryList.clear();

	      if (item.isSelected()) {
	       selectedCatList.add(item.toString());
	        }
	      
	      if(selectedCatList.size()!=0) { 
	        subCategoryList.clear();
	        String categoryList= null;
	        if(selectedCatList.size() == 1)
	        	categoryList ="('"+selectedCatList.get(0)+"')";
		      else if(selectedCatList.size() > 1) {
		    	  categoryList ="('"+selectedCatList.get(0)+"'";
		    	  for(int k= 1; k < selectedCatList.size();k++)
		    		  categoryList += ",'"+selectedCatList.get(k)+"'";
		    	  categoryList +=")";
		      }
	        String sql2="Select Distinct (SubCategoryName) from BusinessSubCategory b1 ,BusinessCategory b2 where b1.BusinessId = b2.BusinessId and b2.CategoryName IN "+categoryList+ "ORDER BY SubCategoryName";
			try {
		   stmt = conn.prepareStatement(sql2);
		   rs=stmt.executeQuery();
		   while(rs.next()){			
				String subcat = rs.getString(1);
				//subCategoryList.addElement(subcat);
				subCatList.add(subcat);
				subCategoryList.addElement(new CheckListItem(subcat));
			}		   
		   
//		   for(int i =0; i < selectedCatList.size();i++){
//			   stmt.setString(1, selectedCatList.get(i));
//			rs=stmt.executeQuery();
//			while(rs.next()){			
//				String subcat = rs.getString(1);
//				//subCategoryList.addElement(subcat);
//				subCatList.add(subcat);
//				subCategoryList.addElement(new CheckListItem(subcat));
//			}
//			
//		   }		   
		   stmt.close();
		   rs.close();
		   SubCategory.setModel(subCategoryList);
			} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			}
	      }
	      if(selectedCatList.size() == 1)
	    	  subQuery1 ="'"+selectedCatList.get(0)+"')";
	      else if(selectedCatList.size() > 1) {
	    	  subQuery1 ="'"+selectedCatList.get(0)+"')";
	    	  for(int k= 1; k < selectedCatList.size();k++)
	    		  subQuery1 += "\n"+ "AND BusinessId IN (SELECT BusinessId FROM BUSINESSCATEGORY WHERE CategoryName = "+"'"+selectedCatList.get(k)+"')";
	      }
//	      if(selectedCatList.size() == 1)
//	    	  subQuery1 ="('"+selectedCatList.get(0)+"'))";
//	      else if(selectedCatList.size() > 1) {
//	    	  subQuery1 ="('"+selectedCatList.get(0)+"'";
//	    	  for(int k= 1; k < selectedCatList.size();k++)
//	    		  subQuery1 += ",'"+selectedCatList.get(k)+"'";
//	    	  subQuery1 +="))";
//	      }
	      
	      if(selectedCatList.size() > 0 ){
	    	  for (Component cp : UserDetailsPanel.getComponents() ){
	        cp.setEnabled(false);
	  	}
	      }
	      else{
	    	  for (Component cp : UserDetailsPanel.getComponents() )
	  	        cp.setEnabled(true);
	      }
	      }
	    });
	
	}
	
		
	public void populateSubCategoryList() {
	SubCategory.addMouseListener(new MouseAdapter() {
	 public void mouseClicked(MouseEvent e) {
	       int index = SubCategory.locationToIndex(e.getPoint());
	      CheckListItem  item = (CheckListItem) SubCategory.getModel().getElementAt(index);
	      if(item.isSelected()) { 
	    	  item.isSelected = false;
	    	  item.setSelected(item.isSelected);
	    	  selectedSubCatList.remove(item.toString()); }
	      else {	item.setSelected(!item.isSelected()); item.isSelected = true; }
	      SubCategory.repaint(SubCategory.getCellBounds(index, index));
	      if(selectedSubCatList.size() ==0) selectedSubCatList.clear();
	        if (item.isSelected()) {
	        	selectedSubCatList.add(item.toString());
	        }	  
	 		} 
	    });
	
	}
	
	public void actionOnExecuteQuery(ActionEvent arg0) throws SQLException{
		if (BusinessTable.getRowCount() > 0) {
			for (int i = BusinessTable.getRowCount() - 1; i > -1; i--) {
				resultTable.removeRow(i);
			}
		}
		if (UsersTable.getRowCount() > 0) {
			for (int i = UsersTable.getRowCount() - 1; i > -1; i--) {
				userResultTable.removeRow(i);
			}
		}
		ShowQuerytextField.setText("");
		finalSubCat.clear();
		subQuery7 =" ";subQuery3=" ";subQuery8=" ";subQuery2 =" ";subQuery9=" "; subQuery10=" ";
		String checkinValueCount =null,checkinFromDay=null,checkInHoursFrom=null,checkinToDay=null,checkInHoursTo=null;
		String yelpingSince =null;
		yelpingSince = ((JTextField)MemberSince_Date.getDateEditor().getUiComponent()).getText();
		

		if(selectedCatList.size()!=0) {
			UserTablePanel.setVisible(false);
			BusinessTablePanel.setVisible(true);
			
			 checkinValueCount =CheckinValue.getText();			
			if(checkinValueCount.length() > 0) {
				String checkInValue = CheckIncomboBox.getSelectedItem().toString();
				if(checkInValue != "Choose Operator")
				subQuery7 =" AND BUSINESSID IN (SELECT BusinessId FROM CheckIN WHERE CheckinCount "+checkInValue+" "+checkinValueCount+")";
			}
			checkinFromDay =  DaysFromComboBox.getSelectedItem().toString();
			int dayfrom = splitConvertHour(checkinFromDay);
			checkInHoursFrom = HoursFromComboBox.getSelectedItem().toString();			
			checkinToDay =  ToDaysComboBox.getSelectedItem().toString();
			int dayto = splitConvertHour(checkinToDay);
			if(dayfrom != -1 && dayto == -1 ) dayto = dayfrom;
			checkInHoursTo = ToHoursComboBox.getSelectedItem().toString();	
			if(checkInHoursFrom != "Choose Hour" && checkInHoursTo == "Choose Hour") {
				int nextOnehour =Integer.parseInt(checkInHoursFrom) +1;
				checkInHoursTo = String.valueOf(nextOnehour);
			}
	
	
			if(dayfrom != -1  && dayto != -1 && checkInHoursFrom == "Choose Hour" && checkInHoursTo == "Choose Hour" )
					subQuery3 =" AND BUSINESSID IN (SELECT BusinessId FROM CheckIN WHERE CheckinDayNum BETWEEN "+dayfrom+" AND " +dayto +")";
			else if(dayfrom == -1  && dayto == -1 && checkInHoursFrom != "Choose Hour" && checkInHoursTo != "Choose Hour" )
					subQuery3 =" AND BUSINESSID IN (SELECT BusinessId FROM CheckIN WHERE CheckinHour BETWEEN "+checkInHoursFrom+" AND "+checkInHoursTo+")"; 
			else if(dayfrom != -1  && dayto != -1 && checkInHoursFrom != "Choose Hour" && checkInHoursTo != "Choose Hour" ){
				if(dayfrom == dayto)
					subQuery3 =" AND BUSINESSID IN (SELECT BusinessId FROM CheckIN WHERE CheckinDayNum = "+dayfrom + " AND CheckinHour BETWEEN "+checkInHoursFrom+" AND "+checkInHoursTo+")";
				else {
					int i = dayfrom+1,j = dayto-1;
					subQuery3="AND BusinessId IN (SELECT BusinessId FROM CheckIN WHERE CheckinDayNum ="+dayfrom+" AND CheckinHour >=" +checkInHoursFrom+ "\n"+
                    "UNION \n"+ 
                    "SELECT BusinessId FROM CheckIN WHERE CheckinDayNum BETWEEN "+ i +" AND "+ j +"\n"+
                    "UNION \n"+
                    "SELECT BusinessId FROM CheckIN WHERE CheckinDayNum ="+ dayto+" AND  CheckinHour < ="+ checkInHoursTo +")";                   
				}
			}
						
			String starValue = null,starsChoose=null,votesChoose=null,voteValue=null,reviewFromDate=null,reviewToDate = null;
			starsChoose =  StarsComboBox.getSelectedItem().toString();
			starValue =StarValueTextPane.getText();
			votesChoose = VotescomboBox.getSelectedItem().toString();
			voteValue =VotestextPane.getText();
			reviewFromDate = ((JTextField)Review_From_Date.getDateEditor().getUiComponent()).getText();
			reviewToDate= ((JTextField)Review_To_Date.getDateEditor().getUiComponent()).getText();
			java.util.Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate =formatter.format(date);
			
			
			if(reviewFromDate.length() > 0 && starValue.length() ==0 && voteValue.length() ==0){
				if (reviewToDate.length() ==0)
				subQuery8 ="AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "+"'"+reviewFromDate+"'" +" AND "+"'"+currentDate +"'"+")";
				else subQuery8 ="AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "+ "'"+reviewFromDate+"'"+" AND "+"'"+reviewToDate+"'"+")";
				}
			else if (reviewFromDate.length() == 0 && starValue.length() >0 && voteValue.length() ==0)
					subQuery8 ="AND BusinessID IN (SELECT BusinessId FROM Review WHERE Stars " +starsChoose+" "+starValue+")";
			else if (reviewFromDate.length() == 0 && starValue.length() ==0 && voteValue.length() > 0)	
					subQuery8 = "AND BusinessID IN (SELECT BusinessId FROM Review WHERE TotalVotes "+votesChoose+" "+voteValue+ ")";
			else if (reviewFromDate.length() > 0 && starValue.length() > 0 && voteValue.length() == 0){
				if (reviewToDate.length() ==0)
					subQuery8 ="AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "+"'"+reviewFromDate+"'" +" AND "+"'"+currentDate+"'" +" AND Stars " +starsChoose+" "+starValue+")";
					else subQuery8 ="AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "+"'"+reviewFromDate+"'"+" AND "+"'"+reviewToDate+"'"+" AND Stars " +starsChoose+" "+starValue+")";					
			}
			else if (reviewFromDate.length() > 0 && starValue.length() == 0 && voteValue.length() > 0){
				if (reviewToDate.length() ==0)
					subQuery8 ="AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "+"'"+reviewFromDate+"'" +" AND "+"'"+currentDate+"'" +" AND TotalVotes "+votesChoose+" "+voteValue+ ")";
					else subQuery8 ="AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "+"'"+reviewFromDate+"'"+" AND "+"'"+reviewToDate+"'"+" AND TotalVotes "+votesChoose+" "+voteValue+ ")";					
			}
			else if (reviewFromDate.length() == 0 && starValue.length() > 0 && voteValue.length() > 0)
				subQuery8 ="AND BusinessID IN (SELECT BusinessId FROM Review WHERE Stars " +starsChoose+" "+starValue+" AND TotalVotes "+votesChoose+" "+voteValue+ ")";
			else if (reviewFromDate.length() > 0 && starValue.length() > 0 && voteValue.length() > 0){
				if (reviewToDate.length() ==0)
					subQuery8 ="AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "+"'"+reviewFromDate+"'" +" AND "+"'"+currentDate+"'" +" AND Stars " +starsChoose+" "+starValue+" AND TotalVotes "+votesChoose+" "+voteValue+ ")";
					else subQuery8 ="AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "+"'"+reviewFromDate+"'"+" AND "+"'"+reviewToDate+"'"+" AND Stars " +starsChoose+" "+starValue+" AND TotalVotes "+votesChoose+" "+voteValue+ ")";					
			}
			
			
		if(selectedSubCatList.size()!=0){
			for(int i =0 ; i < selectedSubCatList.size();i++)
				{
					if( subCatList.contains(selectedSubCatList.get(i)))
						finalSubCat.add(selectedSubCatList.get(i));
				}
		}
		if(finalSubCat.size() !=0){
        subQuery2 = "AND BusinessId IN " + "(SELECT BusinessId FROM BusinessSubCategory WHERE SubCategoryName = ";
        if(finalSubCat.size() == 1)
	    	  subQuery2 +="'"+finalSubCat.get(0)+"')";
	      else if(finalSubCat.size() > 1) {
	    	  subQuery2 +="'"+finalSubCat.get(0)+"')";
	    	  for(int k= 1; k < finalSubCat.size();k++)
	    		  subQuery2 += "\n"+ "AND BusinessId IN (SELECT BusinessId FROM BusinessSubCategory WHERE SubCategoryName = "+"'"+finalSubCat.get(k)+"')";
	      }
		}
		
//		if(finalSubCat.size() !=0){
//	        subQuery2 = "AND BusinessId IN " + "(SELECT BusinessId FROM BusinessSubCategory WHERE SubCategoryName IN \r\n";
//	        if(finalSubCat.size() == 1)
//		    	  subQuery2 +="('"+finalSubCat.get(0)+"'))";
//		      else if(finalSubCat.size() > 1) {
//		    	  subQuery2 +="('"+finalSubCat.get(0)+"'";
//		    	  for(int k= 1; k < finalSubCat.size();k++)
//		    		  subQuery2 += ",'"+finalSubCat.get(k)+"'";
//		    	  subQuery2 +="))";
//		      }
//			}
		
		FinalQuery ="SELECT DISTINCT rownum as RowNumber, b.BName as BName, b.City as City, b.State as State, b.Stars as Stars, b.BusinessId as BusinessId \r\n"+
					"FROM BUSINESS b \r\n"+ "WHERE BusinessId IN (SELECT BusinessId FROM BUSINESSCATEGORY WHERE CategoryName = ";
		FinalQuery += subQuery1 +"\r\n"+ subQuery2 +"\r\n"+subQuery3+"\r\n"+"\r\n"+subQuery7+"\r\n"+subQuery8;
		
		System.out.println(FinalQuery);
		ShowQuerytextField.setText(FinalQuery);
		String[] rowObj = new String[5];
		int i =0;
		try {
			rs = null;
			stmt=conn.prepareStatement(FinalQuery);
			rs = stmt.executeQuery(FinalQuery);
			while(rs.next())
			{
				rowObj = new String[] {rs.getString("RowNumber"), rs.getString("BName"), rs.getString("City"), rs.getString("State"), rs.getString("Stars")};
	        	bIDMap.put(i++, rs.getString("BusinessId"));
				resultTable.addRow(rowObj);
			}
			stmt.close();
			rs.close();
		} catch(Exception ex) {
			System.out.println(ex);
		}	
		CheckinValue.setText(null);
		CheckIncomboBox.setSelectedIndex(0);
		DaysFromComboBox.setSelectedIndex(0);
		HoursFromComboBox.setSelectedIndex(0);
		ToDaysComboBox.setSelectedIndex(0);
		ToHoursComboBox.setSelectedIndex(0);
		StarsComboBox.setSelectedIndex(0);
		StarValueTextPane.setText(null);
		VotescomboBox.setSelectedIndex(0);
		VotestextPane.setText(null);
		Review_From_Date.setCalendar(null);
		Review_To_Date.setCalendar(null);
		
	} else if(yelpingSince.length()>0 ){
		BusinessTablePanel.setVisible(false);
		UserTablePanel.setVisible(true);
		String yelping=null,reviewChoose=null,numOfFriendsChoose=null,avgStarsChoose=null,reviewCount=null,numOfFriends=null,avgStars=null;
		yelping = yelpingSince.substring(0, 7);
		reviewChoose = ReviewCountcomboBox.getSelectedItem().toString(); 
		numOfFriendsChoose = NumOfFriendscomboBox.getSelectedItem().toString();
		avgStarsChoose = AvgStarscomboBox.getSelectedItem().toString();
		reviewCount =ReviewCounttextPane.getText();
		numOfFriends =NumOfFriendstextPane.getText();
		avgStars =AvgStarstextPane.getText(); 
		String selectChoose = SelectAttributecomboBox.getSelectedItem().toString();
		UserFinalQuery =" SELECT DISTINCT rownum as RowNumber, UserId, UserName, Yelping_Since, AverageStars FROM YelpUser WHERE "+"Yelping_Since >= "+"'"+yelping+"'";
		if(reviewCount.length() >0 && reviewChoose !="Choose Operator")
			subQuery8 = selectChoose + " UserId IN (SELECT UserId FROM YelpUser WHERE ReviewCount "+reviewChoose+" "+reviewCount+")";
		if(avgStars.length() >0 && avgStarsChoose !="Choose Operator")
			subQuery9 = selectChoose +" UserId IN (SELECT UserId FROM YelpUser WHERE AverageStars "+avgStarsChoose+" "+avgStars+")";
		if(numOfFriends.length()>0 && numOfFriendsChoose !="Choose Operator")
			subQuery10 =selectChoose+ "  UserId IN (SELECT UserId FROM Friends GROUP BY UserId HAVING COUNT(FriendId) "+numOfFriendsChoose+" "+numOfFriends+")";
		UserFinalQuery += "\r\n"+subQuery8+"\r\n"+subQuery9+"\r\n"+subQuery10;
		
		System.out.println(UserFinalQuery);
		ShowQuerytextField.setText(UserFinalQuery);
		String[] rowObj = new String[4];
		int i =0;
		try {
			rs = null;
			stmt=conn.prepareStatement(UserFinalQuery);
			rs = stmt.executeQuery(UserFinalQuery);
			while(rs.next()){
	        	rowObj = new String[] {rs.getString("RowNumber"), rs.getString("UserName"), rs.getString("Yelping_Since"), rs.getString("AverageStars")};	        
	        	userResultTable.addRow(rowObj);
	        	UserIdMap.put(i++, rs.getString("UserId"));
	        }
	        conn.commit();
	        rs.close();
			} catch(Exception ex) {
			System.out.println(ex);
		}
		MemberSince_Date.setCalendar(null);
		ReviewCountcomboBox.setSelectedItem("Choose Operator");
		NumOfFriendscomboBox.setSelectedItem("Choose Operator");
		AvgStarscomboBox.setSelectedItem("Choose Operator");
		ReviewCounttextPane.setText(null);
		NumOfFriendstextPane.setText(null);
		AvgStarstextPane.setText(null);
	}
		
}
	
	private void openReviewFrame() { 
		BusinessTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JFrame frame = new JFrame("Business Review");
					JPanel panel = new JPanel();
					panel.setLayout(new FlowLayout());
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					
					BusinessReviewTable = new JTable();
					BusinessReviewTable.setBounds(952, 75, 938, 691);

					reviewTable = new DefaultTableModel();
					BusinessReviewTable.setModel(reviewTable);
					reviewTable.addColumn("BusinessName");
					reviewTable.addColumn("UserName");
					reviewTable.addColumn("ReviewId");
					reviewTable.addColumn("ReviewText");					
					
					JScrollPane reviewResultPane = new JScrollPane(BusinessReviewTable);
					panel.add(reviewResultPane);

					String[] rowObj = new String[3];
					try {
					CallableStatement call = conn.prepareCall("BEGIN YElP_Business_Review(?,?); END;");	
					call.setString(1, bIDMap.get(row));
					call.registerOutParameter(2,OracleTypes.CURSOR);					
			        call.execute();
			        rs = (ResultSet) call.getObject(2);
						while(rs.next())
						{
							rowObj = new String[] {rs.getString("BName"),rs.getString("UserName"),rs.getString("ReviewId"),rs.getString("Review")};
							//String data[][] = (String[][]) appendValue(myTwoDimensionalStringArray, rowObj);
							reviewTable.addRow(rowObj);
						}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}					
					frame.getContentPane().add(panel);
					frame.setSize(1000, 1000);
					frame.setLocationRelativeTo(null);
				//	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				}
			}
		});

	}
	
	private void openUserReviewFrame() { 
		UsersTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JFrame frame = new JFrame("User Review");
					JPanel panel = new JPanel();
					panel.setLayout(new FlowLayout());
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					
					userReviewTable = new DefaultTableModel();
					userReviewTable.addColumn("BusinessName");
					userReviewTable.addColumn("UserName");
					userReviewTable.addColumn("ReviewId");
					userReviewTable.addColumn("ReviewText");					
					
					UsersReview = new JTable();
					UsersReview.setModel(userReviewTable);
					UsersReview.setBounds(952, 75, 938, 691);
			//		frmYelpApplication.getContentPane().add(UsersReview);
					
					JScrollPane reviewResultPane = new JScrollPane(UsersReview);
					panel.add(reviewResultPane);
				//	panel.setBounds(951, 67, 939, 482);
				//	frmYelpApplication.getContentPane().add(panel);
					String[] rowObj = new String[4];
					try {
					CallableStatement call = conn.prepareCall("BEGIN YElP_User_Review(?,?); END;");	
					call.setString(1, UserIdMap.get(row));
					call.registerOutParameter(2,OracleTypes.CURSOR);					
			        call.execute();
			        rs = (ResultSet) call.getObject(2);
						while(rs.next())
						{
							rowObj = new String[] {rs.getString("BName"),rs.getString("UserName"),rs.getString("ReviewId"),rs.getString("Review")};
							//String data[][] = (String[][]) appendValue(myTwoDimensionalStringArray, rowObj);
							userReviewTable.addRow(rowObj);
						}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}					
					frame.getContentPane().add(panel);
					frame.setSize(1000, 1000);
					frame.setLocationRelativeTo(null);
				//	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				}
			}
		});

	}
	
	
	static class CheckListItem {
		        private String  label;
		        private boolean isSelected;

		        public CheckListItem(String label) {
		            this.label = label;
		            isSelected = false;
		        }		       
		        public void setSelected(boolean isSelected) {
		            this.isSelected = isSelected;
		        }
		        public boolean isSelected() {
		            return isSelected;
		        } 
		        public String toString()
		        {
		            return label;
		        }
		    }

	static class CheckListRenderer extends JCheckBox implements ListCellRenderer {
		        public Component getListCellRendererComponent(JList list, Object value, int index,boolean isSelected, boolean hasFocus) {
		            setEnabled(list.isEnabled());
		            setSelected(((CheckListItem)value).isSelected());
		            setFont(list.getFont());
		            setBackground(list.getBackground());
		            setForeground(list.getForeground());
		            setText(value.toString());
		            return this;
		        }
		    }
		    
	public static int splitConvertHour(String day){
				 if (day.equals("SUNDAY"))		
						return 0;
				 
				 else if (day.equals("MONDAY"))	
						return 1;
				 
				 else if (day.equals("TUESDAY"))
					 	return 2;
				 
				 else if (day.equals("WEDNESDAY"))
					 	return 3;
				 
				 else if (day.equals("THURSDAY"))
						return 4;
					
				 else if (day.equals("FRIDAY"))
						return 5;
					
				 else if (day.equals("SATURDAY"))
						return 6;
				return -1;
			}
}
