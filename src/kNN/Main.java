package kNN;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		KNN kmeans = new KNN();
		kmeans.train();
	/*	kmeans.predict("MailsCollection/test/ham1.txt");
		kmeans.predict("MailsCollection/test/ham2.txt");
		kmeans.predict("MailsCollection/test/spam1.txt");
		kmeans.predict("MailsCollection/test/spam2.txt"); */
		kmeans.predictAll();
	}

}
