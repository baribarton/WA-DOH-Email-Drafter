import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class Display extends JFrame {

	private ArrayList<String> names;
	private ArrayList<String> emails;
	private ArrayList<String> recipients;
	private EmailDrafter contacts = null;
	private boolean done;

	public final static int MAX_GAP = 50;
	GridLayout experimentLayout = new GridLayout(0, 2);

	public Display(String title, String newUsername, String newPassword, String newFile) {
		super(title);
		contacts = new EmailDrafter(newUsername, newPassword, newFile);
		contacts.read();

		names = new ArrayList<String>();
		emails = new ArrayList<String>();
		recipients = new ArrayList<String>();
		done = false;

		// deep copy arrays
		for (int i = 0; i < contacts.getContactEmails().size(); i++) {
			names.add(contacts.getContactNames().get(i));
			emails.add(contacts.getContactEmails().get(i));
		}

		// setResizable(false);
	}

	/**
	 * Adds each component to the main pane
	 * 
	 * @param pane
	 *            the main pane
	 */
	public void addComponentsToPane(final Container pane) {
		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(experimentLayout);

		final Font defaultFont = new Font("Times New Roman", Font.BOLD, 20);

		for (int i = 0; i < names.size(); i++) {
			final JButton button = new JButton(names.get(i));
			final JLabel label = new JLabel(emails.get(i));

			// change color of button and text on click
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (button.getBackground().equals(Color.GRAY)) {
						button.setBackground(null);
						label.setForeground(Color.BLACK);
						label.setFont(defaultFont);

						System.out.println(recipients.remove(label.getText()));
					} else {
						button.setBackground(Color.GRAY);
						label.setForeground(Color.DARK_GRAY);
						label.setFont(new Font("Times New Roman", Font.PLAIN, 17));

						recipients.add(label.getText());
						System.out.println("Added: " + label.getText());
					}
				}
			});

			// change fonts
			label.setFont(defaultFont);
			button.setFont(defaultFont);

			mainPanel.add(button);
			mainPanel.add(label);
		}

		experimentLayout.layoutContainer(mainPanel);
		experimentLayout.setHgap(10);

		// make into a scrollable screen
		JScrollPane scroll = new JScrollPane(mainPanel);

		GridLayout top = new GridLayout(0, 2);
		final JPanel topLabels = new JPanel();
		topLabels.setLayout(top);

		JLabel names = new JLabel("    Names");
		JLabel emails = new JLabel("    Email Addresses");

		Font labelFont = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 40);
		names.setFont(labelFont);
		emails.setFont(labelFont);

		topLabels.add(names);
		topLabels.add(emails);

		final JButton draftButton = createDraftButton(labelFont);

		pane.add(draftButton, BorderLayout.SOUTH);
		pane.add(scroll);
		pane.add(topLabels, BorderLayout.NORTH);
		scroll.setPreferredSize(new Dimension(700, 700));
	}

	/**
	 * TODO: This does not work properly Creates the "Create Draft!" Button with
	 * proper formatting
	 * 
	 * @param labelFont
	 *            font that the button will have
	 * @return the "Create Draft!" button
	 */
	public JButton createDraftButton(Font labelFont) {
		final JButton draftButton = new JButton("Create Draft!");

		draftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setRecipents();
				contacts.draftEmail();
//				PopupFactory factory = PopupFactory.getSharedInstance();
//
//				JList list = new JList(names.toArray());
//				JPanel frame = new JPanel();
//				frame.add(list);
//
//				final Popup popup = factory.getPopup(frame, list, 300, 500);
//				popup.show();
			}
		});

		draftButton.setFont(labelFont);
		return draftButton;
	}

	/**
	 * names and email array lists to string
	 */
	public String toString() {
		String contacts = "";
		for (int i = 0; i < names.size(); i++) {
			contacts += names.get(i) + ": " + emails.get(i) + "\n";
		}

		return contacts;
	}

	public void setRecipents() {
		contacts.setContactEmails(recipients);
	}
	
	public boolean isDone() {
		return done;
	}

	public EmailDrafter getContacts() {
		return contacts;
	}
}