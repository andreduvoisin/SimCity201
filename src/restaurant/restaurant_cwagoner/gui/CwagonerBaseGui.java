package restaurant.restaurant_cwagoner.gui;

import base.interfaces.Role;

public class CwagonerBaseGui {

	CwagonerAnimationPanel animationPanel;
	Role role;

	public CwagonerBaseGui(Role r, CwagonerAnimationPanel p) {
		role = r;
		animationPanel = p;
	}
}
