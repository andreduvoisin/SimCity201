package bank;

import base.PersonAgent;

public class Account {
		public double loan = 0;
		public double balance = 0;
		public PersonAgent person;
		public Account(double l, double b, PersonAgent p) {
			loan = l;
			balance = b;
			person = p;
		}
}
