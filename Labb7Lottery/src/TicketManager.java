import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TicketManager {
	
	private final int HIGHEST_ALLOWED_ROW_NUMBER; // allowed numbers in row will be from 1 up to highestAllowedNumber
	private ArrayList<LotteryTicket> tickets;
	private ArrayList<Integer> winningRow; // the right numbers
	private LotteryTicket currentTicket;
	
	TicketManager(int highestAllowedRowNumber){
		this.HIGHEST_ALLOWED_ROW_NUMBER = highestAllowedRowNumber;
		this.tickets = new ArrayList<LotteryTicket>();
		this.winningRow = new ArrayList<Integer>();
		// random winning row
		generateWinningRow();
		System.out.println(winningRow);
	}
	
	public int getHIGHEST_ALLOWED_ROW_NUMBER(){
		return HIGHEST_ALLOWED_ROW_NUMBER;
	}
	
	public void setCurrentTicket(LotteryTicket newCurrentTicket){
		this.currentTicket = newCurrentTicket;
	}
	
	/**
	 * Creates a ticket and makes it the currently active one
	 * @param ticketOwner
	 */
	public void createTicket(String ticketOwner){
		LotteryTicket ticket = new LotteryTicket(ticketOwner);
		tickets.add(ticket);
		currentTicket = ticket;
	}
	
	/*
	 * Creates a row from the provided numbers and adds it to the current ticket
	 */
	public void createRow(ArrayList<Integer> rowNumbers){
		String ownerName = currentTicket.getName();
		TicketRow row = new TicketRow(rowNumbers, ownerName);
		currentTicket.addRow(row);
	}
	
	/**
	 * Creates and adds a random lottery row to the current ticket
	 */
	public void createRandomRow(){
		ArrayList<Integer> newRow = new ArrayList<>();
		
		Random randGen = new Random();
		
		for(int i = 0; i < 5; i++){
			newRow.add(randGen.nextInt(HIGHEST_ALLOWED_ROW_NUMBER) + 1);
		}
		String ownerName = currentTicket.getName();
		currentTicket.addRow(new TicketRow(newRow, ownerName));
	}
	
	/**
	 * Generates the winning numbers for this "round" of the lottery.
	 */
	public void generateWinningRow(){
		// make sure no old values are there
		winningRow.clear();
		
		Random randGen = new Random();
		for(int i = 0; i < 5; i++){
			winningRow.add(randGen.nextInt(HIGHEST_ALLOWED_ROW_NUMBER) + 1);
		}
	}
	
	public ArrayList<LotteryTicket> getTickets(){
		return tickets;
	}
	
	/**
	 * Get a formatted list of the existing tickets - NOTE:  NOT winning results
	 * @return
	 */
	public String getTicketSummary(){
		
		if(tickets.isEmpty()){
			return "No tickets currently in system";
		}
		
		StringBuilder sb = new StringBuilder("");
		
		for(LotteryTicket ticket : tickets){
			sb.append(ticket.getName() + " has a ticket with " + ticket.getRows().size() + " rows\n\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * Get ticket by owner name - NOTE : may return null 
	 * @param ownerName
	 * @return the matching ticket, or null of no such ticket exists
	 */
	public LotteryTicket getTicketByOwnerName(String ownerName){
		for(LotteryTicket ticket : tickets){
			if(ticket.getName().equalsIgnoreCase(ownerName)){
				return ticket;
			}
		}
		return null;
	}
	
	private ArrayList<TicketRow> getAllTicketRows(){
		ArrayList<TicketRow> allTicketRows = new ArrayList<TicketRow>();
		// get the rows from all of the tickets and put it into one list
		for(LotteryTicket ticket : tickets){
			allTicketRows.addAll(ticket.getRows());
		}
		return allTicketRows;
	}
	
//  // Below is my own naive sort
	
//	/**
//	 * Returns a sorted array of either all ticket rows, or just the rows of the current ticket
//	 * @param forAllTickets if true: all ticket rows, if false: current ticket rows
//	 * @return sorted list, descending order 
//	 */
//	private ArrayList<TicketRow> sortTicketRowsByPoints(boolean forAllTickets){
//		ArrayList<TicketRow> ticketRows;
//		if(forAllTickets){
//			ticketRows = getAllTicketRows();
//		} else {
//			ticketRows = currentTicket.getRows();
//		}
//		
//		// for each of the possible points 0 to maxPoints, move the rows matching that point to the first position
//		// first all 0 points, then they get pushed down by 1 points.. and so on
//		// until the 5 points are at the top with the others following
//		int maxPoints = 5;
//		
//		// loop through possible points
//		for(int points = 0; points <= maxPoints; points++){
//			// loop through all the rows in the list
//			for(int i = 0; i < ticketRows.size(); i++){
//				TicketRow row = ticketRows.get(i);
//				if(row.countCorrectNumbers(winningRow) == points){
//					// remove from list, then add at the top index
//					ticketRows.remove(row);
//					ticketRows.add(0, row);
//				}
//			}
//		}
//		return ticketRows;
//	}
	
	/**
	 * Returns a sorted array of either all ticket rows, or just the rows of the current ticket
	 * @param forAllTickets if true: all ticket rows, if false: current ticket rows
	 * @return sorted list, descending order 
	 */
	private ArrayList<TicketRow> sortTicketRowsByPoints(boolean forAllTickets){
		ArrayList<TicketRow> ticketRows;
		if(forAllTickets){
			ticketRows = getAllTicketRows();
		} else {
			ticketRows = currentTicket.getRows();
		}
		
		
		// before we can sort we must call the count points method, else all point values will be 0
		for(TicketRow row : ticketRows){
			row.countCorrectNumbers(winningRow);
		}
		
		// sort it using the implemented Comparable interface
		
		Collections.sort(ticketRows);
		
		return ticketRows;

	}
	
	/**
	 * For alignment of Strings where some words may be of different length
	 * <p>
	 * Example problem:	Sven : 2 points;
	 * 					Gurkmacka : 2 points  <--- not aligned vertically
	 * Wanted result:	Sven		: 2 points
	 * 					Gurkmacka	: 2 points
	 * </p>
	 * @param wordWithVariableLenght
	 * @return
	 */
	private String addSpacingToString(String wordWithVariableLenght){
		
		StringBuilder sb = new StringBuilder(wordWithVariableLenght);
		int wordLength = wordWithVariableLenght.length();
		int spaceBuffer = getLengthOfLongestName() + 1; 
		// for example: if a string is 5 length, then to that string, we will add 20-5 spaces
		for(int i = 0; i < spaceBuffer - wordLength; i++){
			sb.append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * Triggers all needed for calculating results and returns it as a String
	 * @param forAllTickets
	 * @return
	 */
	public String getFormattedResults(boolean forAllTickets){
		ArrayList<TicketRow> sortedTicketRows = sortTicketRowsByPoints(forAllTickets);
		StringBuilder sb = new StringBuilder("The lottery results for winning row " + winningRow + " are:\n");
		
		//TODO: fix spacing when user names are not equal length
		String ownerName;
		int points;
		String winner = "";
		
		for(TicketRow row : sortedTicketRows){
			ownerName = row.getOwnerName();
			ownerName = addSpacingToString(ownerName);
			points = row.getPoints();
			winner = row.isWinner() ? "WINNER" : "no win";
			sb.append(ownerName + " - " + winner + " : "+ points + " points for row " + row.getRowNumbers());
			sb.append("\n");
		}
		
		String formattedResults = sb.toString();
		// String may be too long to show in console. Save it to file too.
		try {
			FileWriter writer = new FileWriter("lotteryResultList.txt");
			BufferedWriter bWriter = new BufferedWriter(writer);
			bWriter.write(formattedResults);
			bWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // save in project folder
		
		return formattedResults;
	}
	
	private int getLengthOfLongestName(){
		int length = 0;
		for(LotteryTicket ticket : tickets){
			int nameLenght = ticket.getName().length();
			if(nameLenght > length){
				length = nameLenght;
			}
		}
		return length;
	}
	
	
}
