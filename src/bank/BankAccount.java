package bank;

import base.PersonAgent;

public class BankAccount {
		public double loan = 0;
		public double balance = 0;
		public PersonAgent person;
		public BankAccount(double l, double b, PersonAgent p) {
			loan = l;
			balance = b;
			person = p;
		}
}
