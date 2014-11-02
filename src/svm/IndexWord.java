package svm;

public class IndexWord {

	private int index;
	private int nb;

	public IndexWord(int index, int nb) {
		super();
		this.index = index;
		this.nb = nb;
	}

	public int getIndex() {
		return index;
	}

	public int getNb() {
		return nb;
	}

	@Override
	public String toString() {
		return index + ":" + nb + " ";
	}

}
