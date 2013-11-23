package bank;

public class BankTransaction {
		public int sender;
		public int receiver;
		public double amount;
		public BankTransaction(int s, int r, double a){
			sender = s;
			receiver = r;
			amount = a;
		}
}
