package textProcessor;

public class Mail {
	public final int id;
	public final String text;

	public Mail(int id, String text) {
		this.id = id;
		this.text = text;
	}

	@Override
	public String toString() {
		return id + "\t" + text;
	}
}
