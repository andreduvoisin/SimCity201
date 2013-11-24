SimCity201-GUI
==============

## Creating a GUI for SimCity201
##### Tobias Lee
This is just how I create my GUIs. You don't have to do it the same way as I do.

### Step 1: Figure out the necessary components
In this project, three panels are required:
  1. City View Panel (The city as a whole)
  2. Interior View Panel (Inside an individual building, like a restaurant)
  3. Control Panel (To add new people, buildings, anything)

Additionally, I've determined that the following panel(s) would be nice to have:
  + Info Panel (for the selected building)

If you ever feel the need to add additional panels, go back to this step and think about how this new panel interacts with and changes the requirements of other panels

### Step 2: Determine the layout of the components
When I was designing this, I determined that the City View Panel was the most important aspect, with the Interior View Panel being just slightly less important. As long as the Control Panel was functional, it didn't really matter where it was placed or how large it was. I figured If I needed more space for the ControlPanel I'd just convert it to a JTabbedPane anyway. I shoved the Info Panel next to the Interior View Panel because the two are related.<br>
Now is the time to pull out pencil and paper and start drawing out sketches of how you want the GUI to look. I decided to make it look how it does because I can. The important parts to consider at this point are:
  + What are the most important panels
  + What size does everything need to be
  + What is the maximum resolution allowed (Preferably 1366x768 to fit most laptop screens)

Again, feel free to assign different importances to different components and place your panels wherever you want. I don't claim to be good at graphical design; just at implementing a design.

### Step 3: Implementing this overall layout
Now is the time to code.<br>
For this step, we must create a JFrame to hold all the panels.<br>
Create a new class (I called mine SimCityGui.java) and extend JFrame.<br>
Also in this class, create a main function to implement this JFrame and make it exit on close, not resizable, and visible.<br>
Create however many JPanels you need to implement your design (in this case, 4). Instantiate these JPanels to have the preferred sizes that you decided and give them random background colors to differentiate them properly.<br>
  + What layout will you need to implement your design? BoxLayout (Probably not)? BorderLayout? GridLayout (Trust me, it's not this)? Embedded Layouts?

I personally use **GridBagLayout**. It's like GridLayout, only it doesn't automatically resize all your components and gives you a lot more control over how your components are placed. You don't have to use GridBagLayout if you don't want to, but if you do your project will probably look better and Professor Crowley will be happier.

#### Step 3.5: Using GridBagLayout
The first thing you need to use GridBagLayout is to set the layout to a GridBagLayout. The second thing is a GridBagConstraints object.

In your constructor, add the following line:
```java
setLayout(new GridBagLayout());
```
and create a new class variable c:
```java
GridBagConstraints c = new GridBagConstraints();
```

Now cut your design into a grid. In my design, I needed a height of 3 and a width of 2. You might be saying, "But wait, Toby! The height and width of your grid squares are not all uniform!" and you're absolutely right.<br>
Remember when I said GridBagLayout is like GridLayout only minus the ugly part? Yeah, this is where that comes in.<br>
With GridBagLayout, you're free to adjust the number of grid spaces each component takes up. Additionally, each grid row and column resize to match the maximum height and width of the components inside. So in this case, Row 1 has height 100, Row 2 has height 500, and Row 3 has height 100. Column 1 has width 600 and Column 2 has width 500. The City View Panel has a grid height of 2 spaces and the Control Panel has a width of 2 spaces.<br>
So you just do that.
```java
	c.gridx = 0; c.gridy = 0;
	c.gridwidth = 1; c.gridheight = 2;
	this.add(city, c);
	
	c.gridx = 1; c.gridy = 0;
	c.gridwidth = 1; c.gridheight = 1;
	this.add(info, c);
	
	c.gridx = 1; c.gridy = 1;
	c.gridwidth = 1; c.gridheight = 1;
	this.add(interior, c);
	
	c.gridx = 0; c.gridy = 2;
	c.gridwidth = 11; c.gridheight = 1;
	this.add(CP, c);
```
You don't have to change the GridBagConstraints components each time if they don't change. I just include it anyway because I'm forgetful or if I add a different component in between to components.<br>
Observant students will notice that the add() function has a new parameter in it. We include c in this function to let Java hknow to apply these constraints when adding a JComponent to our JFrame.<br>
Now we run this program as a Java Application. If it looks like we wanted to, great. If not, play around with the GridBagConstraints until it does. Now is the time to play around with your layout design to get to it how you want it to look.

### Step 4: Creating each individual Panel
After you've gotten a layout you're satisfied with, it's time to create each individual panel. I decided to do the graphics panels first, namely the City View Panel and the Interior View Panel. For these, I looked to our Restaurant's AnimationPanel for inspiration. I decided to copy the idea of a list of gui components that get painted and moved.<br>
So of course the first step is to create a gui component base class.

#### Step 4.1: CityComponent.java
Now let's think about what should be present in these components. I came up with a few at least:
  + x-y coordinates
  + paint functionality
  + movement functionality
  + String ID
  
You'll probably come up with more when you actually implement it.

#### Step 4.2: CityRestaurant.java
From this base class we can extend any number of components. We'll need to make cars, people, restaurants, banks, markets, buses, and perhaps others. I decided to do the restaurant first because that's what we're most familiar with, but I could have just as easily made anything else.<br>
There's very little to do here. All that is necessary to do is make some constructors and overload the paint and moving functionalities. It's particularly easy because the restaurant doesn't move.

#### Step 4.3: Back to the main panels
Now that we have these components the creation of these panels is very easy. All we have to do is create a list of components and paint/move them, and create a timer to allow animation to happen. We'll also create a pointer to the outer JFrame to link all the panels together and pass that in the constructor.

#### Step 4.4 Using CardLayout for multiple views
These past few steps might have been a bit boring for some of you because you've done a bit of it for your restaurants, but this next part should probably be new for most of you.<br>
Our goal is to make it so that when you click on a restaurant or a bank, the Interior View Panel will change to reflect the interior of that building. To do this, we need to be able to do two things:
 1. Add functionality to clicks
 2. Change the view of the Interior View Panel in response to this click.
 
Thing 1 can be solved with MouseListener; Thing 2 can be solved with CardLayout.

#### Step 4.5: Adding CardLayout
You might be asking why I'm starting with Thing 2 rather than Thing 1. The reason is simply because you have nothing to do when you click on something if you don't have this CardLayout implemented yet.<br>
Now if you were in Crowley's class, you might have heard him mention CardLayout at some point as being a very easy way to switch views. Of course, he's correct.<br>
To use CardLayout, you need a list of panels to display and a list of String keys that map to these panels. I decided to use a HashMap to store these panels, because that's what made sense to me given the properties of a HashMap and CardLayout.<br>
After you populate this HashMap, you just add the panels to the CardLayout:
```java
	cards = new HashMap<String, CityCard>();
		cards.put("null", new CityCard(city, Color.DARK_GRAY));
		cards.put("Road", new CityCard(city));
		cards.put("Restaurant 1", new CityCard(city, Color.blue));
		cards.put("Restaurant 2", new CityCard(city, Color.red));

		layout = new CardLayout();
		this.setLayout(layout);
		for (String key:cards.keySet()) {
			this.add(cards.get(key), key);
		}
		

		layout.show(this, "null");
```
If you're wondering about why we need ```layout```, it's because that's how CardLayout works to change what panel is being displayed. CityCard is how you implemented your Interior View Panel. It'll be something akin to AnimationPanel from your Restaurant project, but right now my panels are just solid colors.

Adding another panel is pretty easy:
```java
	public boolean addView(CityCard panel, String key) {
		if (cards.containsKey(key))
			return false;
		cards.put(key, panel);
		this.add(cards.get(key), key);
		return true;
	}
```
You just add the panel and its key to the CardLayout.

Changing the view is also pretty easy:
```java
	public void setView(String key) {
		if (cards.containsKey(key)) {
			layout.show(this, key);
		}
	}
```
And that's essentially all you need to implement CardLayout for your project.

#### Step 4.6: Adding MouseListener
Now you don't have to do things this way. I personally come from an awt background when I made Java games in high school, which is why I chose to do it this way, but in theory using JButtons would work the same way.<br>
To add MouseListener, simply implement MouseListener in the City View Panel and in the constructor, call ```addMouseListener(this);```<br>
Eclipse will ask you to add the unimplemented methods, so do that. You'll see a lot of new methods getting added, and you can look them all up later if you're interested, but the one that I'll be using is mousePressed.<br>
What I want to do is when I click my mouse, I want to check if there is a component in my city that contains the point that my mouse clicked. Given that you can get the mouse point by arg0.getPoint() or arg0.getX() and arg0.getY(), can you think of a way to solve this problem?<br>
Of course the easy way is to create a contains function in our component class that returns true if the component contains the point passed as a parameter. You should be able to do this on your own. I'll put it here anyways:
```java
	for (CityComponent c: statics) {
			if (c.contains(arg0.getX(), arg0.getY())) {
				city.view.setView(c.ID);
			}
		}
```
Now when we click on an object we want to call our setView for our Interior View Panel. We can simply call the Interior View Panel from the encasing JFrame and then call setView with the component's ID passed as the parameter. If the Interior View Panel finds a corresponding panel, it'll display that. Now would be a great time to start replacing your filler JPanels in the JFrame with your actual City Panels, otherwise you won't see anything happening.

#### Step 4.7: Information Panel
The Information Panel is really not too interesting. Mine is literally a huge JLabel with a function to change the text of that JLabel. You can make this as intricate or as non-existent as you desire, but you shouldn't need help with creating a JPanel with a JLabel. The only potentially new thing would be changing the text of this JLabel:
```java
	public void setText(String s) {
		text.setText(s);
	}
```
But again, not too difficult.

Now all we need to do is change the text of the info panel when we change the view of the Interior View Panel:
```java
	public void setView(String key) {
		if (cards.containsKey(key)) {
			layout.show(this, key);
			city.info.setText(key);
		}
	}
```

#### Step 4.8: Swing controls
You can implement whatever you'll need for your city here. I just put in the ability to create new Restaurants and Banks, but you'll definitely need more than that. Also, try to make it look nice using your newfound knowledge of GridBagLayout. But at this point, you've essentially completed a skeleton of your gui and the next step after fleshing it out is to integrate it with your agents.
