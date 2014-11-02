package bayes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import textProcessor.BagOfWord;
import textProcessor.Mail;
import textProcessor.MailCollection;
import textProcessor.TextProcessor;

public class Bayes {

	private final static double PARAMETER = 0.001;

	private String hamName = "MailsCollection/ham.txt";
	private String spamName = "MailsCollection/spam.txt";
	private BagOfWord nbOfDiffWordHam = new BagOfWord();
	private BagOfWord nbOfDiffWordSpam = new BagOfWord();
	private BagOfWord newBow;
	private Map<String, Double> hashTableHam = new HashMap<String, Double>();
	private Map<String, Double> hashTableSpam = new HashMap<String, Double>();
	private Map<String, Double> word_Proba = new HashMap<String, Double>();
	private int nbOfWordsOfHam = 0;
	private int nbOfWordsOfSpam = 0;

	public Bayes() throws IOException {
		train();
	}

	public void predict(String fileName) throws IOException {
		String testMail = fileName;
		MailCollection testCollection = new MailCollection(testMail);

		double numerator = 1;
		double dominator1 = 1;
		double dominator2 = 1;
		for (Mail ham : testCollection) {
			BagOfWord bow_ham = TextProcessor.process(ham);
			for (Entry<String, Integer> e : bow_ham.entrySet()) {
				String key = e.getKey();
				if (word_Proba.containsKey(key)) {
					double i = word_Proba.get(key);
					numerator *= i;
					dominator1 = numerator;
					dominator2 *= (1 - i);
				}
			}
			newBow = bow_ham;
		}
		double proba = numerator / (dominator1 + dominator2);
		if (proba < 0.5) {
			System.out.println("Probability : " + proba
					+ ", predict result : spam");
		} else {
			System.out.println("Probability : " + proba
					+ ", predict result : ham");
		}

	}

	public BagOfWord getNbOfDiffWordHam() {
		return nbOfDiffWordHam;
	}

	public String getHamName() {
		return hamName;
	}

	public String getSpamName() {
		return spamName;
	}

	public BagOfWord getNbOfDiffWordSpam() {
		return nbOfDiffWordSpam;
	}

	public Map<String, Double> getHashTableHam() {
		return hashTableHam;
	}

	public Map<String, Double> getHashTableSpam() {
		return hashTableSpam;
	}

	public Map<String, Double> getWord_Proba() {
		return word_Proba;
	}

	public int getNbOfWordsOfHam() {
		return nbOfWordsOfHam;
	}

	public int getNbOfWordsOfSpam() {
		return nbOfWordsOfSpam;
	}

	private void train() throws IOException {
		MailCollection coHam = new MailCollection(hamName);
		for (Mail ham : coHam) { // iterate over the collection
			BagOfWord bow_ham = TextProcessor.process(ham); // create a bag
															// of words for
															// ham

			for (Entry<String, Integer> coSet : bow_ham.getMap().entrySet()) {
				if (nbOfDiffWordHam.getMap().containsKey(coSet.getKey()))
					nbOfDiffWordHam.getMap().put(
							coSet.getKey(),
							nbOfDiffWordHam.getMap().get(coSet.getKey())
									+ coSet.getValue());
				else
					nbOfDiffWordHam.getMap().put(coSet.getKey(),
							coSet.getValue());
			}

			nbOfWordsOfHam += bow_ham.size();
		}

		MailCollection coSpam = new MailCollection(spamName);
		for (Mail spam : coSpam) {
			BagOfWord bow_spam = TextProcessor.process(spam);

			for (Entry<String, Integer> coSet : bow_spam.getMap().entrySet()) {
				if (nbOfDiffWordSpam.getMap().containsKey(coSet.getKey()))
					nbOfDiffWordSpam.getMap().put(
							coSet.getKey(),
							nbOfDiffWordSpam.getMap().get(coSet.getKey())
									+ coSet.getValue());
				else
					nbOfDiffWordSpam.getMap().put(coSet.getKey(),
							coSet.getValue());
			}

			nbOfWordsOfSpam += bow_spam.size();
		}

		// hashtable_good
		for (Entry<String, Integer> e : nbOfDiffWordHam.entrySet()) {
			Double i = (double) ((double) e.getValue() / (double) nbOfWordsOfHam);
			hashTableHam.put(e.getKey(), i);
		}
		// System.out.println(hashTableHam.toString());

		// hashtable_bed
		for (Entry<String, Integer> e : nbOfDiffWordSpam.entrySet()) {
			Double i = (double) ((double) e.getValue() / (double) nbOfWordsOfSpam);
			hashTableSpam.put(e.getKey(), i);
		}
		// System.out.println(hashTableSpam.toString());

		for (Entry<String, Integer> e : nbOfDiffWordHam.entrySet()) {
			word_Proba.put(e.getKey(), 0.0);
		}

		for (Entry<String, Integer> e : nbOfDiffWordSpam.entrySet()) {
			word_Proba.put(e.getKey(), 0.0);
		}

		// System.out.println(word_Proba.size());

		for (Entry<String, Double> e : word_Proba.entrySet()) {
			String key = e.getKey();
			if (hashTableHam.containsKey(key) && hashTableSpam.containsKey(key)) {
				word_Proba.put(
						key,
						hashTableHam.get(key)
								/ (hashTableHam.get(key) + hashTableSpam
										.get(key)));
			} else if (!hashTableHam.containsKey(key)) {
				word_Proba.put(key, PARAMETER / hashTableSpam.get(key));
			} else if (!hashTableSpam.containsKey(key)) {
				word_Proba.put(key, (hashTableHam.get(key) - PARAMETER)
						/ hashTableHam.get(key));
			} else {
				System.out.println("There is something woring with word_proba");
			}

		}

		// System.out.println(word_Proba.toString());
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

	public Map<String, Double> learn(boolean isHam) {
		if (isHam) {
			for (Entry<String, Integer> e : newBow.entrySet()) {
				String key = e.getKey();
				if (hashTableHam.containsKey(key)) {
					hashTableHam.put(key, e.getValue() + hashTableHam.get(key));
				} else {
				}
			}
		}
		return word_Proba;
	}
}
