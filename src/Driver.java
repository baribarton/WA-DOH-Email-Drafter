import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Driver {

	public static void main(String[] args) {
		// realThing();
		test();
	}

	/**
	 * Actual method to run
	 */
	public static void realThing() {
		Scanner keyboard = new Scanner(System.in);

		System.out.println("Enter your Outlook Username\n");
		String myUsername = keyboard.nextLine();

		System.out.println("Enter your Outlook password\n");
		String myPassword = keyboard.nextLine();

		System.out.println("Enter directory location of contacts\n");
		String myFile = keyboard.nextLine() + ".xls";

		display(myUsername, myPassword, myFile);
	}

	private static void display(final String myUsername, final String myPassword, final String myFile) {
		/* Use an appropriate Look and Feel */
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(myUsername, myPassword, myFile);
			}
		});

	}

	/**
	 * Tester
	 */
	public static void test() {
		final String myUsername = "";	//enter username

		final String myPassword = "";	//enter password

		final String myFile = "";	//file path where contacts are located

		display(myUsername, myPassword, myFile);
	}

	/**
	 * Create the GUI and show it. For thread safety, this method is invoked
	 * from the event dispatch thread.
	 */
	private static void createAndShowGUI(String myUsername, String myPassword, String myFile) {

		// Create and set up the window.
		Display frame = new Display("Email Drafter", myUsername, myPassword, myFile);

		frame.setSize(700, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane.
		frame.addComponentsToPane(frame.getContentPane());

		// Display the window.
		frame.pack();
		frame.setVisible(true);

//		frame.setRecipents();

//		if (frame.isDone())
//			frame.getContacts().draftEmail();
	}

}
