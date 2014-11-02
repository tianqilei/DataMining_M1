package kNN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;

import textProcessor.BagOfWord;
import textProcessor.Mail;
import textProcessor.MailCollection;
import textProcessor.TextProcessor;

// it can just say yes or no, there is no probability
public class KNN {

	private final static int K = 9;

	private String hamName = "MailsCollection/ham.txt";
	private String spamName = "MailsCollection/spam.txt";
	private BagOfWord diffWordHam;
	private BagOfWord diffWordSpam;
	private BagOfWord newBow;
	private int nbOfWordsInHam;
	private int nbOfWordsInSpam;
	private ArrayList<Integer> distenceList;
	private ArrayList<Mail2> mails;

	public KNN() throws IOException {
		diffWordHam = new BagOfWord();
		diffWordSpam = new BagOfWord();
		nbOfWordsInHam = 0;
		nbOfWordsInSpam = 0;
		distenceList = new ArrayList<Integer>();
		mails = new ArrayList<Mail2>();
	}

	public void predict(String fileName) throws IOException {
		ArrayList<Mail2> temp = mails;
		String testMail = fileName;
		nbOfWordsInHam = 0;
		nbOfWordsInSpam = 0;
		MailCollection testCollection = new MailCollection(testMail);

		for (Mail ham : testCollection) {
			BagOfWord bow_ham = TextProcessor.process(ham);
			for (Entry<String, Integer> e : bow_ham.entrySet()) {
				String key = e.getKey();
				if (diffWordHam.getMap().containsKey(key)) {
					nbOfWordsInHam++;
				}

				if (diffWordSpam.getMap().containsKey(key)) {
					nbOfWordsInSpam++;
				}
			}
		}

		Iterator<Mail2> iterator = temp.iterator();
		while (iterator.hasNext()) {
			Mail2 m = iterator.next();
			int distance;
			m.setDistance((distance = calculateDistance(m, nbOfWordsInHam,
					nbOfWordsInSpam)));
			distenceList.add(distance);
		}

		Collections.sort(mails, new ComparatorMail());

		int isHam = 0;
		int isSpam = 0;

		for (int i = 0; i < K; i++) {
			Mail2 m = mails.get(i);

			if (m.getIsHam() == 1) {
				isHam += 1;
			} else {
				isSpam += 1;
			}
		}

		if (isHam > isSpam) {
			System.out.println("predict result : Ham");
		} else if (isHam < isSpam) {
			System.out.println("predict result : Spam");
		} else {
			System.out.println("Ham or Spam");
		}

	};

	private int powerOf2(int i) {
		return i * i;
	}

	public int calculateDistance(Mail2 m, int nbOfWordsInHam,
			int nbOfWordsInSpam) {
		return powerOf2(m.getNbOfGoodWord() - nbOfWordsInHam)
				+ powerOf2(m.getNbOfBedWord() - nbOfWordsInSpam);
	}

	public void train() throws IOException {
		MailCollection coHam = new MailCollection(hamName);
		for (Mail ham : coHam) {
			BagOfWord bow_ham = TextProcessor.process(ham);

			for (java.util.Map.Entry<String, Integer> coSet : bow_ham.getMap()
					.entrySet()) {
				if (diffWordHam.getMap().containsKey(coSet.getKey()))
					diffWordHam.getMap().put(
							coSet.getKey(),
							diffWordHam.getMap().get(coSet.getKey())
									+ coSet.getValue());
				else
					diffWordHam.getMap().put(coSet.getKey(), coSet.getValue());
			}

			nbOfWordsInHam += bow_ham.size();
		}

		MailCollection coSpam = new MailCollection(spamName);
		for (Mail spam : coSpam) {
			BagOfWord bow_spam = TextProcessor.process(spam);

			for (java.util.Map.Entry<String, Integer> coSet : bow_spam.getMap()
					.entrySet()) {
				if (diffWordSpam.getMap().containsKey(coSet.getKey()))
					diffWordSpam.getMap().put(
							coSet.getKey(),
							diffWordSpam.getMap().get(coSet.getKey())
									+ coSet.getValue());
				else
					diffWordSpam.getMap().put(coSet.getKey(), coSet.getValue());
			}

			nbOfWordsInSpam += bow_spam.size();
		}
		int id = 0;

		for (Mail ham : coHam) {
			int nbGood = 0;
			int nbBed = 0;
			BagOfWord bow_ham = TextProcessor.process(ham);
			for (Entry<String, Integer> e : bow_ham.entrySet()) {
				String key = e.getKey();
				if (diffWordHam.getMap().containsKey(key)) {
					nbGood += 1;
				}

				if (diffWordSpam.getMap().containsKey(key)) {
					nbBed += 1;
				}
			}
			mails.add(new Mail2(id++, nbBed, nbGood, 1));
			nbGood = 0;
			nbBed = 0;

		}

		for (Mail spam : coSpam) {
			int nbGood = 0;
			int nbBed = 0;
			BagOfWord bow_spam = TextProcessor.process(spam);
			for (Entry<String, Integer> e : bow_spam.entrySet()) {
				String key = e.getKey();
				if (diffWordHam.getMap().containsKey(key)) {
					nbGood += 1;
				}

				if (diffWordSpam.getMap().containsKey(key)) {
					nbBed += 1;
				}
			}
			mails.add(new Mail2(id++, nbBed, nbGood, 0));
			nbGood = 0;
			nbBed = 0;
		}

	}

	public String getHamName() {
		return hamName;
	}

	public String getSpamName() {
		return spamName;
	}

	public BagOfWord getDiffWordHam() {
		return diffWordHam;
	}

	public BagOfWord getDiffWordSpam() {
		return diffWordSpam;
	}

	public BagOfWord getNewBow() {
		return newBow;
	}

	public int getNbOfWordsInHam() {
		return nbOfWordsInHam;
	}

	public int getNbOfWordsInSpam() {
		return nbOfWordsInSpam;
	}

	public ArrayList<Integer> getDistenceList() {
		return distenceList;
	}

	public ArrayList<Mail2> getMails() {
		return mails;
	}

	public void predictAll() throws IOException {
		System.out.println("Predict ham : ");
		predict("MailsCollection/ham/1.txt");
		predict("MailsCollection/ham/2.txt");
		predict("MailsCollection/ham/3.txt");
		predict("MailsCollection/ham/4.txt");
		predict("MailsCollection/ham/5.txt");
		predict("MailsCollection/ham/6.txt");
		predict("MailsCollection/ham/7.txt");
		predict("MailsCollection/ham/8.txt");
		predict("MailsCollection/ham/9.txt");
		predict("MailsCollection/ham/10.txt");
		predict("MailsCollection/ham/11.txt");
		predict("MailsCollection/ham/12.txt");
		predict("MailsCollection/ham/13.txt");
		predict("MailsCollection/ham/14.txt");
		predict("MailsCollection/ham/15.txt");
		predict("MailsCollection/ham/16.txt");
		predict("MailsCollection/ham/18.txt");
		predict("MailsCollection/ham/19.txt");
		predict("MailsCollection/ham/20.txt");
		predict("MailsCollection/ham/21.txt");
		predict("MailsCollection/ham/22.txt");
		predict("MailsCollection/ham/23.txt");
		predict("MailsCollection/ham/24.txt");
		predict("MailsCollection/test/ham1.txt");
		predict("MailsCollection/test/ham2.txt");

		System.out.println("Predict spam : ");
		predict("MailsCollection/spam/1.txt");
		predict("MailsCollection/spam/2.txt");
		predict("MailsCollection/spam/3.txt");
		predict("MailsCollection/spam/4.txt");
		predict("MailsCollection/spam/5.txt");
		predict("MailsCollection/spam/6.txt");
		predict("MailsCollection/spam/7.txt");
		predict("MailsCollection/spam/8.txt");
		predict("MailsCollection/spam/9.txt");
		predict("MailsCollection/spam/11.txt");
		predict("MailsCollection/spam/12.txt");
		predict("MailsCollection/spam/13.txt");
		predict("MailsCollection/spam/14.txt");
		predict("MailsCollection/spam/15.txt");
		predict("MailsCollection/spam/16.txt");
		predict("MailsCollection/spam/17.txt");
		predict("MailsCollection/spam/18.txt");
		predict("MailsCollection/spam/19.txt");
		predict("MailsCollection/spam/21.txt");
		predict("MailsCollection/spam/22.txt");
		predict("MailsCollection/spam/23.txt");
		predict("MailsCollection/spam/24.txt");
		predict("MailsCollection/spam/25.txt");
		predict("MailsCollection/test/spam1.txt");
		predict("MailsCollection/test/spam2.txt");

	}

	private class ComparatorMail implements Comparator<Mail2> {

		public int compare(Mail2 mail0, Mail2 mail1) {
			return mail0.getDistance() - mail1.getDistance();
		}
	}

}
