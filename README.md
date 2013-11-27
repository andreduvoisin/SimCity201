TEAM 28: DOBBY
=======
#SimCity201

###Team Work
The CSCI 201 Project teaches students about the myriad facets of team work: pair programming, team ethics, etc. Team 28 has embraced this team 
mentality and has approached the project accordingly. Although the following section lists _individual_ contributions, the project is the result of team contributions. Every section is the sum of the work of multiple members.

####Andre Duvoisin	(GUI Specialist)
* Bank GUI
* BaseAgent Design
* Base GUI Integration
* ControlPanel Layout
* Citywide GUI Integration
* Zoom-In View Functionality
* Restaurant Integration Manager

####Angelica Tran	(Market Manager)
* City Animation Upgrades
* Market Design, Roles, Testing
* Market/Restaurant Integration Manager
  
####Chase Wagoner	(Bus Driver)
* Transportation Design, Roles, GUI
* Restaurant Integration

####David Carr		(The Integrator)
* Housing Roles, Design
* ConfigParser/SortingHat
* Control Panel/Configuration Reading
* Restaurant Base Interface
* Restaurant Intermediate Roles
* PersonAgent Scheduler, Jobs, Events
* Restaurant Integration

####Jerry Webb		(Romantic Interest)
* GUI Infrastructure
* Restaurant Integration

####Maggi Yang		(Literally Dornsife)
* Housing Roles
* Housing Testing
* City Animation Upgrades 
* Restaurant Integration

####Rex Xu			(Money Man)
* Bank Design, Roles, Testing
* City Party Planner
* City Financial Coordinator
* Project Coordination/Management
* Person Agent Design/Implementation
* Restaurant Integration

####Shane Mileham	(Portland Power)
* Contact List
* Market Design
* Party Creation
* Person Agent Design/Implementation
* B* Algorithm and City GUI Coordinates
* Configuration File Design and Implementation
* Transportation Rider Pickup/Dropoff Design
* Restaurant Integration


###Run System Run
Various scenarios can be run via the configuration panel.
* Restaurant 0	- Andre Duvoisin 
* Restaurant 1	- Chase Wagoner	 
* Restaurant 2 	- Jerry Webb	 
* Restaurant 3	- Maggi Yan	 	 
* Restaurant 4	- David Carr	 
* Restaurant 5  - Shane Mileham	 
* Restaurant 6	- Angelica Tran	 
* Restaurant 7	- Rex Xu		 
* Bank			- Sends customers to the bank for transactions
* Housing		- Sends people to houses to perform maintenance
* Food Market	- Sends people to market to purchase food items
* Party			- Creates a party crazed animal (24 hour full run)
* Master Config - Creates many people with interweaving actions

###Full Disclosure
Unfortunately, there are a few areas of the city that we were not able to complete to our satisfaction.

####General
Our agents are not fully autonomous. For our V1, most actions are pre-determined upon instantiation.
####Restaurants
Occasional delays occur in some restaurants. Race conditions sometimes cause debilitating failure in our restaurants.
####Transportation
We modelled our transportation system off that of our own city, Los Angeles. Just like our fellow Angelinos, the denizens of SimCity also avoid taking public transportation at all costs. In fact, they do not ever take the bus and choose instead to walk.

###Questions & Discussion
Please use the [issue tracker](https://github.com/usc-csci201-fall2013/simcity201/issues) for this repository to ask and discuss topics related to the team project.

###Resources:
  *  [Overview & Deliverables](http://www-scf.usc.edu/~csci201/team/)
  *  [Operational Concepts Description](http://www-scf.usc.edu/~csci201/team/operational-concepts-description.html)
  *  [Requirements](http://www-scf.usc.edu/~csci201/team/simcity201.html)
  

###SimCity Demo
A basic demo developed by Professor Crowley is available in the [`demo`](https://github.com/usc-csci201-fall2013/simcity201/tree/master/demo) directory of this repository.