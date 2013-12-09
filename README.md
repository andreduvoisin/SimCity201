TEAM 28: DOBBY
=======
#SimCity201

###Team Work
The CSCI 201 Project teaches students about the myriad facets of team work: pair programming, team ethics, etc. Team 28 has embraced this team 
mentality and has approached the project accordingly. Although the following section lists _individual_ contributions, the project is the result of team contributions. Every section is the product of a team effort.

####Andre Duvoisin	(Fix-It Felix)
* Bank GUI
* BaseAgent Design
* Base GUI Integration
* ControlPanel Layout
* Citywide GUI Integration
* CityCard Zoom-In View Functionality
* Restaurant Integration Manager
* Config File Manager for Restaurants

####Angelica Tran	(Market Manager)
* City Animation Upgrades
* Market Design, Roles, Testing
* Market/Restaurant Integration Manager
  
####Chase Wagoner	(Bus Driver)
* Transportation Design, Roles, GUI
* Restaurant Integration

####David Carr		(The Internet)
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

####Shane Mileham	(The Designer)
* Contact List
* Person Agent Design/Implementation
* B* Algorithm and City GUI Coordinates
* Configuration File Design and Implementation
* Market Design
* Party Creation
* Transportation Pickup/Dropoff Design
* Restaurant Integration


###Run System Run
Various scenarios can be run via the configuration panel. In v1, to run scenarios successively, the program must be restarted.
* Restaurant 0	- Andre Duvoisin 
* Restaurant 1	- Chase Wagoner	 
* Restaurant 2 	- Jerry Webb	 
* Restaurant 3	- Maggi Yan	 	 
* Restaurant 4	- David Carr	 
* Restaurant 5  - Shane Mileham	 
* Restaurant 6	- Angelica Tran	 
* Restaurant 7	- Rex Xu		 
* Bank		- Sends customers to the bank for transactions 		-> Bank located at north end of Plaza
* Housing	- Sends people to houses to perform maintenance		-> Housing located all along sides of panel 
* Food Market	- Sends people to market to purchase food items		-> Market located in southwest quadrant
* Party		- Creates a party crazed animal (24 hour full run)	-> All people head to the host's house to party
* Simulate All  - Creates many people with interweaving actions		-> People perform varied actions

###Full Disclosure
Unfortunately, there are a few areas of the city that we were not able to complete to our satisfaction.

####General
For our v1, most agent actions are pre-determined based on simulation choices.
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