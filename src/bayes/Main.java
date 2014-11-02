package bayes;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		Bayes bayes = new Bayes();
/*		bayes.predict("MailsCollection/test/ham1.txt");
		bayes.predict("MailsCollection/test/ham2.txt");
		bayes.predict("MailsCollection/test/spam1.txt");
		bayes.predict("MailsCollection/test/spam2.txt");*/

		 bayes.predictAll();
		// bayes.predict("MailsCollection/ham/r1.txt");

	}

}
