import java.util.*;
import java.sql.*;
import java.text.*;
import java.io.*;

public class application {
  //Administrator
  static void AdministratorMenu(Connection con){
  	System.out.println("-----Operations for administrator menu-----");
    System.out.println("What kinds of operations would you like to perform?");
    System.out.println("1. Create all tables");
    System.out.println("2. Delete all tables");
    System.out.println("3. Load from datafile");
    System.out.println("4. Show number of records in each table");
    System.out.println("5. Return to the main menu");
    System.out.print("Enter Your Choice: ");
    Scanner sc = new Scanner(System.in);
    int choice = sc.nextInt();
    while(choice <1 || choice>5){
      System.out.print("The Choice should be within 1-5. Enter again");
      choice = sc.nextInt();
    }
    
    switch(choice){
    	case 1:
      	CreateAllTables(con);
        break;
      case 2:
      	DeleteAllTables(con);
        break;
      case 3:
        LoadFromDatafile(con);
        break;
      case 4:
        ShowNumberOfRecordsInTable(con);
        break;
      case 5:
      	MainMenu(con);
        break;
    }
    AdministratorMenu(con);
  }
  static void CreateAllTables(Connection con){
		String sql_create_user_category =	"CREATE TABLE USER_CATEGORY" +
      "(user_category_id INTEGER NOT NULL Check (user_category_id between 0 and 9), " +
    	"max_cars INTEGER NOT NULL Check (max_cars between 0 and 9), " +
    	"loan_period INTEGER NOT Null Check(loan_period between 0 and 99));";
    
    String sql_create_user = "CREATE TABLE USER" +
  	  "(user_id VARCHAR (12) NOT NULL, " +
    	"name VARCHAR (25) NOT NULL, " +
      "age INTEGER NOT NULL Check (age between 0 and 99), " +
      "occupation VARCHAR (20) NOT NULL, " +
      "user_category_id INTEGER NOT NULL Check (user_category_id between 0 and 9));";

    String sql_create_car_category =  "CREATE TABLE CAR_CATEGORY" +
      "(car_category_id INTEGER NOT NULL Check (car_category_id between 0 and 9), " +
      "car_category_name VARCHAR (20) NOT NULL);";

    String sql_create_car = "CREATE TABLE CAR" +
			"(call_number VARCHAR (8) NOT NULL, "+
      "number_of_copies INTEGER NOT NULL Check (number_of_copies between 0 and 9), "+
      "car_name VARCHAR (10) NOT NULL, " +
      "company VARCHAR (25) NOT NULL, " +
      "date_of_manufacture Date, " +
      "number_of_times_rented INTEGER NOT NULL Check (number_of_times_rented between 0 and 99), "+
      "car_category_id INTEGER NOT NULL Check (car_category_id between 0 and 9));";

    String sql_create_rent = "CREATE TABLE RENT" +
      "(call_number VARCHAR (8) NOT NULL, " +
      "copy_number INTEGER NOT NULL Check (copy_number between 0 and 9)," +
      "user_id VARCHAR(12) NOT NULL," +
      "rent_date DATE," +
      "return_date DATE);";

    try {
      Statement stmt = con.createStatement();
      System.out.println("Processing..");
      stmt.executeUpdate(sql_create_user_category);
      stmt.executeUpdate(sql_create_user);
      stmt.executeUpdate(sql_create_car_category);
      stmt.executeUpdate(sql_create_car);
      stmt.executeUpdate(sql_create_rent);
      System.out.println("Done. Database is initializaed.");
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  static void DeleteAllTables(Connection con){
		String sql_drop_user_category = "DROP TABLE USER_CATEGORY;";
		String sql_drop_user = "DROP TABLE USER;";
    String sql_drop_car_category = "DROP TABLE CAR_CATEGORY;";
    String sql_drop_car =	"DROP TABLE CAR;";
    String sql_drop_rent = "DROP TABLE RENT;";
    try {
      Statement stmt = con.createStatement();
      System.out.println("Processing..");
      stmt.executeUpdate(sql_drop_user_category);
      stmt.executeUpdate(sql_drop_user);
      stmt.executeUpdate(sql_drop_car_category);
      stmt.executeUpdate(sql_drop_car);
      stmt.executeUpdate(sql_drop_rent);
      System.out.println("Done. Database is removed.");
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  static void LoadFromDatafile(Connection con){
    System.out.print("Type in the Source Data Folder Path: ");
    Scanner sc = new Scanner(System.in);
    String path = sc.nextLine();
    PreparedStatement ps;
    File file;

    //user_category.txt
		String sql_user_category = "INSERT INTO USER_CATEGORY (user_category_id, max_cars, loan_period)" +
      "VALUES (?,?,?);";
    try{
      ps = con.prepareStatement(sql_user_category);
      file = new File(path+"/user_category.txt");
      sc = new Scanner(file);
      while(sc.hasNextLine()){
        int user_category_id = sc.nextInt();
        int max_cars = sc.nextInt();
        int loan_period = sc.nextInt();
        ps.setInt(1, user_category_id);
        ps.setInt(2, max_cars);
        ps.setInt(3, loan_period);
        ps.execute();
      }
    } catch (Exception e) {
      System.out.println(e);
    }

    //user.txt
    String sql_user = "INSERT INTO USER (user_id, name, age, occupation, user_category_id)" +
      "VALUES(?,?,?,?,?);";
    try {
      ps = con.prepareStatement(sql_user);
      file = new File(path+"/user.txt");
      sc = new Scanner(file);
      while(sc.hasNextLine()){
        String user_id = sc.next();
        String name = sc.next();
        if(!sc.hasNextInt()){
          name = name + " " + sc.next();
        }
        int age = sc.nextInt();
        String occupation = sc.next();
        int user_category_id = sc.nextInt();
        ps.setString(1, user_id);
        ps.setString(2, name);
        ps.setInt(3, age);
        ps.setString(4, occupation);
        ps.setInt(5,user_category_id);
        ps.execute();
      }
    } catch (Exception e) {
      System.out.println(e);
    }

    //car_category.ext
    String sql_car_category = "INSERT INTO CAR_CATEGORY (car_category_id, car_category_name)" +
      "VALUES (?,?);";
    try {
      ps = con.prepareStatement(sql_car_category);
      file = new File(path+"/car_category.txt");
      sc = new Scanner(file);
      while(sc.hasNextLine()){
        int car_category_id = sc.nextInt();
        String car_category_name = sc.next();
        ps.setInt(1, car_category_id);
        ps.setString(2, car_category_name);
        ps.execute();
      }
    } catch (Exception e) {
      System.out.println(e);
    }

    //car.txt
    String sql_car = "INSERT INTO CAR(call_number, number_of_copies, car_name, company, date_of_manufacture, number_of_times_rented, car_category_id)" +
      "VALUES (?,?,?,?,?,?,?);";
    try {
      ps = con.prepareStatement(sql_car);
      file = new File(path+"/car.txt");
      sc = new Scanner(file);
      while(sc.hasNextLine()){
        String call_number = sc.next();
        int number_of_copies = sc.nextInt();
        String car_name = sc.next();
        String company = sc.next();
        String date = sc.next();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse(date);
        java.sql.Date mdate = new java.sql.Date(parsed.getTime());
        int number_of_times_rented = sc.nextInt();
        int car_category_id = sc.nextInt();
        ps.setString(1,call_number);
        ps.setInt(2, number_of_copies);
        ps.setString(3, car_name);
        ps.setString(4, company);
        ps.setDate(5, mdate);
        ps.setInt(6, number_of_times_rented);
        ps.setInt(7, car_category_id);
        ps.execute();
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    
    //rent.txt
    String sql_rent = "INSERT INTO RENT (call_number, copy_number, user_id, rent_date, return_date)" +
      "VALUES (?,?,?,?,?);";
    try {
      ps = con.prepareStatement(sql_rent);
      file = new File(path+"/rent.txt");
      sc = new Scanner(file);
      while(sc.hasNextLine()){
        String call_number = sc.next();
        ps.setString(1, call_number);
        int copy_number = sc.nextInt();
        ps.setInt(2, copy_number);
        String user_id = sc.next();
        ps.setString(3, user_id);
        String retDate = sc.next();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse(retDate);
        java.sql.Date rent_date = new java.sql.Date(parsed.getTime());
        ps.setDate(4, rent_date);
        String rtnDate = sc.next();
        String return_date = "NULL".equals(rtnDate) ? null : rtnDate;
        if(return_date == null){
          ps.setNull(5, Types.DATE);
        } else {
          java.util.Date rtn_date = new SimpleDateFormat("yyyy-MM-dd").parse(return_date);
          java.sql.Date sql_rtn_date = new java.sql.Date(rtn_date.getTime());
          ps.setDate(5, sql_rtn_date);
        }
        ps.execute();
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  static void ShowNumberOfRecordsInTable(Connection con){
    System.out.println("Number of records in each table: ");

    String sql = "SELECT COUNT(*) AS rowcount FROM USER_CATEGORY";
    String sql2 = "SELECT COUNT(*) AS rowcount FROM USER";
    String sql3 = "SELECT COUNT(*) AS rowcount FROM CAR_CATEGORY";
    String sql4 = "SELECT COUNT(*) AS rowcount FROM CAR";
    String sql5 = "SELECT COUNT(*) AS rowcount FROM RENT";

    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      rs.next();
      int count1 = rs.getInt("rowcount");
      rs.close();

      ResultSet rs2 = stmt.executeQuery(sql2);
      rs2.next();
      int count2 = rs2.getInt("rowcount");
      rs2.close();

      ResultSet rs3 = stmt.executeQuery(sql3);
      rs3.next();
      int count3 = rs3.getInt("rowcount");
      rs3.close();

      ResultSet rs4 = stmt.executeQuery(sql4);
      rs4.next();
      int count4 = rs4.getInt("rowcount");
      rs4.close();

      ResultSet rs5 = stmt.executeQuery(sql5);
      rs5.next();
      int count5 = rs5.getInt("rowcount");
      rs5.close();

      System.out.println("user_category :" + count1);
      System.out.println("user :" + count2);
      System.out.println("car_category :" + count3);
      System.out.println("car :" + count4);
      System.out.println("rent :" + count5);
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  //End of administrator
  //User
  static void UserMenu(Connection con){
    System.out.println("-----User Menu-----");
		System.out.println("What kind of operation would you like to perform?");
		System.out.println("1. Search for Cars");
		System.out.println("2. Show loan record of a user");
		System.out.println("3. Return to the main menu");
		System.out.print("Enter Your Choice: ");
    Scanner sc = new Scanner(System.in);
    int choice = sc.nextInt();
    while(choice <1 || choice>3){
      System.out.print("The Choice should be within 1-3. Enter again");
      choice = sc.nextInt();
    }
    switch(choice){
    	case 1:
      	SearchCar(con);
        break;
      case 2:
      	LoanRecord(con);
        break;
      case 3:
      	MainMenu(con);
        break;
    }
    UserMenu(con);
  }
  static void SearchCar(Connection con){
    System.out.println("Choose the Search criterion: ");
		System.out.println("1. call number ");
		System.out.println("2. name ");
		System.out.println("3. company ");
		System.out.print("Choose the search criterion: ");
    Scanner sc = new Scanner(System.in);
    int choice = sc.nextInt();
    while(choice <1 || choice>3){
      System.out.print("The Choice should be within 1-3. Enter again");
      choice = sc.nextInt();
    }
    switch(choice){
    	case 1:
      	ByCallNumber(con);
        break;
      case 2:
      	ByName(con);
        break;
      case 3:
      	ByCompany(con);
        break;
    }
  }
  static void ByCallNumber(Connection con){
    System.out.print("Type in the call number: ");
		Scanner sc = new Scanner(System.in);
    String search = sc.nextLine();
    while(search.length()!=8){
      System.out.println("Call Number is not 8 characters. Try Again.");
      System.out.print("Enter The Call Number: ");
      search = sc.nextLine();
    }
		System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
    String sql = "SELECT * FROM CAR " +
    "NATURAL JOIN CAR_CATEGORY " +
    "WHERE call_number = '" + search + "'";
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()){
        String CallNum = rs.getString("call_number");
        String CName = rs.getString("car_name");
        String CarCateName = rs.getString("car_category_name");
        String CarCateID = rs.getString("car_category_id");
        String Company = rs.getString("company");
        int NumofCopy = rs.getInt("number_of_copies");
        System.out.format("|%s|%s|%s|%s|%s|", CallNum, CName, CarCateName, Company, NumofCopy);
        System.out.println("");
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  static void ByName(Connection con){
    System.out.print("Type in the Search Keyword: ");
		Scanner sc = new Scanner(System.in);
    String search = sc.nextLine();
		System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
    String sql = "SELECT * FROM CAR " +
    "NATURAL JOIN CAR_CATEGORY " +
    "WHERE car_name LIKE '%"+ search +"%'";
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()){
        String CallNum = rs.getString("call_number");
        String CName = rs.getString("car_name");
        String CarCateName = rs.getString("car_category_name");
        String CarCateID = rs.getString("car_category_id");
        String Company = rs.getString("company");
        int NumofCopy = rs.getInt("number_of_copies");
        System.out.format("|%s|%s|%s|%s|%s|", CallNum, CName, CarCateName, Company, NumofCopy);
        System.out.println("");
      }
    System.out.println("");
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  static void ByCompany(Connection con){
    System.out.print("Type in the company: ");
		Scanner sc = new Scanner(System.in);
    String search = sc.nextLine();
		System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
    String sql = "SELECT * FROM CAR " +
    "NATURAL JOIN CAR_CATEGORY " +
    "WHERE company LIKE '%" + search + "%'";
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()){
      String CallNum = rs.getString("call_number");
      String CName = rs.getString("car_name");
      String CarCateName = rs.getString("car_category_name");
      String CarCateID = rs.getString("car_category_id");
      String Company = rs.getString("company");
      int NumofCopy = rs.getInt("number_of_copies");
      System.out.format("|%s|%s|%s|%s|%s|", CallNum, CName, CarCateName, Company, NumofCopy);
      System.out.println("");
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  static void LoanRecord(Connection con){
    System.out.print("Enter The cuser ID: ");
    Scanner sc = new Scanner(System.in);
    String search = sc.next();
    while(search.length()!=12){
      System.out.println("Cuser ID is not 12 characters. Try Again.");
      System.out.print("Enter The cuser ID: ");
      search = sc.next();
    }
    System.out.println("Loan Record: ");
    System.out.println("|Call Num|Copy Num|Name|Company|Check-out|Returned?|");

    String sql = "SELECT * FROM RENT "+
      "INNER JOIN CAR "+
      "ON RENT.call_number = CAR.call_number "+
      "WHERE user_id = '"+ search +"'"+ 
      "ORDER BY rent_date DESC";
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()){
        String CallNum = rs.getString("call_number");
        int CopyNum = rs.getInt("copy_number");
    	  String CName = rs.getString("car_name");
        String Company = rs.getString("company");
        java.sql.Date RentDate = rs.getDate("rent_date");
        
        String isReturned;
        if(RentDate != null){
          isReturned = "Yes";
        } 
        else{
          isReturned = "No";
        }
        System.out.format("|%s|%s|%s|%s|%s|%s|", CallNum, CopyNum, CName, Company, RentDate, isReturned);
        System.out.println("");
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  //End of user
  //Manager
  static void ManagerMenu(Connection con){
    System.out.println("-----Manager Menu-----");
    System.out.println("What kind of operation would you like to perform?");
    System.out.println("1. Car Renting");
    System.out.println("2. Car Returning");
    System.out.println("3. List all un-returned car copies which are checked-out within a period");
    System.out.println("4. Return to main menu");
    System.out.print("Enter Your Choice: ");
    Scanner sc = new Scanner(System.in);
    int choice = sc.nextInt(); 
    while(choice <1 || choice>4){
      System.out.println("The Choice should be within 1-4. Enter again");
      choice = sc.nextInt();
    }
    switch(choice){
    	case 1:
      	CarRenting(con);
        break;
      case 2:
      	CarReturning(con);
        break;
      case 3:
      	ListUnreturnedCarCopy(con);
        break;
      case 4:
        MainMenu(con);
        break;
    }
    ManagerMenu(con)
  }
  static void CarRenting(Connection con){
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter The User ID: ");
    String userID  = sc.next();
    PreparedStatement ps;

    while(userID.length()!=12){
      System.out.println("User ID is not 12 characters. Try Again.");
      System.out.print("Enter The User ID: ");
      userID = sc.next();
    }
    System.out.print("Enter The Call Number: ");
    String callNumber  = sc.next();
    while(callNumber.length()!=8){
      System.out.println("Call Number is not 8 characters. Try Again.");
      System.out.print("Enter The Call Number: ");
      callNumber = sc.next();
    }
    System.out.print("Enter The Copy Number: ");
    int copyNumber  = sc.nextInt();
    while(copyNumber >9 || copyNumber <0){
      System.out.println("Copy Number is not a 1-digit integer. Try Again.");
      System.out.print("Enter The Copy Number: ");
      copyNumber = sc.nextInt();
    }
    // is not null
    String sql = "SELECT * FROM RENT " +
    "WHERE call_number = '"+callNumber+"' " +
    "AND copy_number = "+copyNumber+" " +
    "AND return_date IS NOT NULL ";
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      if (!rs.isBeforeFirst()){  // no result
        //implement renting
        String sql_insert = "INSERT INTO RENT(call_number, copy_number, user_id, rent_date, return_date)" +
          "VALUES (?,?,?,?,?);";
        ps = con.prepareStatement(sql_insert);
        ps.setString(1, callNumber);
        ps.setInt(2, copyNumber);
        ps.setString(3, userID);
        //get current date
        java.util.Date date = new java.util.Date();
        java.sql.Date rent_date = new java.sql.Date(date.getTime());
        ps.setDate(4, rent_date);
        ps.setNull(5, Types.DATE);
        ps.execute();
        System.out.println("Car renting performed successfully.");
      }
      else
        System.out.println("Car renting unavailable.");
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  static void CarReturning(Connection con){
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter The User ID: ");
    String userID  = sc.next();
    PreparedStatement ps;

    while(userID.length()!=12){
      System.out.println("User ID is not 12 characters. Try Again.");
      System.out.print("Enter The User ID: ");
      userID = sc.next();
    }
    System.out.print("Enter The Call Number: ");
    String callNumber  = sc.next();
    while(callNumber.length()!=8){
      System.out.println("Call Number is not 8 characters. Try Again.");
      System.out.print("Enter The Call Number: ");
      callNumber = sc.next();
    }
    System.out.print("Enter The Copy Number: ");
    int copyNumber  = sc.nextInt();
    while(copyNumber >9 || copyNumber <0){
      System.out.println("Copy Number is not a 1-digit integer. Try Again.");
      System.out.print("Enter The Copy Number: ");
      copyNumber = sc.nextInt();
    }
    //check if record exist
    String sql = "SELECT * FROM RENT " +
      "WHERE user_id = '"+userID+"' " +
      "AND call_number = '"+callNumber+"' " +
      "AND copy_number = "+copyNumber+" " +
      "AND return_date IS NULL ";

    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      if(!rs.isBeforeFirst()){ //no result
        System.out.println("Car returning unavailable.");
      } else {
        java.util.Date date = new java.util.Date();
        java.sql.Date return_date = new java.sql.Date(date.getTime());
        String sql_update = "UPDATE RENT " +
          "SET return_date = '"+return_date+"' ";
        stmt.executeUpdate(sql_update);
        System.out.println("Car returning performed successfully.");
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  static void ListUnreturnedCarCopy(Connection con){
    Scanner sc = new Scanner(System.in);
    System.out.print("Type in the starting date [dd/mm/yyyy]: ");
    String sdate = sc.next();
    System.out.print("Type in the ending date [dd/mm/yyyy]: ");
    String edate = sc.next();
    System.out.println("|UID|CallNum|CopyNum|Checkout|");
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    try {
      java.util.Date sDate = df.parse(sdate);
      java.sql.Date sqlsDate = new java.sql.Date(sDate.getTime());
      java.util.Date eDate = df.parse(edate);
      java.sql.Date sqleDate = new java.sql.Date(eDate.getTime());
      String sql = "SELECT * FROM RENT " +
      "WHERE rent_date > '" + sqlsDate + "' " +
      "AND rent_date < '" + sqleDate + "' " +
      "AND return_date IS NULL " +
      "ORDER BY rent_date DESC;";

      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while(rs.next()){
        String UID = rs.getString("user_id");
        String CallNum = rs.getString("call_number");
        String CopyNum = rs.getString("copy_number");
        java.sql.Date RentingDate = rs.getDate("rent_date");
        System.out.format("|%s|%s|%s|%s|", UID, CallNum, CopyNum, RentingDate);
        System.out.println("");
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  
  //End of manager
  static void MainMenu(Connection con){
    System.out.println("-----Main Menu-----");
    System.out.println("What kinds of operations would you like to perform?");
    System.out.println("1. Operations for Administrator");
    System.out.println("2. Operations for User");
    System.out.println("3. Operations for Manager");
    System.out.println("4. Exit this program");
    System.out.print("Enter Your Choice: ");
    Scanner sc = new Scanner(System.in);
    int choice = sc.nextInt();
    while(choice <1 || choice>4){
      System.out.print("The Choice should be within 1-4. Enter again");
      choice = sc.nextInt();
    }
    switch(choice){
    	case 1:
      	AdministratorMenu(con);
        break;
      case 2:
      	UserMenu(con);
        break;
      case 3:
        ManagerMenu(con);
        break;
      case 4:
      	System.exit(0);
        break;
    }
    MainMenu(con);
  }

  public static void main(String[] args) {
    //Database Connection..
    try{
      String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db23";
      String dbUsername = "Group23";
      String dbPassword = "CSCI3170";
      Class.forName("com.mysql.jdbc.Driver");
      System.out.println("Connecting to database...");
      Connection con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
      System.out.println("Connect successfully.");
      
      //Main menu
      System.out.println("Welcome to Car Renting System!\n");
      MainMenu(con);

    }catch (ClassNotFoundException e){
      System.out.println("[Error]: Java MySQL DB Driver not found!");
      System.exit(0);
    }catch (SQLException e){
      System.out.println(e);
    }
  }
}