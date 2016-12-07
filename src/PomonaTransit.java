/**
 *
 * Name :		Wesley Lang & Ning Li
 * Task :		Lab #4 - Pomona Transit System
 * Date : 		December 4,2016
 * Course : 	CS435 - Databases
 * Description: Create pomona transit system that works.
 * 
 */

import java.sql.*;
import java.util.Scanner;


public class PomonaTransit {
	
	//static reference to itself
	private static PomonaTransit instance = new PomonaTransit();

	private static final String URL = "jdbc:mysql://localhost:3306/Lab4?autoReconnect=true&useSSL=false";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver"; 
	
	public static void main(String[] args) {
		menu();
	}
	
	//display the method and get user command
	public static void menu() {
		System.out.println("Please choose from the following:");
		System.out.println("1 - Display schedule for all trips.");
		System.out.println("2 - Edit the schedule");
		System.out.println("3 - Display the stops of a given trip");
		System.out.println("4 - Display the weekly schedule of a given driver and date");
		System.out.println("5 - Add a driver");
		System.out.println("6 - Add a bus");
		System.out.println("7 - Delete a bus");
		System.out.println("8 - Record data of a given trip");
		System.out.println("0 - Exit program");
		
		Scanner in = new Scanner(System.in);
		int input = in.nextInt();
		System.out.println();
		
		//call functions accordingly to user's input then keep the ask again unless
		//user enters 0 to exit the program
		if(input == 0) {
			System.exit(0);
		}
		else if(input == 1) { 
			displaySchedule();

		}
		else if(input == 2){ 
			editSchedule();

		}
		else if(input == 3){ 
			displayStops();

		}
		else if(input == 4){ 
			displayWeeklySchedule();

		}
		else if(input == 5){ 
			addDriver();

		}
		else if(input == 6){ 
			addBus();

		}
		else if(input == 7){ 
			deleteBus();

		}
		else if(input == 8){ 
			recordData();

		}
		else{
			System.out.println("Invalid input. Please try again.");
			menu();
		}
		in.close();
	}

	private static void displaySchedule() {
		//ask the user for start location, destination, and date, then display
		//the corresponding schedule with all information of that particlar trip.
		String startLocationName, destination, date;
		Scanner in = new Scanner(System.in);
		System.out.print("Start Location Name: ");
		startLocationName = in.next();
		System.out.print("Destination Name: ");
		destination = in.next();
		System.out.print("Date (YYYY-MM-DD): ");
		date = in.next();
		
		String query;
		query = "SELECT t.TripNumber, t.StartLocationName, t.DestinationName, to.Date"
				+ ", to.ScheduledStartTime, to.ScheduledArrivalTime, to.DriverName, to.BusId "
				+ "FROM Trip t, TripOffering to "
				+ "WHERE t.TripNumber = to.TripNumber AND "
				+ "t.StartLocationName LIKE '" + startLocationName + "' AND "
				+ "t.DestinationName" + " LIKE '" + destination + "' "+ " AND "
				+ "to.Date LIKE '" + date + "'";
		
		sqlDisplayer(query);
		in.close();
	}
	
	private static void editSchedule() {
		//display option to which information that the user would like to edit
		System.out.println("Editing Options: ");
		System.out.println("1 - Delete a trip offering specified by Trip#, Date, and ScheduledStartTime.");
		System.out.println("2 - Add a set of trip offerings.");
		System.out.println("3 - Change the driver for a given trip.");
		System.out.println("4 - Change the bus for a given trip.");
		
		Scanner in = new Scanner(System.in);
		int input = in.nextInt();
		
		if(input == 1)
			deleteTripOffering();
		else if(input == 2)
			addTripOfferings();
		else if(input == 3)
			changeDriver();
		else if(input == 4)
			changeBus();
		else {
			System.out.println("Invalid input. Try again.");
			editSchedule();
		}
		in.close();
	}
	
	private static void deleteTripOffering() {
		//this function deletes a tripoffering correspond to user's choice
		String sqlVar;
		int tripNumber;
		String date;
		String scheduledStartTime;
		Scanner in = new Scanner(System.in);
	
		System.out.print("TripNumber: ");	
		tripNumber = in.nextInt();
		System.out.print("Date(Please follow 'YYYY-MM-DD' format): ");	
		date = in.next();
		System.out.print("ScheduledStartTime(Please follow 'HH:MM' 24H format): ");	
		scheduledStartTime = in.next() + ":00";
	
		sqlVar = "DELETE FROM TripOffering WHERE "
				+ "TripNumber='" + tripNumber + "'" 
				+ " AND Date='" + date + "'"
				+ " AND ScheduledStartTime='" + scheduledStartTime
				+"';";
		sqlHandler(sqlVar);
		
		System.out.println("Trip Deleted Successfully!");
		in.close();
	}

	private static void addTripOfferings() {
		//this function adds a tripoffering according to user's choice
		String sqlVar;
		int tripNumber;
		int busID;
		String date;
		String scheduledStartTime, scheduledArrivalTime;
		String driverName;
		Scanner in = new Scanner(System.in);
	
		System.out.print("TripNumber: ");	
		tripNumber = in.nextInt();
		System.out.print("Date(Please follow 'YYYY-MM-DD' format): ");	
		date = in.next();
		System.out.print("ScheduledStartTime(Please follow 'HH:MM' 24H format): ");	
		scheduledStartTime = in.next() + ":00";
		System.out.print("ScheduledArrivalTime(Please follow 'HH:MM' 24H format): ");	
		scheduledArrivalTime = in.next() + ":00";
		System.out.print("DriverName: ");	
		driverName = in.next();
		System.out.print("BusID: ");	
		busID = in.nextInt();
	
		sqlVar = "INSERT INTO TripOffering VALUES("
				+ tripNumber + ",'" 
				+ date + "','" 
				+ scheduledStartTime + "','"
				+ scheduledArrivalTime + "','"
				+ driverName + "',"
				+ busID
				+");";
		sqlHandler(sqlVar);
		
		System.out.println("Trip Added Successfully! Add another? ('Y' or 'N')");
		String input = in.next();
		if(input.toUpperCase().equals("Y"))addTripOfferings();
		in.close();
	}

	private static void changeDriver() {
		//this function change the driver with user's input
		String sqlVar;
		int tripNumber;
		String date;
		String scheduledStartTime;
		String driverName;
		Scanner in = new Scanner(System.in);
	
		System.out.print("TripNumber: ");	
		tripNumber = in.nextInt();
		System.out.print("Date(Please follow 'YYYY-MM-DD' format): ");	
		date = in.next();
		System.out.print("ScheduledStartTime(Please follow 'HH:MM' 24H format): ");	
		scheduledStartTime = in.next() + ":00";
		System.out.print("New Bus Driver: ");
		driverName = in.next();
	
		sqlVar = "UPDATE TripOffering "
				+ "SET DriverName='" + driverName + "'"
				+ "WHERE "
				+ "TripNumber='" + tripNumber + "'" 
				+ " AND Date='" + date + "'"
				+ " AND ScheduledStartTime='" + scheduledStartTime
				+"';";
		sqlHandler(sqlVar);
		
		System.out.println("Driver Changed Successfully!");
		in.close();
	}

	private static void changeBus() {
		//this function changes the bus with user's input
		String sqlVar;
		int tripNumber;
		String date;
		String scheduledStartTime;
		int busID;
		Scanner in = new Scanner(System.in);
	
		System.out.print("TripNumber: ");	
		tripNumber = in.nextInt();
		System.out.print("Date(Please follow 'YYYY-MM-DD' format): ");	
		date = in.next();
		System.out.print("ScheduledStartTime(Please follow 'HH:MM' 24H format): ");	
		scheduledStartTime = in.next() + ":00";
		System.out.print("New Bus: ");
		busID = in.nextInt();
	
		sqlVar = "UPDATE TripOffering "
				+ "SET BusID='" + busID + "'"
				+ "WHERE "
				+ "TripNumber='" + tripNumber + "'" 
				+ " AND Date='" + date + "'"
				+ " AND ScheduledStartTime='" + scheduledStartTime
				+"';";
		sqlHandler(sqlVar);
		
		System.out.println("Bus Changed Successfully!");
		in.close();
	}

	private static void displayStops() {
		//this function displays the stops of a trip with user's input
		String query;
		int tripNumber;
		Scanner in = new Scanner(System.in);
	
		System.out.print("TripNumber: ");	
		tripNumber = in.nextInt();
		query = "SELECT * FROM TripStopInfo WHERE TripNumber=" + tripNumber + ";";
		
		sqlDisplayer(query);
		in.close();
	}
	
	private static void displayWeeklySchedule() {
		//this function display the weekly schedule with user's input of name and date
		//then sorts trip offering by given name, then date
		Scanner in = new Scanner(System.in);
		System.out.print("Enter in a driver's name: ");
		String driverName = in.next();
		System.out.print("Given month(two digits): ");
		String month = in.next();
		System.out.print("Given day(two digits): ");
		int day = in.nextInt();
		System.out.print("Given year (two digits): ");
		String year = in.next();
				
		int i = 1;
		while (i <= 7) {
			String tempDay;
			if(day < 9) {
				tempDay = "0" + day;
			} else {
				tempDay = "" + day;
			}
			String query = "SELECT * FROM TripOffering WHERE DATE LIKE '00"
					+ month + "-" + tempDay + "-" + year + "' AND DriverName LIKE '" + driverName + "'";
			sqlDisplayer(query);
			i++;
			day++;
		}
		in.close();
	}
	
	private static void addDriver() {
		//this function add a driver per user's request with name and phone number
		String driverName;
		String sqlVar;
		String driverTelephoneNumber;
		Scanner in = new Scanner(System.in);

		System.out.print("DriverName: ");	
		driverName = in.next();
		System.out.print("DriverTelephoneNumber (Please follow XXX-XXXX format): ");
		driverTelephoneNumber = in.next();
		sqlVar = "INSERT INTO DRIVER VALUES('" + driverName + "', '" + driverTelephoneNumber + "');";
		sqlHandler(sqlVar);
		
		System.out.println("Driver Added Successfully!");
		in.close();
	}
	
	private static void addBus() {
		//this function add a bus according to user's input of new bus ID, model and year
		String model = "NULL";
		String sqlVar;
		int busID = 0;
		int year = 0;
		Scanner in = new Scanner(System.in);

		System.out.print("BusID: ");	
		busID = in.nextInt();
		System.out.print("Model: ");	
		model = in.next();
		System.out.print("Year: ");
		year = in.nextInt();
		
		sqlVar = "INSERT INTO BUS VALUES('" + busID + "', '" + model + "', '" + year + "');";
		sqlHandler(sqlVar);
		
		System.out.println("Bus Added Successfully!");
		in.close();
	}
	
	
	private static void deleteBus() {
		//this method prompts user for bus ID to delete that record
		String sqlVar;
		int busID;
		Scanner in = new Scanner(System.in);
	
		System.out.print("BusID: ");	
		busID = in.nextInt();
		sqlVar = "DELETE FROM BUS WHERE BusID='" + busID + "';";
		sqlHandler(sqlVar);
		
		System.out.println("Bus Deleted Successfully!");
		in.close();
	}

	private static void recordData() {
		//this function prompts user to insert the actual data of a given trip offering specified by its key. 
		//the actual data include the attributes of the table actual trip stop info
		
		int tripNumber;
		String date;
		String scheduledStartTime;
		Scanner in = new Scanner(System.in);
		
		System.out.print("TripNumber: ");
		tripNumber = in.nextInt();
		System.out.print("Date (YYYY-MM-DD): ");
		date = in.next();
		System.out.print("ScheduledStartTime (HH:MM:SS): ");
		scheduledStartTime = in.next();
		
		String scheduledArrival = "";
		String stopNumber = "";
		String query;
		
		//get scheduledArrival
		query = "SELECT o.scheduledArrivalTime, t.StopNumber FROM TripOffering o, "
				+ " TripStopInfo t WHERE t.TripNumber = o.TripNumber AND t.TripNumber =" + 
				tripNumber + " AND DATE LIKE '" + date + "' AND ScheduledStartTime LIKE '"
				+ scheduledStartTime + "'";
		
		//gets the scheduledArrival and stopNumber
		try{
			//Load the MySQL Connector / J classes
			Class.forName(DRIVER_CLASS).newInstance();
			
			//Set connect string to local MySQL database
			String connString = URL + "/" + 
					"?user=" + USERNAME + "&password=" + PASSWORD + "&useSSL=false";
			
			Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(query);
			
			//Display column values
			while(rs.next()){
				scheduledArrival = rs.getString(1);
				stopNumber = rs.getString(2);
			}
			
			//closes resultSet and connection
			rs.close();
			conn.close();			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		//insert into actualtripstopinfo
		String sqlVar = "INSERT INTO ActualTripStopInfo (TripNumber, "
				+ "Date, ScheduledStartTime, StopNumber, ScheduledArrivalTime) "
				+ "VALUES ('" + tripNumber + "', '" + date + "', '"
				+ scheduledStartTime + "', '" + stopNumber + "', '" 
				+ scheduledArrival + "')";
		
		sqlHandler(sqlVar);
		in.close();
	}
	
	private static void sqlHandler(String sqlVar){
		try{

			//Load the MySQL Connector / J classes
			Class.forName(DRIVER_CLASS).newInstance();
			
			//Set connect string to local MySQL database
			String connString = URL + "/" + 
					"?user=" + USERNAME + "&password=" + PASSWORD + "&useSSL=false";
			
			System.out.println("Trying connection with " + connString);
			Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
			//Get result set
			Statement stmt = conn.createStatement();
			String varSQL = sqlVar;
			stmt.executeUpdate(varSQL);
			
			stmt.close();
			conn.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void sqlDisplayer(String query){
		try{
			//Load the MySQL Connector / J classes
			Class.forName(DRIVER_CLASS).newInstance();
			
			//Set connect string to local MySQL database
			String connString = URL + "/" + 
					"?user=" + USERNAME + "&password=" + PASSWORD + "&useSSL=false";
			
			System.out.println("Trying connection with " + connString);
			Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			Statement stmt = conn.createStatement();

			String select = query;
			ResultSet rs = stmt.executeQuery(select);
			//Get meta data on just opened result set
			ResultSetMetaData rsMeta = rs.getMetaData();
			
			// Display Column names as string
			String varColNames = "";
			int varColCount = rsMeta.getColumnCount();
			for(int col = 1; col <= varColCount;col++){
				varColNames = varColNames + rsMeta.getColumnName(col)+ " ";
			}
			System.out.println(varColNames);
			
			//Display column values
			while(rs.next()){
				for(int col = 1; col <= varColCount;col++){
					System.out.print(rs.getString(col) + " ");
				}
				System.out.println();
			}
			
			//closes Resultset and connection
			rs.close();
			conn.close();			
		} catch(Exception e){
			e.printStackTrace();
		}	
	}
}
