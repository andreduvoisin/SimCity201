package bank;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class BankGui extends JFrame implements ActionListener{
	private static BankGui instance = null;
	JFrame BankFrame = new JFrame("SimCity Animation");
	private BankPanel bankPanel = new BankPanel(this);

	static final int hSpacing = 30;
	static final int vSpacing = 0;
	static final int xIndexing = 50;
	static final int yIndexing = 50;
	
	static final int WINDOWX = 700;
	static final int WINDOWY = 600;
	static final int BankX = 700;
	static final int BankY = 600;
	static final int CONTROLX = 300;
	static final int CONTROLY = 600;
	
	public BankGui() {
		BankFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BankFrame.setBounds(xIndexing, yIndexing , WINDOWX, WINDOWY);
        BankFrame.setVisible(true);
        
        Dimension bankDim = new Dimension(BankX, BankY);
        bankPanel.setPreferredSize(bankDim);
        
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
