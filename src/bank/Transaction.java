package bank;

public class Transaction {
		public int sender;
		public int receiver;
		public double amount;
		public Transaction(int s, int r, double a){
			sender = s;
			receiver = r;
			amount = a;
		}
}
