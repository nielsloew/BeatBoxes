import gnu.io.*;
import java.io.*;
import java.util.Enumeration;

public class SerialListener implements SerialPortEventListener {
	private SerialPort serialPort;
	private Party party;

	SerialListener(Party party) {
		this.party = party;
	}

	private static final String PORT_NAMES[] = { 
		"/dev/tty.usbmodem1411", // Mac OS X
        "/dev/ttyACM0", // Raspberry Pi
		"/dev/ttyUSB0", // Linux
		"COM3", // Windows
	};

	private BufferedReader input;
	private OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;

	public void initialize() {
        //System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");  // Use this only for Raspberry pi

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("CommPortIdentifier fatal error. Could not find COM port.");
			System.exit(0);
		}

		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(),TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();

			//party.stop();  // The party never stops
		}
	}

	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = input.readLine();
				String tagUID = null;
				int id = 0;

				if (inputLine.length() > 0) {
					if (inputLine.indexOf(".") > 0) {
						try {
							id = Integer.parseInt(inputLine.substring(0,inputLine.indexOf(".")));
						} catch (NumberFormatException e) {
							System.out.println(e);
						}
						tagUID = inputLine.substring(inputLine.indexOf(".") + 1);
					} else if (inputLine.equals("TERMINATE")) System.exit(0);

					if (id >= 1 && id <= 4) {
						id--;
						party.crashParty(id, tagUID);
					}
				}
			} catch (Exception e) {System.err.println(e.toString());}
		}
	}
}
