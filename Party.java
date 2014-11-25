import java.net.URL;
import javax.sound.sampled.*;

public class Party extends Thread {
	private PartyPerson[][] partyPeople = new PartyPerson[4][6];
	private String[][] currentInput = new String[4][1];
	private boolean stopPartying;

	Party() {
		// Green Box
		partyPeople[0][0] = new PartyPerson(0, "0c3fdb35", Party.class.getResource("WavVersions/Beat1.wav"));
		partyPeople[0][1] = new PartyPerson(0, "a024db35", Party.class.getResource("WavVersions/Beat2.wav"));
		partyPeople[0][2] = new PartyPerson(0, "c0bc17c5", Party.class.getResource("WavVersions/Beat3.wav"));
		partyPeople[0][3] = new PartyPerson(0, "3742db35", Party.class.getResource("WavVersions/Beat4.wav"));
		partyPeople[0][4] = new PartyPerson(0, "5144d935", Party.class.getResource("WavVersions/Beat5.wav"));
		partyPeople[0][5] = new PartyPerson(0, "dafbd835", Party.class.getResource("WavVersions/Beat6.wav"));

		//Red Box
		partyPeople[1][0] = new PartyPerson(1, "4caad835", Party.class.getResource("WavVersions/Bass1.wav"));
		partyPeople[1][1] = new PartyPerson(1, "b59617c5", Party.class.getResource("WavVersions/Bass2.wav"));
		partyPeople[1][2] = new PartyPerson(1, "9c1bd935", Party.class.getResource("WavVersions/Bass3.wav"));
		partyPeople[1][3] = new PartyPerson(1, "4de7d735", Party.class.getResource("WavVersions/Bass4.wav"));
		partyPeople[1][4] = new PartyPerson(1, "0c1618c5", Party.class.getResource("WavVersions/Bass5.wav"));
		partyPeople[1][5] = new PartyPerson(1, "8e64d835", Party.class.getResource("WavVersions/Bass6.wav"));

		// Yellow Box
		partyPeople[2][0] = new PartyPerson(2, "8436c955", Party.class.getResource("WavVersions/Lead1.wav"));
		partyPeople[2][1] = new PartyPerson(2, "1aa4da35", Party.class.getResource("WavVersions/Lead2.wav"));
		partyPeople[2][2] = new PartyPerson(2, "541bd935", Party.class.getResource("WavVersions/Lead3.wav"));
		partyPeople[2][3] = new PartyPerson(2, "fb9b17c5", Party.class.getResource("WavVersions/Lead4.wav"));
		partyPeople[2][4] = new PartyPerson(2, "ed08d935", Party.class.getResource("WavVersions/Lead5.wav"));
		partyPeople[2][5] = new PartyPerson(2, "83aad835", Party.class.getResource("WavVersions/Lead6.wav"));

		// Blue Box
		partyPeople[3][0] = new PartyPerson(3, "841bd935", Party.class.getResource("WavVersions/LeadB1.wav"));
		partyPeople[3][1] = new PartyPerson(3, "30aad835", Party.class.getResource("WavVersions/LeadB2.wav"));
		partyPeople[3][2] = new PartyPerson(3, "b3abd835", Party.class.getResource("WavVersions/LeadB3.wav"));
		partyPeople[3][3] = new PartyPerson(3, "cdc617c5", Party.class.getResource("WavVersions/LeadB4.wav"));
		partyPeople[3][4] = new PartyPerson(3, "035f18c5", Party.class.getResource("WavVersions/LeadB5.wav"));
		partyPeople[3][5] = new PartyPerson(3, "a91bd935", Party.class.getResource("WavVersions/LeadB6.wav"));

		stopPartying = false;
	}

	public void run() {
		for (int i = 0; i < partyPeople.length; i++) {
			for (int j = 0; j < partyPeople[0].length; j++) {
				partyPeople[i][j].hush();
				partyPeople[i][j].start();
			}
		}

		while (!stopPartying) {
			for (int i = 0; i < currentInput.length; i++) {
				if (currentInput[i][0] != null) {
					for (int j = 0; j < partyPeople[i].length; j++) {
						if (currentInput[i][0].equals(partyPeople[i][j].whoAreYou())) partyPeople[i][j].shout();
						else partyPeople[i][j].hush();
					}
				}																		
			}
		}
	}

	public void stop() {  // who called the cops?
		stopPartying = true;
	}

	public void crashParty(int id, String tagUID) {
		currentInput[id][0] = tagUID;
	}

	private class PartyPerson extends Thread {
		private int id;
		private String tagUID;

		private Clip ac;
		private DataLine.Info info;
		private AudioFormat format;
		private Line line;
		private FloatControl volume;

		PartyPerson(int id, String tagUID, URL url) {
			this.id = id;
			this.tagUID = tagUID;

			try {
				AudioInputStream ais = AudioSystem.getAudioInputStream(url);
				if (ais != null) {
					format = ais.getFormat();
					info = new DataLine.Info(Clip.class, format, AudioSystem.NOT_SPECIFIED);
					line = AudioSystem.getLine(info);
					ac = (Clip) line;
					ac.open(ais);
					volume = (FloatControl) ac.getControl(FloatControl.Type.MASTER_GAIN);
				}
			} catch (Exception e) {System.err.println(e.toString());}
		}

		public void run() {
			try {ac.loop(Clip.LOOP_CONTINUOUSLY);}
			catch (Exception e) {System.err.println(e.toString());}

			while (true) {}  // 24 hour party people
		}

		public void shout() {  // let it all out
			volume.setValue(volume.getMaximum());
		}

		public void hush() {
			volume.setValue(volume.getMinimum());
		}

		public String whoAreYou() {
			return tagUID;
		}
	}
}
