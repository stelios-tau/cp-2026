package cp.Week9;

/*
 * This example does not work in a 64bit build.
 * Can someone trigger it?
 */

class SharedData {
    public long sharedLong = -1;  // Shared long variable (not volatile!)

    public void toggleValue() {
        sharedLong = -1; // Write -1 (0xFFFFFFFFFFFFFFFF)
        sharedLong = 0x0;  // Write 0   (0x0000000000000000)
    }
}

class WritingThread extends Thread {
    private final SharedData data;

    public WritingThread(SharedData data) {
        this.data = data;
    }

    public void run() {
        while (true) {
            data.toggleValue();  // Rapidly switch between -1 and 0
        }
    }
}

class ReadingThread extends Thread {
    private final SharedData data;

    public ReadingThread(SharedData data) {
        this.data = data;
    }

    public void run() {
        while (true) {
            long value = data.sharedLong;  // Read shared long
            if (value != -1 && value != 0) {  // Check for corrupted value
                System.out.println("⚠️ Data race detected! Read corrupted value: " + value);
                System.exit(0);  // Exit immediately once corruption is found
            }
        }
    }
}


public class DataRace {
    public static void main(String[] args) {
        SharedData data = new SharedData();

        Thread writer = new WritingThread(data);
        Thread reader = new ReadingThread(data);

        writer.start();
        reader.start();
    }
}
