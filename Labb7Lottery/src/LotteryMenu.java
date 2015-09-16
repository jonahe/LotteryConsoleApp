import java.util.ArrayList;
import java.util.Scanner;

public class LotteryMenu {
	
	private Scanner intScanner;
	private Scanner strScanner;
	private String goBackOption;
	private final String QUIT_Str = "99"; // string value to go back in menu or exit
	private final int QUIT_Int = 99; // int value to go back in menu or exit
	private TicketManager ticketManager;
	
	public LotteryMenu(TicketManager ticketManager){
		this.ticketManager = ticketManager;
		this.intScanner = new Scanner(System.in);
		this.strScanner = new Scanner(System.in);
		this.goBackOption = "\n99) Go back one step"; // add this to each option message, to give user opt. to exit
	}

	public void mainMenu(){
		//TODO: only give the option to check winnings rows if there are ticket to check..
		String mainMessage = "\nWhat do you want to do?\n"
				+ "1) Create a new lottery ticket\n"
				+ "2) Choose an existing ticket to work with\n"
				+ "3) Check ALL existing tickets for winning numbers\n"
				+ "4) Generate new winning row (Keeping the any existing tickets)\n"
				+ "99) Leave lottery";
		int choice = QUIT_Int;
		// options
		int createNewTicket = 1;
		int chooseExistingTicket = 2;
		int checkAllForWinning = 3;
		int generateNewWinningRow = 4;
		
		do{
			choice = askForAndGetNextInt(mainMessage, 1, 2, 3, 4, QUIT_Int);
			if(choice == createNewTicket){
				createNewTicketMenu();
			} else if(choice == chooseExistingTicket) {
				chooseTicketToWorkWith();
			} else if(choice == checkAllForWinning){
				if(ticketManager.getTickets().size() == 0){
					System.out.println("There are no tickets to check! Create at least one first!");
				} else {
					boolean forAllTickets = true;
					System.out.println(ticketManager.getFormattedResults(forAllTickets));
					System.out.println("If results are too big for console, check project folder lotteryResults.txt");
				}
			} else if(choice == generateNewWinningRow){
				ticketManager.generateWinningRow();
				System.out.println("New winning row generated!");
			}
			
		} while (choice != QUIT_Int);
	}
	
	private void createNewTicketMenu(){
		String message = "Enter your name";
		String ticketOwner = askForAndGetNextString(message);
		ticketManager.createTicket(ticketOwner);
		System.out.println("Ticket created for owner " + ticketOwner);
		// go on to adding rows
		createNewRowMenu();
		
	}
	
	private void createNewRowMenu(){
		String message = "1) Manually create a new row\n"
						+ "2) Automatically create new row\n"
						+ "3) Automatically create 10 new rows\n"
						+ "4) Automatically create X new rows"
						+ goBackOption;
		// options
		int manCreateNewRow = 1;
		int autCreateNewRow = 2;
		int autCreate10NewRows = 3;
		int autCreateXNewRows = 4;
		
		int choice = QUIT_Int;
		
		do{
			choice = askForAndGetNextInt(message, 1, 2, 3, 4, QUIT_Int);
			if(choice == manCreateNewRow){
				getNumbersForNewRow();
				
			} else if(choice == autCreateNewRow){
				ticketManager.createRandomRow();
				System.out.println("A random row was added! What now?");
			} else if(choice == autCreate10NewRows){
				for(int i = 0; i < 10; i++){
					ticketManager.createRandomRow();
				}
				System.out.println("Ten new rows where added. What now?");
			} else if(choice == autCreateXNewRows){
				String askMessage = "How many rows? Don't make me crash..";
				int rowCount = askForAndGetNextInt(askMessage);
				for(int i = 0; i < rowCount; i++){
					ticketManager.createRandomRow();
				}
				System.out.println(rowCount + " new rows where added. What now?");
			}
				
		} while (choice != QUIT_Int);
	}
	
	private void getNumbersForNewRow(){
		
		// generate valid options
		int highestAllowed = ticketManager.getHIGHEST_ALLOWED_ROW_NUMBER();
		Integer[] validOptions = new Integer[highestAllowed]; // space for num 1 to highestAllowed
		// i = the value 1 to highestAllowed
		// i - 1  =  index
		for(int i = 1; i <= highestAllowed; i++){
			validOptions[i-1] = i;
		}
		String message = "Enter a number between 1 and " + highestAllowed;
		ArrayList<Integer> rowNumbers = new ArrayList<Integer>();
		for(int i = 0; i < 5; i++){
			rowNumbers.add(askForAndGetNextInt(message, validOptions));
		}
		ticketManager.createRow(rowNumbers);
		System.out.println("Row was created:");
		for(int num : rowNumbers){
			System.out.print(num + "\t");
		}
		System.out.println("Now what?");
	}
	
	
	private void chooseTicketToWorkWith(){
		// print a short ticket summary
		System.out.println(ticketManager.getTicketSummary());
		
		String message = "Select one of the tickets by entering the owner name" + goBackOption;
		String input = "";
		// generate an array of all the valid options   =  all the ticket owner names
		ArrayList<LotteryTicket> tickets = ticketManager.getTickets();
		String[] validOptions = new String[tickets.size() + 1];
		// save the last slot for the the quit option
		for(int i = 0; i < validOptions.length -1; i++){
			validOptions[i] = tickets.get(i).getName();
		}
		// last option is the quit option
		validOptions[validOptions.length-1] = QUIT_Str;
		
		do{
			input = askForAndGetNextString(message, validOptions);
			//Find ticket by name
			if(!input.equals(QUIT_Str)){
				LotteryTicket chosenTicket = ticketManager.getTicketByOwnerName(input);
				if(chosenTicket == null){
					System.out.println("Something went wrong when searching for ticket! :(");
				} else {
					ticketManager.setCurrentTicket(chosenTicket);
					ticketOptionsMenu();
					break;
				}
			} else {
				// user entered 99
				break;
			}
			
		} while(true);
		
	}
	
	private void ticketOptionsMenu(){
		String message = 	"What do you want to do?\n"
							+ "1) Add row\n"
							+ "2) Get results for this ticket"
							+ goBackOption;
		// options
		int addRow = 1;
		int getResult = 2;
		
		int choice = QUIT_Int;
		do{
			choice = askForAndGetNextInt(message, 1, 2, QUIT_Int);
			if(choice == addRow){
				createNewRowMenu();
				break;
			} else if(choice == getResult){
				boolean forAllTickets = false; // we only want for the current ticket
				System.out.println(ticketManager.getFormattedResults(forAllTickets));
			}
			
		} while (choice != QUIT_Int);
	}
	
	/**
	 * Ask user for input, and keep asking until s/he chooses one of the valid options
	 * 
	 * @param askMessage
	 * @param validOptions "varargs", meaning "a","b","c" would be ok, as well as 0 args, and String[] as arg
	 * @return one of the valid options, or anything, if no validOptions was provided
	 */
	
	private String askForAndGetNextString(String askMessage, String... validOptions){
		String input = "";
		do{
			System.out.print(askMessage + ": ");
			input = strScanner.nextLine();
			// if it's not one of the valid options, repeat the message - start over
			
			if(validOptions.length == 0){
				// any option will be fine
				break;
			}
			
			if(!contains(validOptions, input)){
				System.out.println("That's not a valid option..");
				continue;
			} else {
				break;
			}
			
		} while (true);
		
		return input;
	}
	
	
	/**
	 * Ask user for input, and keep asking until s/he chooses one of the valid options
	 * 
	 * @param askMessage
	 * @param validOptions "varargs", meaning 3,4,5 would be ok, as well as 0 args, and Integer[] as arg
	 * @return one of the valid options, or anything, if no validOptions was provided
	 */
	private int askForAndGetNextInt(String askMessage, Integer... validOptions){
		int input = 99;
		do{
			System.out.print(askMessage + ": ");
			// check if we really have an int waiting.
			if(intScanner.hasNextInt()){
				input = intScanner.nextInt();
			} else {
				intScanner.nextLine();
				System.out.println("That's not an integer!\n");
				continue;
			}
			// if no options were sent, then any input is OK
			if(validOptions.length == 0){
				break;
			}
			
			// if it's not one of the valid options, repeat the message - start over
			if(!contains(validOptions, input)){
				System.out.println("That's not a valid number..");
				continue;
			} else {
				// go to return 
				break;
			}
			
		} while (true);
		
		return input;
	}
	
	/**
	 * Generic method, mimicking contains() method for Lists, for arrays
	 * @param array
	 * @param needle
	 * @return
	 */
	private <T> boolean contains(T[] array, T needle){
		boolean contains = false;
		for(T value : array){
			if(value.equals(needle)){
				contains = true;
			}
		}
		return contains;
	}
}
