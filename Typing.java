package ProjectileNMK;

import java.awt.event.*;
import javax.swing.*;

public class Typing implements KeyListener {

	public Typing() {
		super();
	}

	public void keyTyped(KeyEvent key) {
		
	}

	public void keyPressed(KeyEvent key) {
		doSomething(key);
	}

	public void keyReleased(KeyEvent key) {

	}

	private void doSomething(KeyEvent key) {

		int id = key.getID();
		String keyString;

		int keyCode = key.getKeyCode();
		keyString = "" + KeyEvent.getKeyText(keyCode);

		if (keyString.equals("Space")) {
			System.out.println("Space");
		}

	}

}