

public class Main {

	public static void main(String[] args){
		
		// allowed numbers in row will be from 1 up to highestAllowedNumber
		int highestAllowedNumber = 20;
		// what is a row? how many numbers? 
		int numbersPerRow = 3;
		Lottery lottery = new Lottery("SuperLotto", highestAllowedNumber, numbersPerRow);
		lottery.startLottery();
	}
}
