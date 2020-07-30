package ProjectileNMK;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class JframeTennis extends JFrame {
	JLabel emptyLabel = new JLabel("");

	public static void main(String[] args) {
		new JframeTennis().setVisible(true);
	}

	private JframeTennis() {
		// sets title
		super("Tennis Simulation");
		// sets size
		setSize(600, 600);
		// allows it to close
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		// creates a button
		// JButton velocity = new JButton("Velocity");
		// adds the button
		// add(velocity);
		//action listener
		//when action is taken, do the stuff
		JTextField field = new JTextField(10);
		add(field, BorderLayout.SOUTH);
//	    ComponentListener listener = new ComponentAdapter() {
//	        public void componentShown(ComponentEvent evt) {
//	          Component c = (Component) evt.getSource();
//	          System.out.println("Component is now visible");
//	        
//	          ;
	        
	   

	}

	public void actionPerformed(ActionEvent e) {

	}

}
