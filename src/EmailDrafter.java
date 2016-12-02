
// [START simple_includes]
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;

public class EmailDrafter {

	// first dimension is the group name
	private ArrayList<String> contactNames;
	private ArrayList<String> contactEmails;
	private String username, password, file;

	// the column at which to stop reading at in excel
	public final int MAX_COL = 2;

	public EmailDrafter(String newUsername, String newPassword, String newFile) {
		contactNames = new ArrayList<String>();
		contactEmails = new ArrayList<String>();
		username = newUsername;
		password = newPassword;
		file = newFile;
	}

	private void setContacts(HSSFRow row, HSSFSheet sheet, int rows) {
		HSSFCell cell = null;
		ArrayList<String> contacts = new ArrayList<String>();

		// used to convert cells to Strings
		DataFormatter formatter = new DataFormatter();

		for (int r = 1; r < rows; r++) {
			row = sheet.getRow(r);
			if (row != null) {
				for (int c = 0; c < MAX_COL; c++) {
					cell = row.getCell((short) c);

					// formats cells as Strings
					String value = formatter.formatCellValue(cell);
					if (cell != null) {
						contacts.add(value);
					}
				}
			}
		}

		splitArray(contacts);

	}

	/**
	 * read file and put contents into an array
	 */
	public void read() {
		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = null;

			int allRows; // Number of rows
			allRows = sheet.getPhysicalNumberOfRows();

			// This trick ensures that we get the data properly even if it
			// doesn't start from first few rows
			for (int i = 0; i < 10 || i < allRows; i++) {
				row = sheet.getRow(i);
			}

			setContacts(row, sheet, allRows);
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * splits the contacts array into two arrays: one with just the names and
	 * one with just the email
	 * 
	 * @param contacts
	 *            the array that holds the name and emails of the contacts
	 */
	private void splitArray(ArrayList<String> contacts) {
		// System.out.println(contacts.size());
		while (!contacts.isEmpty()) {
			contactNames.add(contacts.remove(0));
			contactEmails.add(contacts.remove(0));
		}
	}

	/**
	 * This method puts an email in the draft folder with the recipients loaded
	 */
	public void draftEmail() {

		// email setup
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props, null);
		Store store = null;
		try {
			store = session.getStore("imaps");
		} catch (NoSuchProviderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			store.connect("imap-mail.outlook.com", username, password);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(store);

		Folder draft = null;

		try {
			// Draft mail
			Message message = new MimeMessage(session);

			// write to email
			for (int i = 0; i < contactEmails.size(); i++) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(contactEmails.get(i)));
			}

			message.setSubject("This is the Subject Line!");
			message.setText("This is message body");

			// open drafts folder
			draft = store.getFolder("drafts");
			draft.open(Folder.READ_WRITE);
			draft.appendMessages(new Message[] { message });

			System.out.println(draft.getFullName() + " has : " + draft.getMessageCount() + " Message(s)");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Displays the contact info to screen
	 */
	public void display() {
		System.out.println("Names size = " + contactNames.size());
		System.out.println("Emails size = " + contactEmails.size());
		for (int i = 0; i < contactNames.size(); i++) {
			System.out.println(contactNames.get(i) + ": " + contactEmails.get(i));
		}
	}

	public ArrayList<String> getContactNames() {
		return contactNames;
	}

	public ArrayList<String> getContactEmails() {
		return contactEmails;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFile() {
		return file;
	}

	/**
	 * deep copy array list
	 * 
	 * @param emails
	 *            array list to be copied
	 */
	public void setContactEmails(ArrayList<String> emails) {

		// remove everything in the list
		while (!contactEmails.isEmpty()) {
			contactEmails.remove(0);
		}

		for (int i = 0; i < emails.size(); i++) {
			contactEmails.add(emails.get(i));
		}
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFile(String file) {
		this.file = file;
	}
}