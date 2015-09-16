import java.util.ArrayList;

public class LotteryTicket {

	private ArrayList<TicketRow> rows;
	private String name; // owner of this ticket

	public LotteryTicket(String name) {
		this.rows = new ArrayList<TicketRow>();
		this.name = name;
	}
	
	public void addRow(TicketRow row){
		rows.add(row);
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<TicketRow> getRows(){
		return rows;
	}
	
	
}
