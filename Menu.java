import java.sql.ResultSet;

public class Menu {

private static final int NUM_OF_FIND_QUERIES = 4; 
private static final int NUM_OF_UPDATE_QUERIES = 1;
private static final int NUM_OF_MENU_OPTIONS = 2;
QueryManager qm;

private void processFind(UI ui) {
String infoDialog = "You have selected FIND. \n"
		+"(0) All names and addresses of all guests living in London\n"
		+"(1) Average price of a room \n"
		+"(2) Details of all rooms at Grosvenor Hotel\n"
		+"(3) Number of rooms in each hotel in London \n"
		+"(-1) to return.";
ResultSet rset;
while (true) {
int c = ui.getCommand(NUM_OF_FIND_QUERIES, infoDialog);
switch(c) {
case 0:
	/*6.9 list the names and addresses of all guests living in London, alphabetically ordered by name */
	rset = qm.executeQuery("begin open ? for SELECT g.guestName, g.guestAddress" 
					     	 + " FROM Hotel h, Guest g, Booking b"
						 	+ " WHERE g.guestNo = b.guestNo"
						 	+ " AND b.hotelNo = h.hotelNo AND h.city = 'London'"
						 	+ " ORDER BY g.guestName; end;");
	break;
case 1: 
	/*6.13 What is the average price of a room? */
	rset = qm.executeQuery("begin open ? for SELECT AVG(price) AS averagePrice" 
						+ " FROM Room; end;");
	break;
case 2:
	/*6.18 List the details of all rooms at the Grosvenor Hotel, including the name of the guests
	staying in the room, if the room is occupied. Use subqueries and/or joins for this query. */
	rset = qm.executeQuery("begin open ? for SELECT r.*, g.guestName, b.dateFrom, b.dateTo"
		+ " FROM Room r LEFT JOIN Booking b ON r.roomNo = b.roomNo"
		+ " LEFT JOIN Guest g on g.guestNo = b.guestNo"
		+ " WHERE b.dateFrom <= SYSDATE AND b.dateTo >= SYSDATE AND r.hotelNo ="
		+ " (SELECT hotelNo FROM Hotel"
		+ " WHERE hotelName = 'Grosvenor Hotel')"
		+ " ORDER BY b.dateFrom; end;");

	break;
	
case 3:
	/*6.23 List the number of rooms in each hotel in London */
	rset = qm.executeQuery("begin open ? for SELECT r.hotelNo, h.hotelName,"
				+   " COUNT(*) AS numRooms" 
				+   " FROM Room r, Hotel h"
			    +   " WHERE r.hotelNo = h.hotelNo"			
				+   " AND h.city = 'London'"
				+   " GROUP BY r.hotelNo, h.hotelName; end;");
	break;
default:
	return;

} 
if (rset != null)
	ui.printResults(rset);

}

}


private void processUpdate(UI ui) {
		
		String infoDialog = "You have selected UPDATE. \nUpdate... \n"
				+ "(0) The price of all rooms by 5% \n"
				+ "Select (-1) to return.";
		
		int rowsAffected = 0;
		ResultSet rset;
		while (true) {
			int c = ui.getCommand(NUM_OF_UPDATE_QUERIES, infoDialog);
			switch (c) {
			case 0:
				/* Exercise 6.28 update the price of all rooms by 5% */
				qm.executeUpdate("begin UPDATE Room" 
							     +   " SET price = price * 1.05; end;");
				rset = qm.executeQuery("begin open ? for select price as updated_price from Room; end;");
				ui.printResults(rset);
				break;
			default: 
				return; 
			}
			
			// ui.sendMessage(rowsAffected + " rows updated!");
		}
		
		
	}


public void processQueryCommands(UI ui) {
qm = new QueryManager();
String infoDialog = "Welcome to HotelDB. Select an option to begin. \n"
	+"(0) FIND - search hotel information \n"
	+"(1) UPDATE - update hotel information \n"
	+"(-1) to exit.";
while (true) {
			int c = ui.getCommand(NUM_OF_MENU_OPTIONS, infoDialog);
			switch (c) {
			case 0:
				processFind(ui);
				break;
			case 1:
				processUpdate(ui);
				break;
			default:	
				qm.closeConnection();
				return;

}
}
}

}
