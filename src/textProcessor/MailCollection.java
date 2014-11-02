package textProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class MailCollection implements Iterable<Mail> {
	private final String filename;

	public MailCollection(String filename) throws IOException {
		this.filename = filename;
	}

	@Override
	public Iterator<Mail> iterator() {
		try {
			return new CollectionIterator();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private class CollectionIterator implements Iterator<Mail> {
		private final FileReader fr;
		private final BufferedReader in;
		private String currentLine;
		private int currentID;
		private StringBuilder currentText;

		private CollectionIterator() throws IOException {
			fr = new FileReader(new File(filename));
			in = new BufferedReader(fr);
			currentLine = in.readLine();
			currentID = -1;
		}

		@Override
		public void finalize() throws IOException {
			in.close();
			fr.reset();
		}

		@Override
		public boolean hasNext() {
			return currentLine != null;
		}

		@Override
		public Mail next() {
			try {
				while (true) {
					if (currentID == -1 && currentLine.startsWith(".I")) { // beginning
																			// of
																			// doc
						currentID = Integer.valueOf(currentLine.substring(3));
						currentLine = in.readLine();
						currentText = new StringBuilder();
					}
					if (currentID != -1
							&& (currentLine == null || currentLine
									.startsWith(".I"))) { // end of doc
						int tmp = currentID;
						currentID = -1;
						return new Mail(tmp, currentText.toString().trim());
					} else if (currentLine.startsWith(".T")) { // title
						currentLine = in.readLine();
						if (!currentLine.startsWith(".")) {
							currentText.append(" " + currentLine);
						}
					} else if (currentLine.startsWith(".S")) { // abstract
						currentLine = in.readLine();
						while (currentLine != null
								&& !currentLine.startsWith(".")) {
							currentText.append(" " + currentLine);
							currentLine = in.readLine();
						}
					} else { // skip all the others line
						currentLine = in.readLine();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
