
public class Lottery {

	private String lotteryName;
	private LotteryMenu menu;
	
	Lottery(String lotteryName, int highestAllowedNumber, int numbersPerRow){
		this.lotteryName = lotteryName;
		// This is where the magic happens
		TicketManager ticketManager = new TicketManager(highestAllowedNumber, numbersPerRow);
		this.menu = new LotteryMenu(ticketManager, numbersPerRow);
	}
	
	public void startLottery(){
		System.out.println("Hello and welcome to " + lotteryName + "!");
		// start the menu system. everything is triggered from here
		menu.mainMenu();
		System.out.println("Sorry to see you leave.. See you next time!");
	}
}
