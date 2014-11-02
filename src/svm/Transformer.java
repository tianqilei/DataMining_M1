package svm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import textProcessor.BagOfWord;
import textProcessor.Mail;
import textProcessor.MailCollection;
import textProcessor.TextProcessor;

public class Transformer {

	private String hamName = "MailsCollection/ham.txt";
	private String spamName = "MailsCollection/spam.txt";
	private BagOfWord allDiffWord = new BagOfWord();
	private Map<String, Integer> indexWordMap = new HashMap<String, Integer>();
	private Map<String, IndexWord> iwMap;

	public Map<String, Integer> processTextForTraining() throws IOException {
		MailCollection coHam = new MailCollection(hamName);
		for (Mail ham : coHam) {
			BagOfWord bow_ham = TextProcessor.process(ham);
			for (Entry<String, Integer> coSet : bow_ham.getMap().entrySet()) {
				allDiffWord.upsert(coSet.getKey());
			}

		}

		MailCollection coSpam = new MailCollection(spamName);
		for (Mail spam : coSpam) {
			BagOfWord bow_ham = TextProcessor.process(spam);

			for (Entry<String, Integer> coSet : bow_ham.getMap().entrySet()) {
				allDiffWord.upsert(coSet.getKey());
			}

		}

		int id = 1;
		for (Entry<String, Integer> e : allDiffWord.getMap().entrySet()) {
			indexWordMap.put(e.getKey(), id++);
		}

		System.out.println(indexWordMap.toString());
		for (Mail ham : coHam) {
			BagOfWord bow_ham = TextProcessor.process(ham);
			iwMap = new HashMap<String, IndexWord>();
			for (Entry<String, Integer> coSet : bow_ham.getMap().entrySet()) {
				String key = coSet.getKey();
				int nb = coSet.getValue();
				iwMap.put(key, new IndexWord(indexWordMap.get(key), nb));
			}

			System.out.print("1 ");
			for (IndexWord iw : iwMap.values()) {
				System.out.print(iw.toString());
			}
			System.out.println();
		}

		for (Mail spam : coSpam) {
			BagOfWord bow_spam = TextProcessor.process(spam);
			iwMap = new HashMap<String, IndexWord>();
			for (Entry<String, Integer> coSet : bow_spam.getMap().entrySet()) {
				String key = coSet.getKey();
				int nb = coSet.getValue();
				iwMap.put(key, new IndexWord(indexWordMap.get(key), nb));
			}

			System.out.print("-1 ");
			for (IndexWord iw : iwMap.values()) {
				System.out.print(iw.toString());
			}
			System.out.println();
		}

		return indexWordMap;
	}

	public void processTestForTest() throws IOException {
		indexWordMap = processTextForTraining();
		String hamTestName1 = "MailsCollection/test/ham1.txt";
		String hamTestName2 = "MailsCollection/test/ham2.txt";
		String spamTestName1 = "MailsCollection/test/spam1.txt";
		String spamTestName2 = "MailsCollection/test/spam2.txt";

		processMail(hamTestName1, 1, indexWordMap);
		processMail(hamTestName2, 1, indexWordMap);
		processMail(spamTestName1, -1, indexWordMap);
		processMail(spamTestName2, -1, indexWordMap);
	}

	private void processMail(String testName, int i,
			Map<String, Integer> indexWordMap) throws IOException {
		MailCollection mc = new MailCollection(testName);
		for (Mail ham : mc) {
			BagOfWord bow_ham = TextProcessor.process(ham);
			iwMap = new HashMap<String, IndexWord>();
			for (Entry<String, Integer> coSet : bow_ham.getMap().entrySet()) {
				String key = coSet.getKey();
				int nb = coSet.getValue();
				if (indexWordMap.containsKey(key)) {
					iwMap.put(key, new IndexWord(indexWordMap.get(key), nb));
				}
			}

			System.out.print(i + " ");
			for (IndexWord iw : iwMap.values()) {
				System.out.print(iw.toString());
			}
			System.out.println();

		}
	}

}
