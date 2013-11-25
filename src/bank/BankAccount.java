package bank;

import base.interfaces.Person;

public class BankAccount {
		public double loan = 0;
		public double balance = 0;
		public Person person;
		public BankAccount(double l, double b, Person p) {
			loan = l;
			balance = b;
			person = p;
		}
}
