

public class Main {

	public static void main(String[] args){
		
		// allowed numbers in row will be from 1 up to highestAllowedNumber
		int highestAllowedNumber = 20;
		Lottery lottery = new Lottery("SuperLotto", highestAllowedNumber);
		lottery.startLottery();
	}
}
