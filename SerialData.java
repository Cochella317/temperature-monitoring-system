import java.util.Scanner;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/**
 * SerialData class is used to read data from the serial port.
 */
public class SerialData {
    /**
     * getSerialData method gets data from serial port.
     */
    public static void getSerialData() {
        var sp = SerialPort.getCommPort("/dev/cu.usbmodem11101");
        //define UART parameters
        sp.setComPortParameters(9600, 8,SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        //if data has not arrived yet, wait 1000 ms
        sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);

        var isOpen = sp.openPort();
        if(isOpen){
            System.out.println("Port Open!");
        } else if (!isOpen) {
            throw new IllegalStateException("Serial Port has failed to open");
        }

        //delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         *addDataLister is an anonymous class used to deal with events
         *
         * method getListening Events defines what type of event we are looking for
         * @return SerialPort.LISTENING_EVENT_DATA_AVAILABLE will call serialEvent when there is data in the serial port
         *
         * method serialEvent reads data from serial port
         * @param serialPortEvent contains information about the event that just occurred
         */
        sp.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                Scanner sc;
                //verify that it was a data event
                if (serialPortEvent.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE){
                    //creates a stream for serial data so that Scanner can read it
                    try {
                        sc = new Scanner(sp.getInputStream());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    while (sc.hasNextLine()){
                        String data = sc.nextLine();
                        String regex = "[,]";

                        String [] dataArray = data.split(regex);

                        double time = Double.parseDouble(dataArray[0]);
                        double temperature = Double.parseDouble(dataArray[1]);

                        //sends point to be plotted on graph
                        MainPlot.my_plotter.updateSeries(time, temperature);

                    }

                }

            }
        });

    }
}
