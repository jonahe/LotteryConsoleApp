import java.util.ArrayList;

public class TicketRow implements Comparable<TicketRow>{

	private ArrayList<Integer> rowNumbers;
	private boolean isWinner;
	private String ownerName; // same as the ticket owner, but needed for easy access
	private int points;

	public TicketRow(ArrayList<Integer> rowNumbers, String ownerName) {
		this.rowNumbers = rowNumbers;
		this.ownerName = ownerName;
		this.isWinner = false;
		this.points = 0;
	}
	
	//TODO: clean up.   make way to check if the row has been checked?
	
	public ArrayList<Integer> getRowNumbers(){
		return rowNumbers;
	}
	
	public String getOwnerName(){
		return ownerName;
	}
	public int getPoints(){
		return points;
	}
	
	public boolean isWinner(){
		return isWinner;
	}
	public boolean isWinningRow(ArrayList<Integer> correctRow){
		return rowNumbers.equals(correctRow);
	}
	
	/**
	 * Given a winning row, this returns the number of correct guesses in this row
	 * @param correctRow
	 * @return
	 */
	public int countCorrectNumbers(ArrayList<Integer> correctRow){
		int count = 0;
		for(int i = 0; i < correctRow.size(); i++){
			// if values at same indexes DON'T match - just continue, else if they do: count++
			if(!rowNumbers.get(i).equals(correctRow.get(i))){
				continue;
			} else {
				count++;
			}
		}
		// this row is a winner
		if(count == 5){
			isWinner = true;
		}
		// save the value for easy access later
		points = count;
		return count;
	}
	
	@Override
	public String toString(){
		
		System.out.println("This lottery row contains: ");
		StringBuilder sb = new StringBuilder();
		for(int number : rowNumbers){
			sb.append(number + "\t");
		}
		sb.append("\t - ").append(ownerName);
		
		return sb.toString();
	}

	@Override
	public int compareTo(TicketRow row) {
		// compares only with respect to the points
		// will return a negative value if this objects points are bigger than the one being compared
		// and vice versa  - look up the interface Comparable
		return row.getPoints() - this.points;
	}

	
	
	
}
