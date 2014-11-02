package textProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextProcessor {
	public final static List<String> commonWords = buildArrayFromFile("MailsCollection/common_words");

	public static BagOfWord process(Mail doc) {
		BagOfWord bow = new BagOfWord();
		String text = process(doc.text);
		String[] terms = text.split(" ");
		for (String term : terms) {
			bow.upsert(term);
		}
		return bow;
	}

	private static String process(String s) {
		return stem(filterStopWords(cleanup(s)));
	}

	private static String cleanup(String s) {
		s = s.toLowerCase();
		s = s.replaceAll("[^a-z-.(),?!: ]+", ""); // strip out every non
													// alpha-punctuation
													// characters
		s = s.replaceAll("[-.(),?!:]", " "); // replace all punctuation symbols
												// with whitespace
		s = s.replaceAll("[ ]+", " "); // merge all successions of whitespaces
										// into one
		s = s.trim();
		return s;
	}

	private static String filterStopWords(String s) {
		s = " " + s + " ";
		for (String word : commonWords) {
			if (s.equals(word))
				return "";
			s = s.replace(" " + word + " ", " ");
		}
		s = s.trim();
		return s;
	}

	private static String stem(String s) {
		String[] words = s.split(" ");
		StringBuilder builder = new StringBuilder();
		for (String word : words) {
			PorterStemmer stemmer = new PorterStemmer();
			stemmer.add(word.toCharArray(), word.length());
			stemmer.stem();
			builder.append(stemmer.toString() + " ");
		}
		return builder.toString().trim();
	}

	private static List<String> buildArrayFromFile(String filename) {
		List<String> words = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(new File(filename));
			BufferedReader in = new BufferedReader(fr);
			String buf = in.readLine();
			while (true) {
				if (buf == null) {
					break;
				}
				if (buf != "/*") {
					words.add(buf);
				}
				buf = in.readLine();
			}
			in.close();

		} catch (IOException e) {
			System.err.println("Can't open file " + filename
					+ " for reading... Loading aborted");
		} catch (NumberFormatException e) {
			System.err.println("Problem reading numbers in: " + filename);
		} catch (Exception e) {
			System.err.println("Invalid Format : " + filename
					+ "... Loading aborted");
		}
		return words;
	}
}
