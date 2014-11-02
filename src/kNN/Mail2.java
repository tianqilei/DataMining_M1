package kNN;

public class Mail2 {

	private int id;
	private int nbOfBedWord;
	private int nbOfGoodWord;
	private int isHam;
	private int distance;

	public Mail2(int id, int nbOfBedWord, int nbOfGoodWord, int isHam) {
		super();
		this.id = id;
		this.nbOfBedWord = nbOfBedWord;
		this.nbOfGoodWord = nbOfGoodWord;
		this.isHam = isHam;
		distance = 0;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNbOfBedWord() {
		return nbOfBedWord;
	}

	public void setNbOfBedWord(int nbOfBedWord) {
		this.nbOfBedWord = nbOfBedWord;
	}

	public int getNbOfGoodWord() {
		return nbOfGoodWord;
	}

	public void setNbOfGoodWord(int nbOfGoodWord) {
		this.nbOfGoodWord = nbOfGoodWord;
	}

	public int getIsHam() {
		return isHam;
	}

	public void setIsHam(int isHam) {
		this.isHam = isHam;
	}

	@Override
	public String toString() {
		return "Mail2 [id=" + id + ", nbOfBedWord=" + nbOfBedWord
				+ ", nbOfGoodWord=" + nbOfGoodWord + ", isHam=" + isHam
				+ ", distance=" + distance + "]";
	}

}
