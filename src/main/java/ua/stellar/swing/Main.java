package ua.stellar.swing;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import ua.stellar.swing.forms.MainForm;

public class Main {
	
	public static void main(String[] args) {
		
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = environment.getScreenDevices();
		
		for (int i=0; i<devices.length; i++) {			 
	        DisplayMode dmode = devices[i].getDisplayMode();

	        System.out.println("Монитор #" + i + ": " + dmode.getWidth() + "x" + dmode.getHeight() );	 
	    }
		
		MainForm mainForm = new MainForm();
		mainForm.setVisible(true);
	}

}
