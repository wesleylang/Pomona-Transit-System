/**
 *
 * Name :		Wesley Lang & Ning Li
 * Task :		Simple Array Sum
 * Date : 		December 4,2016
 * Course : 	CS435 - Databases
 * Description: Given an array of N integers, can you find the sum of its elements?
 * 
 */	
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Scanner;


public class CS435JDBCLab {
	
	private static String dbHost = "localhost";
	private static String dbName = "pomona_transit";
	private static String dbUser = "JohnCena";
	
	
	public static void main(String[] args) {
		menu();
	}
	
	public static void menu() {
		System.out.println("What would you like to do?");
		System.out.println("[1] Display schedule for all trips.");
		System.out.println("[2] Edit the schedule");
		System.out.println("[3] Display the stops of a given trip");
		System.out.println("[4] Display the weekly schedule of a given driver and date");
		System.out.println("[5] Add a driver");
		System.out.println("[6] Add a bus");
		System.out.println("[7] Delete a bus");
		System.out.println("[8] Record data of a given trip");
		System.out.println("[0] Exit");
		
		Scanner in = new Scanner(System.in);
		int input = in.nextInt();
		
		switch(input) {
		case 0: System.exit(0);
			break;
		case 1: displaySchedule();
			break;
		case 2: editSchedule();
			break;
		case 3: displayStops();
			break;
		case 4: displayWeeklySchedule();
			break;
		case 5: addDriver();
			break;
		case 6: addBus();
			break;
		case 7: deleteBus();
			break;
		case 8: recordData();	
			break;
		default: System.out.println("Invalid input. Try again.");
			menu();
			break;
		}
		in.close();
	}

	private static void displaySchedule() {
		//display the schedule of all trips for a given start location, destination
		//and date. In addition schedule includes schedules start, scheduled arrival,
		//driver id and bus id.
		
		String startLocationName, destination, date;
		Scanner in = new Scanner(System.in);
		System.out.print("Start Location Name: ");
		startLocationName = in.next();
		
		System.out.print("Destination Name: ");
		destination = in.next();
		
		System.out.print("Date (YYYY-MM-DD): ");
		date = in.next();
		
		String query;
		query = "SELECT t.TripNumber, t.StartLocationName, t.DestinationName, o.Date"
				+ ", o.ScheduledStartTime, o.ScheduledArrivalTime, o.DriverName, "
				+ "o.BusId "
				+ "FROM Trip t, TripOffering o "
				+ "WHERE t.TripNumber = o.TripNumber AND "
				+ "t.StartLocationName LIKE '" + startLocationName + "' AND "
				+ "t.DestinationName" + " LIKE '" + destination + "' "+ " AND "
				+ "o.Date LIKE '" + date + "'";
		
		sqlDisplayer(query);
		in.close();
	}
	
	private static void editSchedule() {
		System.out.println("Schedule Editing Options: ");
		System.out.println("[1] Delete a trip offering specified by Trip#, Date, and ScheduledStartTime.");
		System.out.println("[2] Add a set of trip offerings.");
		System.out.println("[3] Change the driver for a given trip.");
		System.out.println("[4] Change the bus for a given trip.");
		
		Scanner in = new Scanner(System.in);
		int input = in.nextInt();
		
		switch(input) {
		case 1: deleteTripOffering();
			break;
		case 2: addTripOfferings();
			break;
		case 3: changeDriver();
			break;
		case 4: changeBus();
			break;
		default: System.out.println("Invalid input. Try again.");
			editSchedule();
			break;
		}
		in.close();
	}
	
	private static void deleteTripOffering() {
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
		
		System.out.println("Trip deleted!");
		in.close();
	}

	private static void addTripOfferings() {
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
		
		System.out.println("Trip Added! Would you like to add another? ('Y' or 'N')");
		String input = in.next();
		if(input.toUpperCase().equals("Y"))addTripOfferings();
		in.close();
	}

	private static void changeDriver() {
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
		
		System.out.println("Driver Changed!");
		in.close();
	}

	private static void changeBus() {
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
		
		System.out.println("Bus Changed!");
		in.close();
	}

	private static void displayStops() {
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
		// display the weekly schedule of a given driver and date
		
		//sort trip offering by given name, then date
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
		String driverName;
		String sqlVar;
		String driverTelephoneNumber;
		Scanner in = new Scanner(System.in);

		System.out.print("DriverName: ");	
		driverName = in.next();
		
		System.out.print("DriverTelephoneNumber (Please follow XXX-XXXX format: ");
		driverTelephoneNumber = in.next();
		
		sqlVar = "INSERT INTO DRIVER VALUES('" + driverName + "', '" + driverTelephoneNumber + "');";
		sqlHandler(sqlVar);
		
		System.out.println("Driver added!");
		in.close();
	}
	
	private static void addBus() {
		// INSERT INTO 'pomona_transit'.'bus'('BusID', 'Model', 'Year');
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
		
		System.out.println("Bus added!");
		in.close();
	}
	
	
	private static void deleteBus() {
		String sqlVar;
		int busID;
		Scanner in = new Scanner(System.in);
	
		System.out.print("BusID: ");	
		busID = in.nextInt();
	
		sqlVar = "DELETE FROM BUS WHERE BusID='" + busID + "';";
		sqlHandler(sqlVar);
		
		System.out.println("Bus deleted!");
		in.close();
	}

	private static void recordData() {
		// insert the actual data of a given trip offering specified by its key. 
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
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			//Set connect string to local MySQL database, user is JohnCena
			String connString = "jdbc:mysql://" + dbHost + "/" + dbName + 
					"?user=" + dbUser + "&password=doot"  + "&useSSL=false";
			
			Connection conn = DriverManager.getConnection(connString);
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(query);
			
			//Display column values
			while(rs.next()){
				scheduledArrival = rs.getString(1);
				stopNumber = rs.getString(2);
			}
			
			//Clean up
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
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			//Set connect string to local MySQL database, user is JohnCena
			String connString = "jdbc:mysql://" + dbHost + "/" + dbName + 
					"?user=" + dbUser + "&password=doot"  + "&useSSL=false";
			
			System.out.println("Trying connection with " + connString);
			Connection conn = DriverManager.getConnection(connString);
			
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
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			//Set connect string to local MySQL database, user is JohnCena
			String connString = "jdbc:mysql://" + dbHost + "/" + dbName + 
					"?user=" + dbUser + "&password=doot"  + "&useSSL=false";
			
			System.out.println("Trying connection with " + connString);
			Connection conn = DriverManager.getConnection(connString);
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
			
			//Clean up
			rs.close();
			conn.close();			
		} catch(Exception e){
			e.printStackTrace();
		}	
	}
}
