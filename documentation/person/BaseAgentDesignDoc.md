# BaseAgent Design Document
## PersonAgent extends Agent implements Person
### Data
```
List<Role> roles; //i.e. WaiterRole, BankTellerRole, etc.
enum PersonState {None, IsHungry, DecidedEatAtHome, DecidedEatAtRestaurant, Eating}
PersonState state = None;
double money;
Restaurant restaurantChoice;
Home home;
```
### Scheduler
```
// Need to properly order the rules - something like
// person's emergencies, role scheduler, rest of person rules.

if state = IsHungry
	DecideWhatToEat();
if state = DecidedEatAtHome
	EatAtHome();
if state = DecidedEatAtRestaurant
	EatAtRestaurant(restaurantChoice);

Boolean anytrue = F;
for each r in roles such that r.active = T
	anytrue = r.pickAndExecuteAnAction();
return anytrue;
```
### Messages
```
// From GUI?
msgGotHungry() {
	state = PersonState.IsHungry;
}
```
### Actions
```
DecideWhatToEat() {
	// What will be our algorithm to figure out which to do?
	switch(random(2)) {
		case 0:
			state = PersonState.DecidedEatAtHome;
			break;
		case 1:
			state = PersonState.DecidedEatAtRestaurant;
			// What will be our algorithm to figure out which restaurant to go to?
			restaurantChoice = random(8);
			break;
	}
}
EatAtHome() {
	DoGoHome(home);
	roles.find(HouseRenterRole).active = T;
	state = PersonState.Eating;
}
EatAtRestaurant(Restaurant r) {
	DoGoToRestaurant(r);
	r.getHost().msgImHungry(roles.find(CustomerRole));
	roles.find(CustomerRole).active = T;
	state = PersonState.Eating;
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