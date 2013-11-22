##SimCity201 Demo

###Description
`SimCity201.java` is the "application" class that extends `JFrame`. It contains two panels: a `CityPanel` and a `JPanel` (called `buildingPanels`). The `buildingPanels` panel has a `CardLayout` associated with it. This allows us to "store" as many panels as we want, in the same space. Only the "top" panel will ever be available.

The way the code works is that each Building object extends `Rectangle2D.Double` - making a `Building` a rectangle that can be displayed. This class has a nice function called `contains()`. You give contains an `x`, and a `y`, coordinate, and the method will return `true`, if the coordinate is within the object. Each `Building` object also has a reference to its associated `BuildingPanel`. All the `BuildingPanel` objects are added to the `buildingPanels` `JPanel`. To get a specific `BuildingPanel` displayed, we just have to call the `show()` method on the `CardLayout`.

###How it Works
The `CityPanel` class has a `MouseListener`. Any mouse click in the `CityPanel` causes the `mouseClicked()` method to be called. In this method, we iterate over the collection of `Buildings` to find out which one was clicked on - through the contains method.

For the `Building` that was clicked on, we call the `displayBuilding()` method. This method, in the `Building` class, calls the `displayBuildingPanel()` method in the `BuildingPanel` class. The `displayBuildingPanel()` method in the `BuildingPanel` class calls a method by the same name in the `CityPanel` class. Finally, in that method, we call the `show()` method to have the proper `BuildingPanel` displayed in the bottom half of the frame.

###How to Compile & Run
```bash
cd demo
javac SimCity201.java
java SimCity201
```
