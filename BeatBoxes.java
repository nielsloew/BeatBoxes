public class BeatBoxes {
	public static void main(String[] args) {
		Party party = new Party();
		party.start();

		SerialListener main = new SerialListener(party);
		main.initialize();
	}
}
