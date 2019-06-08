# SimCity201

## TEAM 28: DOBBY

### Team Work

The CSCI 201 Project teaches students about the myriad facets of team work: pair programming, team ethics, etc. Team 28 has embraced this team mentality and has approached the project accordingly. Although the following section lists _individual_ contributions, the project is the result of team contributions. Every section is the product of a team effort.

#### Andre Duvoisin (Fix-It Felix)

- Bank GUI
- Bank Integration
- PersonAgent Design
- Base GUI Integration
- Citywide GUI Integration
- ControlPanel Layout and Integration
- CityCard Zoom-In View Functionality
- Trace Panel Integration
- Restaurant Integration Manager
- Helped with Inspector
- Weekend Behavior
  - Disabling/Enabling Workplaces
- Role Switching (Events)
- Config File Manager
- Transportation Integration
- Bug Squasher (hence the nickname!)

#### Angelica Tran (Market Manager)

- City Animation Upgrades
- Market Design, Roles, GUI
- Market Unit Testing: Cook Customer, Cashier, GUI Test, Delivery Truck, Worker
- Market/Restaurant Integration Manager
- Building Properties Panel

#### Chase Wagoner (Bus Driver)

- Transportation Design, Roles, GUI
- Restaurant Integration
- Fixed my restaurant
- Some help on re-design and implementation of bus
- Tested transportation
- Added panel to add people via GUI
- Updated design docs for transportation, housing, and restaurants

#### David Carr (The Internet)

- Housing Roles, Design, Testing
- ConfigParser/SortingHat
- Control Panel/Configuration Reading
- Restaurant Base Interface
- Restaurant Intermediate Roles
- PersonAgent Scheduler, Jobs, Events
- Restaurant Integration/Unit Testing
- Transportation
  - Navigation algorithm
  - Car/Person animation
  - Collision detection
  - Intersections

#### Jerry Webb (Romantic Interest)

- GUI Infrastructure
- Restaurant Integration
- J-Unit Testing
- Design Docs

#### Maggi Yang (Literally Dornsife)

- Housing Roles
- Housing Testing
- City Animation Upgrades
- Restaurant Integration
- Transportation: Commuter Role, car, bus, walking
- Intersections
- Blocks for B\* algorithm for vehicles

#### Rex Xu (Money Man)

- Bank Design, Roles, Testing
- City Party Planner
- City Financial Coordinator
- Project Coordination/Management
- Person Agent Design/Implementation
- Restaurant Integration
- Property Panel
- Inspection Events
- Party Implementation
- Config File Setup

#### Shane Mileham (The Designer)

- Contact List
- Person Agent Design/Implementation
- B\* Algorithm and City GUI Coordinates
- Configuration File Design and Implementation
- Market Design
- Party Creation
- Transportation Pickup/Dropoff Design
- Restaurant Integration

### Run System Run

Various scenarios can be run via the configuration panel. To run scenarios successfully, program must be restarted.

#### Scenarios Tab

Scenarios can be run in this tab. Most can be immediately run, but a few require basic set up.

D: The party is held in the pink house

F: Selectively disable workplaces in Properties tab

O: Bank Robbery occurs in Piggy Bank, not in Gringotts

S: Creates a city with extra workers. Fire these workers in the People tab

#### Properties Tab

- Selectively open/close workplaces
- View workplace summary information
- Manually set inventories for workplaces

#### Trace Tab

Filter print statements by type or workplace

- Info
- Errors
- Debugs
- Warnings
- Messages

#### People Tab

- Manually add people
- Fire people from workplaces

#### View Toggle

The button at the bottom of control panel toggles between grading and beautiful view.

### Full Disclosure

Unfortunately, there are a few areas of the city that we were not able to complete to our satisfaction.

#### Restaurants

No A\* implemented in restaurants. However, collisions have been mitigated by clever interior design.

#### Money

Our SimCity does not acknowledge changes in day. Accordingly, money has diminished value. E.g. daily working capital does not matter.

### V1 README

[V1 README](https://github.com/andreduvoisin/SimCity201/wiki/V1-README)
