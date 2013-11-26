package restaurant.restaurant_davidmca.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import restaurant.restaurant_davidmca.interfaces.Customer;
import base.Gui;

public class CustomerGui implements Gui {

        private Customer agent = null;
        private boolean isPresent = false;
        private boolean isHungry = false;
        private static int CustomerSize = 20;
        private String labelText = "";

        DavidRestaurantGui gui;

        private int xPos, yPos;
        private int xHome, yHome;
        private int xDestination, yDestination;

        private enum Command {
                noCommand, GoToSeat, LeaveRestaurant, WaitingArea
        };

        private Command command = Command.noCommand;

        public CustomerGui(Customer c, DavidRestaurantGui gui, int home) {
                agent = c;
                xPos = -40;
                yPos = -40;
                yHome = 10;
                xHome = 10+30*(home);
                xDestination = -40;
                yDestination = -40;
                this.gui = gui;
        }

        public void updatePosition() {
                if (xPos < xDestination)
                        xPos += 1;
                else if (xPos > xDestination)
                        xPos -= 1;

                if (yPos < yDestination)
                        yPos += 1;
                else if (yPos > yDestination)
                        yPos -= 1;
                if (xPos == xDestination && yPos == yDestination) {
                        if (command == Command.WaitingArea) {
                                agent.msgAnimationFinishedGoToWaitingArea();
                        }
                        if (command == Command.GoToSeat)
                                agent.msgAnimationFinishedGoToSeat();
                        else if (command == Command.LeaveRestaurant) {
                                agent.msgAnimationFinishedLeaveRestaurant();
                                System.out
                                                .println("about to call gui.setCustomerEnabled(agent);");
                                isHungry = false;
                                gui.setCustomerEnabled(agent);
                        }
                        command = Command.noCommand;
                }
        }

        public void draw(Graphics2D g) {
                g.setColor(Color.BLUE);
                g.fillRect(xPos, yPos, CustomerSize, CustomerSize);
                g.setColor(Color.BLACK);
                g.drawString(labelText, xPos, yPos);
        }

        public boolean isPresent() {
                return isPresent;
        }

        public void setLabelText(String text) {
                labelText = text;
        }

        public void setHungry() {
                isHungry = true;
                agent.gotHungry();
                setPresent(true);
        }

        public boolean isHungry() {
                return isHungry;
        }

        public void setPresent(boolean p) {
                isPresent = p;
        }
        
        public void DoGoToWaitingArea() {
                xDestination = xHome;
                yDestination = yHome;
                command = Command.WaitingArea;
        }

        public void DoGoToSeat(int tablex, int tabley) {
                xDestination = tablex;
                yDestination = tabley;
                command = Command.GoToSeat;
        }

        public void DoExitRestaurant() {
                xDestination = -40;
                yDestination = -40;
                command = Command.LeaveRestaurant;
        }

        public int getHomeLocation() {
                return xHome;
        }
}