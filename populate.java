
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.*;

import org.json.simple.parser.*;
import org.json.simple.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class populate {
	
	public static void main(String [] args) throws IOException, ParseException, SQLException{

	 //   String fileName = "C:\\Users\\ruchi\\santa clara\\WINTER_2018\\DataBase Systems\\Assignments\\Assignment3\\YelpDataset1\\YelpDataset-CptS451\\yelp_user.json";
	 //   String fileName1 = "C:\\Users\\ruchi\\santa clara\\WINTER_2018\\DataBase Systems\\Assignments\\Assignment3\\YelpDataset1\\YelpDataset-CptS451\\yelp_business.json";
	 //   String fileName2 = "C:\\Users\\ruchi\\santa clara\\WINTER_2018\\DataBase Systems\\Assignments\\Assignment3\\YelpDataset1\\YelpDataset-CptS451\\yelp_checkin.json";
	 //   String fileName3= "C:\\Users\\ruchi\\santa clara\\WINTER_2018\\DataBase Systems\\Assignments\\Assignment3\\YelpDataset1\\YelpDataset-CptS451\\yelp_review.json";		
	  
		truncateTables();
	    readYelpUser(args[3]);
	    readYelpBusiness(args[0]);
	    readCheckin(args[2]);
	    readReview(args[1]);
	
	}
	
	// Connection to DataBase
		public static Connection getDBConnection() 
		{
			Connection dbConnection = null;
			try {

				Class.forName("oracle.jdbc.driver.OracleDriver");
				dbConnection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/OracleDb","ruchika", "ruchika");
				return dbConnection;
			}catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
			} 
			catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			return dbConnection;
		}
		
	
	// parsing Yelp User JSON file and populating YelpUser Data into database
	public static void readYelpUser(String fileName) throws IOException, ParseException, SQLException {
		Connection con = getDBConnection();
		
		String sqlQuery1="INSERT INTO YelpUser"+ "(Yelping_Since, FunnyVotes, UsefulVotes, CoolVotes, ReviewCount, UserName, UserId, Fans, AverageStars, UserType) VALUES" + "(?,?,?,?,?,?,?,?,?,?)";
 	 	PreparedStatement pstmt1 = con.prepareStatement(sqlQuery1);
		String sqlQuery2 = "INSERT INTO Friends" + "(UserId, FriendId) VALUES" + "(?,?)";
		PreparedStatement pstmt2 = con.prepareStatement(sqlQuery2);
		String sqlQuery3 = "INSERT INTO EliteYears" + "(UserId, Elite) VALUES" + "(?,?)";
		PreparedStatement pstmt3 = con.prepareStatement(sqlQuery3);
		String sqlQuery4 = "INSERT INTO Compliments" + "(UserId, ComplimentType, ComplimentValue) VALUES" + "(?,?,?)";
		PreparedStatement pstmt4 = con.prepareStatement(sqlQuery4);
		 	 	
		FileReader file = new FileReader(fileName);
    	BufferedReader bfreader = new BufferedReader (file);
    	
    	String line = null;
    	JSONParser parser = new JSONParser();
    	JSONObject ob1;
    			
    	while((line = bfreader.readLine()) != null) {
    		ob1 = (JSONObject)parser.parse(line);
    		
    		String yelping_since = (String) ob1.get("yelping_since");
    		pstmt1.setString(1, yelping_since);
    		
    		JSONObject votes = (JSONObject) ob1.get("votes");
    		int funny = ((Long) votes.get("funny")).intValue();
    		int useful = ((Long) votes.get("useful")).intValue();
			int  cool = ((Long) votes.get("cool")).intValue();
    		pstmt1.setInt(2, funny);
    		pstmt1.setInt(3, useful);
    		pstmt1.setInt(4, cool);
    		
    		int review_count = ((Long) ob1.get("review_count")).intValue();
			pstmt1.setInt(5, review_count);
		
			String name = (String) ob1.get("name");
			pstmt1.setString(6, name);
		
			String user_id = (String) ob1.get("user_id");
			pstmt1.setString(7, user_id);
		
			int fans = ((Long) ob1.get("fans")).intValue();
			pstmt1.setInt(8, fans);
		
			float avg_stars = ((Double) ob1.get("average_stars")).floatValue();
			pstmt1.setFloat(9, avg_stars);
		
			String type = (String) ob1.get("type");
			pstmt1.setString(10, type);
			
    		pstmt1.executeUpdate();
    		
    		JSONArray friends = (JSONArray)ob1.get("friends");
    		if(friends != null){
    			for(Object friend : friends){
    				String f_id = (String) friend;
    				pstmt2.setString(1,user_id);
    				pstmt2.setString(2,f_id);
    				pstmt2.executeUpdate();
    			}
    		}
    		
    		JSONArray elite = (JSONArray)ob1.get("elite");
    		if(elite != null){
    			Iterator<Long> itr = elite.iterator();
    			while(itr.hasNext()){
    				int year =  itr.next().intValue();
    				pstmt3.setString(1,user_id);
    				pstmt3.setInt(2,year);
    				pstmt3.executeUpdate();
    			}
    		}
    		
    		JSONObject compliment = (JSONObject) ob1.get("compliments");
    		if(compliment != null){
    		for(Object key : compliment.keySet()){
    			String c_name = (String)key;
    			int c_value = ((Long)compliment.get(key) ).intValue();
    			pstmt4.setString(1,user_id);
    			pstmt4.setString(2,c_name);
				pstmt4.setInt(3,c_value);
				pstmt4.executeUpdate();
    			}
    		}   		
    	} 
    	pstmt1.close();
    	pstmt2.close();
    	pstmt3.close();
    	pstmt4.close();
    	bfreader.close();
    	con.close();
    }
	// parsing Yelp Business JSON file and populating YelpBusiness Data into database
	public static void readYelpBusiness(String fileName) throws IOException, ParseException, SQLException{
		Connection con = getDBConnection();
		
		String sqlQuery1="INSERT INTO Business"+ "(BusinessId, FullAddress, IsOpen, City, ReviewCount, BName, Longitude, State, Stars, Latitude, BType) VALUES" + "(?,?,?,?,?,?,?,?,?,?,?)";
 	 	PreparedStatement pstmt1 = con.prepareStatement(sqlQuery1);
		String sqlQuery2 = "INSERT INTO BusinessHours" + "(Days, OpenHour, CloseHour, BusinessId) VALUES" + "(?,?,?,?)";
		PreparedStatement pstmt2 = con.prepareStatement(sqlQuery2);
		String sqlQuery3 = "INSERT INTO Neighborhoods" + "(NeighborhoodName, BusinessId) VALUES" + "(?,?)";
		PreparedStatement pstmt3 = con.prepareStatement(sqlQuery3);
		String sqlQuery4 = "INSERT INTO BusinessAttributes" + "(BusinessId, AttributeName, AttributeValue) VALUES" + "(?,?,?)";
		PreparedStatement pstmt4 = con.prepareStatement(sqlQuery4);
		String sqlQuery5 = "INSERT INTO BusinessCategory" + "(CategoryName, BusinessId) VALUES" + "(?,?)";
		PreparedStatement pstmt5 = con.prepareStatement(sqlQuery5);
		String sqlQuery6 = "INSERT INTO BusinessSubCategory" + "(SubCategoryName, BusinessId) VALUES" + "(?,?)";
		PreparedStatement pstmt6 = con.prepareStatement(sqlQuery6);
 	 	
		FileReader file = new FileReader(fileName);
    	BufferedReader bfreader = new BufferedReader (file);
    	
    	String line = null;
    	JSONParser parser = new JSONParser();
    	JSONObject ob1;
    			
    	while((line = bfreader.readLine()) != null) {
    		ob1 = (JSONObject)parser.parse(line);
    		
    		String business_id = (String) ob1.get("business_id");
    		pstmt1.setString(1, business_id);
    		
    		String address=(String) ob1.get("full_address");
    		pstmt1.setString(2, address);
    		
    		boolean open =(boolean) ob1.get("open");
    		String open_now=null;
    		if(open) open_now="True";
    		else open_now="False";
    		pstmt1.setString(3, open_now);
    		
    		String city =(String) ob1.get("city");
    		pstmt1.setString(4, city);
    		
    		int review_count =((Long) ob1.get("review_count")).intValue();
    		pstmt1.setInt(5, review_count);
    		
    		String name =(String) ob1.get("name");
    		pstmt1.setString(6, name);
    		
    		float longitude =((Double) ob1.get("longitude")).floatValue();
    		pstmt1.setFloat(7, longitude);
    		
    		String state =(String) ob1.get("state");
    		pstmt1.setString(8, state);
    		
    		float stars =((Double) ob1.get("stars")).floatValue();
    		pstmt1.setFloat(9, stars);
    		
    		float latitude =((Double) ob1.get("latitude")).floatValue();
    		pstmt1.setFloat(10, latitude);
    		
    		String type = (String) ob1.get("type");
    		pstmt1.setString(11, type);
    		
    		pstmt1.executeUpdate();
    		
    		JSONObject ob2 = (JSONObject) ob1.get("hours");
    		if(ob2 !=null) {				
				for (Object key : ob2.keySet()){
			        String keyStr = (String)key;				        
			        JSONObject ob3 = (JSONObject) ob2.get(key);
			        String openHour = (String) ob3.get("open");
			        String closeHour = (String) ob3.get("close");
			        pstmt2.setString(1, keyStr);
			        pstmt2.setString(2, openHour);
			        pstmt2.setString(3, closeHour);
			        pstmt2.setString(4, business_id);
			        pstmt2.executeUpdate();				       				    
			    }
			}
    		
    		JSONArray obArray= (JSONArray) ob1.get("neighborhoods");
    		if(obArray != null){
    			String neighnorName;
    			for(Object key2: obArray){
    				neighnorName = (String)key2;
    				pstmt3.setString(1, neighnorName);
    				pstmt3.setString(2, business_id);
    				pstmt3.executeUpdate();
    			}
    		}
    		
    		JSONObject ob4 = (JSONObject) ob1.get("attributes");
    		if(ob4 != null){
				for (Object key : ob4.keySet()) {
					String attributeName = (String)key;
			        Object attributeValue = ob4.get(attributeName);	
			        
			        if (attributeValue instanceof JSONObject) {
			        	JSONObject ob5 = (JSONObject) ob4.get(key);
			        	for (Object key2 : ob5.keySet()) {
			        		String keyName = (String)key2;
			        		Object keyValue = ob5.get(keyName);
			        		if (keyValue instanceof Integer) {
			        			String a_name = attributeName + " "+ keyName;
			        			String a_value = ((Long) ob5.get(keyName)).toString();
			        			pstmt4.setString(1, business_id);
								pstmt4.setString(2, a_name);
								pstmt4.setString(3, a_value);
								pstmt4.executeUpdate();
			        		}
			        		else if (keyValue instanceof String) {
			        			String a_name = attributeName + " "+ keyName;
			        			String a_value = (String) ob5.get(keyName);
			        			pstmt4.setString(1, business_id);
								pstmt4.setString(2, a_name);
								pstmt4.setString(3, a_value);
								pstmt4.executeUpdate();
			        		}
			        		else if (keyValue instanceof Boolean) {
			        			String a_name = attributeName + " "+ keyName;
			        			boolean a = (Boolean) ob5.get(keyName);
			        			String a_value = String.valueOf(a);
			        			pstmt4.setString(1, business_id);
								pstmt4.setString(2, a_name);
								pstmt4.setString(3, a_value);
								pstmt4.executeUpdate();
			        		}
			        	}
			        }
			        else {
			        	if (attributeValue instanceof Integer) {
		        			String a_value = ((Long) ob4.get(attributeName)).toString();
							pstmt4.setString(1, business_id);
							pstmt4.setString(2, attributeName);
							pstmt4.setString(3, a_value);
							pstmt4.executeUpdate();
		        		}
		        		else if (attributeValue instanceof String) {
		        			String a_value = (String) ob4.get(attributeName);
		        			pstmt4.setString(1, business_id);
							pstmt4.setString(2, attributeName);
							pstmt4.setString(3, a_value);
							pstmt4.executeUpdate();
		        		}
		        		else if (attributeValue instanceof Boolean) {
		        			boolean a = (Boolean) ob4.get(attributeName);
		        			String a_value = String.valueOf(a);
		        			pstmt4.setString(1, business_id);
							pstmt4.setString(2, attributeName);
							pstmt4.setString(3, a_value);
							pstmt4.executeUpdate();
		        		} 
			        }
			        
			    }
			}
    		
    		JSONArray c_array = (JSONArray) ob1.get("categories");
			Iterator<String> iterator = c_array.iterator();
			String category1;
	
			while(iterator.hasNext()) {
				category1 = iterator.next();
				if(category1.equals("Active Life") || category1.equals("Arts & Entertainment") || category1.equals("Automotive") || 
						category1.equals("Car Rental") || category1.equals("Cafes") || category1.equals("Beauty & Spas") || 
						category1.equals("Convenience Stores") || category1.equals("Dentists") || category1.equals("Doctors") ||
						category1.equals("Drugstores") || category1.equals("Department Stores") || category1.equals("Education") ||
						category1.equals("Event Planning & Services") || category1.equals("Flowers & Gifts") || 
						category1.equals("Food") || category1.equals("Health & Medical") || category1.equals("Home Services") ||
						category1.equals("Home & Garden") || category1.equals("Hospitals") || category1.equals("Hotels & Travel") ||
						category1.equals("Hardware Stores") || category1.equals("Grocery") || category1.equals("Medical Centers") ||
						category1.equals("Nurseries & Gardening") || category1.equals("Nightlife") || category1.equals("Restaurants") ||
						category1.equals("Shopping") || category1.equals("Transportation"))
				{
					pstmt5.setString(1, category1);
					pstmt5.setString(2, business_id);
					pstmt5.executeUpdate();
				}
				else
				{
					pstmt6.setString(1, category1);
					pstmt6.setString(2, business_id);
					pstmt6.executeUpdate();
				}				
			}    		
    	}
    	pstmt1.close();
    	pstmt2.close();
    	pstmt3.close();
    	pstmt4.close();
    	pstmt5.close();
    	pstmt6.close();
    	bfreader.close();
    	con.close();
    }
	// parsing Yelp Checkin JSON file and populating YelpCheckin Data into database
	public static void readCheckin(String fileName) throws IOException, ParseException, SQLException{
		Connection con = getDBConnection();
		
		String sqlQuery1 = "INSERT INTO CheckIN" + "(BusinessId, CheckinHour, CheckinDayNum, CheckinDay, CheckinCount) VALUES" + "(?,?,?,?,?)";
		PreparedStatement pstmt1 = con.prepareStatement(sqlQuery1);
		 	 	
		FileReader file = new FileReader(fileName);
    	BufferedReader bfreader = new BufferedReader (file);
    	
    	String line = null;
    	JSONParser parser = new JSONParser();
    	JSONObject ob1;
    			
    	while((line = bfreader.readLine()) != null) {
    		ob1 = (JSONObject)parser.parse(line);
    		
    		String business_id = (String) ob1.get("business_id");
    		pstmt1.setString(1, business_id);
    		
    		JSONObject checkinInfo = (JSONObject)ob1.get("checkin_info");
    			for(Object key : checkinInfo.keySet()){
    				String str = (String) key;
    				String []arr =str.split("-");
    				int dayNum = Integer.parseInt(arr[1]);
    				String day = splitConvertHour(arr[1]);
    				int hour = Integer.parseInt(arr[0]);
    				int count = ((Long)checkinInfo.get(key)).intValue();    				
    				pstmt1.setInt(2, hour);
    				pstmt1.setInt(3,dayNum);
    				pstmt1.setString(4, day);
    				pstmt1.setInt(5, count);   				
    	    		pstmt1.executeUpdate();
    			}			 		
    	}
    	pstmt1.close();
    	bfreader.close();
    	con.close();
    }
	
	public static String splitConvertHour(String day){
		 if (day.equals("0"))		
				return "SUNDAY";
		 
		 else if (day.equals("1"))	
				return "MONDAY";
		 
		 else if (day.equals("2"))
			 	return "TUESDAY";
		 
		 else if (day.equals("3"))
			 return "WEDNESDAY";
		 
		 else if (day.equals("4"))
				return "THURSDAY";
			
		 else if (day.equals("5"))
				return "FRIDAY";
			
		 else if (day.equals("6"))
				return "SATURDAY";
		return "Not Valid";
	}
	// parsing Yelp Review JSON file and populating YelpReview Data into database
	public static void readReview(String fileName) throws IOException, ParseException, SQLException{
		Connection con = getDBConnection();
		
		String sqlQuery1="INSERT INTO Review"+ "(FunnyVote, UsefulVote, CoolVote,TotalVotes, UserId, ReviewId, Stars, RDate, RText, RType, BusinessId) VALUES" + "(?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt1 = con.prepareStatement(sqlQuery1);
		 	 	
		FileReader file = new FileReader(fileName);
    	BufferedReader bfreader = new BufferedReader (file);
    	
    	String line = null;
    	JSONParser parser = new JSONParser();
    	JSONObject ob1;
    			
    	while((line = bfreader.readLine()) != null) {
    		ob1 = (JSONObject)parser.parse(line);
    		
    		JSONObject votes = (JSONObject)ob1.get("votes");
    		int funnyVotes = ((Long)votes.get("funny")).intValue();
    		int usefulVotes = ((Long)votes.get("useful")).intValue();
    		int coolVotes = ((Long)votes.get("cool")).intValue();
    		int totalVotes =0;
    		totalVotes = funnyVotes + usefulVotes + totalVotes;
    		pstmt1.setInt(1, funnyVotes);
    		pstmt1.setInt(2, usefulVotes);
    		pstmt1.setInt(3, coolVotes);
    		pstmt1.setInt(4, totalVotes);

    		String userId = (String) ob1.get("user_id");
			pstmt1.setString(5, userId);
			
			String reviewId = (String) ob1.get("review_id");
			pstmt1.setString(6, reviewId);
			
			int stars = ((Long) ob1.get("stars")).intValue();
			pstmt1.setInt(7, stars);
			
			String date = (String) ob1.get("date");
			pstmt1.setString(8, date);
			
			String text = (String) ob1.get("text");
			pstmt1.setString(9, text);
			
			String type = (String) ob1.get("type");
			pstmt1.setString(10, type);
			
			String businessId = (String) ob1.get("business_id");
			pstmt1.setString(11, businessId);
			
			pstmt1.executeUpdate();
    	}
    	pstmt1.close();
    	bfreader.close();
    	con.close();
    }
	
	public static void truncateTables() throws SQLException{
		Connection con = getDBConnection();
		String sqlQuery13="Alter Table Review Drop CONSTRAINT Fk1_Review Drop CONSTRAINT Fk2_Review ";
		String sqlQuery14="Alter Table CheckIN Drop CONSTRAINT Fk_CheckIN ";
		String sqlQuery15="Alter Table BusinessSubCategory Drop CONSTRAINT Fk_BusinessSubCategory ";
		String sqlQuery16="Alter Table BusinessCategory Drop CONSTRAINT Fk_BusinessCategory ";
		String sqlQuery17="Alter Table BusinessAttributes Drop CONSTRAINT Fk_BusinessAttributes ";
		String sqlQuery18="Alter Table Neighborhoods Drop CONSTRAINT Fk_Neighborhoods ";
		String sqlQuery19="Alter Table BusinessHours Drop CONSTRAINT Fk_BusinessHours ";
		String sqlQuery20="Alter Table Compliments Drop CONSTRAINT Fk_Compliments ";
		String sqlQuery21="Alter Table EliteYears Drop CONSTRAINT Fk_EliteYearsId ";
		String sqlQuery22="Alter Table Friends Drop CONSTRAINT Fk_FriendsId ";
		PreparedStatement pstmt13 = con.prepareStatement(sqlQuery13);
		PreparedStatement pstmt14 = con.prepareStatement(sqlQuery14);
		PreparedStatement pstmt15 = con.prepareStatement(sqlQuery15);
		PreparedStatement pstmt16 = con.prepareStatement(sqlQuery16);
		PreparedStatement pstmt17 = con.prepareStatement(sqlQuery17);
		PreparedStatement pstmt18 = con.prepareStatement(sqlQuery18);
		PreparedStatement pstmt19 = con.prepareStatement(sqlQuery19);
		PreparedStatement pstmt20 = con.prepareStatement(sqlQuery20);
		PreparedStatement pstmt21 = con.prepareStatement(sqlQuery21);
		PreparedStatement pstmt22 = con.prepareStatement(sqlQuery22);
		pstmt13.executeUpdate();pstmt14.executeUpdate();pstmt15.executeUpdate();pstmt16.executeUpdate();pstmt17.executeUpdate();pstmt18.executeUpdate();pstmt19.executeUpdate();pstmt20.executeUpdate();pstmt21.executeUpdate();pstmt22.executeUpdate();
		  String sqlQuery1="Truncate Table Review";
		  String sqlQuery2="Truncate Table CheckIN";
		  String sqlQuery3="Truncate Table Compliments";
		  String sqlQuery4="Truncate Table EliteYears";
		  String sqlQuery5="Truncate Table Friends";
		  String sqlQuery6="Truncate Table YelpUser";
		  String sqlQuery7="Truncate Table BusinessSubCategory";
		  String sqlQuery8="Truncate Table BusinessCategory";
		  String sqlQuery9="Truncate Table BusinessAttributes";
		  String sqlQuery10="Truncate Table Neighborhoods";
		  String sqlQuery11="Truncate Table BusinessHours";
		  String sqlQuery12="Truncate Table Business";
		  PreparedStatement pstmt1 = con.prepareStatement(sqlQuery1);
		  PreparedStatement pstmt2 = con.prepareStatement(sqlQuery2);
		  PreparedStatement pstmt3 = con.prepareStatement(sqlQuery3);
		  PreparedStatement pstmt4 = con.prepareStatement(sqlQuery4);
		  PreparedStatement pstmt5 = con.prepareStatement(sqlQuery5);
		  PreparedStatement pstmt6 = con.prepareStatement(sqlQuery6);
		  PreparedStatement pstmt7 = con.prepareStatement(sqlQuery7);
		  PreparedStatement pstmt8 = con.prepareStatement(sqlQuery8);
		  PreparedStatement pstmt9 = con.prepareStatement(sqlQuery9);
		  PreparedStatement pstmt10 = con.prepareStatement(sqlQuery10);
		  PreparedStatement pstmt11 = con.prepareStatement(sqlQuery11);
		  PreparedStatement pstmt12 = con.prepareStatement(sqlQuery12);
		  pstmt1.executeUpdate();
		  pstmt2.executeUpdate();
		  pstmt3.executeUpdate();
		  pstmt4.executeUpdate();
		  pstmt5.executeUpdate();
		  pstmt6.executeUpdate();
		  pstmt7.executeUpdate();
		  pstmt8.executeUpdate();
		  pstmt9.executeUpdate();
		  pstmt10.executeUpdate();
		  pstmt11.executeUpdate();
		  pstmt12.executeUpdate();
		  String sqlQuery23="Alter Table Friends ADD CONSTRAINT Fk_FriendsId FOREIGN  KEY (UserId) REFERENCES YelpUser (UserId) ON DELETE CASCADE ";
		  String sqlQuery24="Alter Table EliteYears ADD CONSTRAINT Fk_EliteYearsId FOREIGN  KEY (UserId) REFERENCES YelpUser (UserId) ON DELETE CASCADE ";
		  String sqlQuery25="Alter Table Compliments ADD CONSTRAINT Fk_Compliments FOREIGN  KEY (UserId) REFERENCES YelpUser (UserId) ON DELETE CASCADE ";
		  String sqlQuery26="Alter Table BusinessHours ADD CONSTRAINT Fk_BusinessHours FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE ";
		  String sqlQuery27="Alter Table Neighborhoods ADD CONSTRAINT Fk_Neighborhoods FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE ";
		  String sqlQuery28="Alter Table BusinessAttributes ADD CONSTRAINT Fk_BusinessAttributes FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE ";
		  String sqlQuery29="Alter Table BusinessCategory ADD CONSTRAINT Fk_BusinessCategory FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE ";
		  String sqlQuery30="Alter Table BusinessSubCategory ADD CONSTRAINT Fk_BusinessSubCategory FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE ";
		  String sqlQuery31="Alter Table CheckIN ADD CONSTRAINT Fk_CheckIN FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE ";
		  String sqlQuery32="Alter Table Review ADD CONSTRAINT Fk2_Review  FOREIGN  KEY (UserId) REFERENCES YelpUser (UserId) ON DELETE CASCADE ";
		  String sqlQuery33="Alter Table Review ADD  CONSTRAINT Fk1_Review FOREIGN KEY (BusinessId) REFERENCES Business (BusinessId) ON DELETE CASCADE ";
		  PreparedStatement pstmt23 = con.prepareStatement(sqlQuery23);
			PreparedStatement pstmt24 = con.prepareStatement(sqlQuery24);
			PreparedStatement pstmt25 = con.prepareStatement(sqlQuery25);
			PreparedStatement pstmt26 = con.prepareStatement(sqlQuery26);
			PreparedStatement pstmt27 = con.prepareStatement(sqlQuery27);
			PreparedStatement pstmt28 = con.prepareStatement(sqlQuery28);
			PreparedStatement pstmt29 = con.prepareStatement(sqlQuery29);
			PreparedStatement pstmt30 = con.prepareStatement(sqlQuery30);
			PreparedStatement pstmt31 = con.prepareStatement(sqlQuery31);
			PreparedStatement pstmt32 = con.prepareStatement(sqlQuery32);
			PreparedStatement pstmt33 = con.prepareStatement(sqlQuery33);
			pstmt23.executeUpdate();pstmt24.executeUpdate();pstmt25.executeUpdate();pstmt26.executeUpdate();pstmt27.executeUpdate();pstmt28.executeUpdate();pstmt29.executeUpdate();pstmt30.executeUpdate();pstmt31.executeUpdate();pstmt32.executeUpdate();pstmt33.executeUpdate();
			pstmt1.close();pstmt2.close();pstmt3.close();pstmt4.close();pstmt5.close();pstmt6.close();pstmt7.close();pstmt8.close();pstmt9.close();pstmt10.close();pstmt12.close();pstmt12.close();
			pstmt13.close();pstmt14.close();pstmt15.close();pstmt16.close();pstmt17.close();pstmt18.close();pstmt19.close();pstmt20.close();pstmt21.close();pstmt22.close();pstmt23.close();pstmt24.close();
			pstmt25.close();pstmt26.close();pstmt27.close();pstmt28.close();pstmt29.close();pstmt30.close();pstmt31.close();pstmt32.close();pstmt33.close();
			con.close();
	}
}
	

