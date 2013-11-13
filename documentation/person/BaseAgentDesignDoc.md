# BaseAgent Design Document
## PersonAgent extends Agent implements Person
### Data
```
List<Role> roles; //i.e. WaiterRole, BankTellerRole, etc.

enum PersonAction {TimeToWork, IsHungry, BuyHouse, BuyCar}
Set<PersonAction> actions;

enum PersonState {Eating, Working, Shopping};
PersonState state;

// Assigned in Constructor when PersonAgent is initialized.
static int SSN;
int mySSN;
static int timeSchedule;
int myTimeSchedule;

double money;
int stateOfWealth; //based on money
double credit; //credit card
int creditScore;

int mealsToEat;
int age;

boolean hasHome;
boolean hasLoan;
boolean hasCar;

List<Restaurant> restaurants;
Restaurant restaurantChoice;

Home home;
Work work;
Market market;
Role job;

// All information about the city.
```
### Scheduler
```
// WORK
if actions.contains(PersonAction.TimeToWork) {
	actions.remove(PersonAction.TimeToWork);
	GoToWork();
}

// ROLE
Boolean anytrue = F;
for each r in roles such that r.active = T
	anytrue = r.pickAndExecuteAnAction();
return anytrue;

// PERSONAL
if actions.contains(PersonAction.IsHungry) {
	actions.remove(PersonAction.IsHungry);
	EatFood();
}
if actions.contains(PersonAction.BuyHouse) {
	actions.remove(PersonAction.BuyHouse);
	BuyHouse();
}
if actions.contains(PersonAction.BuyCar) {
	actions.remove(PersonAction.BuyCar);
	BuyCar();
}
```
### Messages
```
// From GUI or timer.
msgGotHungry() {
	actions.add(PersonAction.IsHungry);
}

// From GUI or timer.
msgTimeToWork() {
	actions.add(PersonAction.TimeToWork);
}

// From GUI or timer.
msgBuyHouse() {
	actions.add(PersonAction.BuyHouse);
}

// From GUI or timer.
msgBuyCar() {
	actions.add(PersonAction.BuyCar);
}
```
### Actions
```
GoToWork() {
	DoGoTo(work.location);
	work.getHost().msgImHere(job);
	job.active = T;
	state = PersonState.Working;
}

EatFood() {
	// What will be our algorithm to figure out which to do?
	switch(random(2)) {
		case 0:
			// Eat at home.
			DoGoTo(home.location);
			roles.find(HouseRenterRole).active = T;
			DoGoMakeFoodAtHome();
			state = PersonState.Eating;
			break;
		case 1:
			// Eat at restaurant.
			// What will be our algorithm to figure out which restaurant to go to?
			restaurantChoice = restaurants.chooseRestaurant();
			DoGoTo(restaurantChoice.location);
			restaurantChoice.getHost().msgImHungry(roles.find(CustomerRole));
			roles.find(CustomerRole).active = T;
			state = PersonState.Eating;
			break;
	}
}

BuyHouse() {
	DoGoTo(market.location);
	market.getHost().msgImHere(roles.find(MarketCustomerRole));
	roles.find(MarketCustomerRole).active = T;
	state = PersonState.Shopping;
}

BuyCar() {
	DoGoTo(market.location);
	market.getHose().msgImHere(roles.find(MarketCustomerRole));
	roles.find(MarketCustomerRole).active = T;
	state = PersonState.Shopping;
}
```
### Utilities
```
public void addRole(Role r) {
	roles.add(r);
	r.setPerson(this);
}
public void removeRole(Role r) {
	roles.remove(r);
}
```