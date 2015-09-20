import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

public class DemandPagingSimulator {
	static Integer num_p_frames = 0; // number of physical frames
	final int NUM_V_FRAMES = 20; // number of virtual frames
	public static void main(String[] args) {
		DemandPagingSimulator demandPagingSimulator = new DemandPagingSimulator();
		demandPagingSimulator.setNumPhysFrames();
		char[] ref = new char[8];
		boolean hasRefStr = false;
		for (;;) {
			demandPagingSimulator.printPrompt();
			String s = demandPagingSimulator.selectOption();
			if (s.equals("0")) {
				System.out.println("Exit!");
				break;
			} else if (s.equals("1")) {
				ref = demandPagingSimulator.readRef();
				hasRefStr = true;
				System.out.println("");
			} else if (s.equals("2")) {
				ref = demandPagingSimulator.generateRef();
				System.out.println(ref);
				hasRefStr = true;
				System.out.println("");
			} else if (s.equals("3")) {
				if (hasRefStr == true) {
					System.out.print("Current reference string is ");
					System.out.println(ref);
				} else {
					System.out.println("There is no reference string stored yet. Set a reference string!");
				}
				System.out.println("");
			} else if (s.equals("4")) {
				if (hasRefStr == true) {
					demandPagingSimulator.simulateFIFO(ref);
				} else {
					System.out.println("There is no reference string stored yet. Set a reference string!");
				}
				System.out.println("");
			} else if (s.equals("5")) {
				if (hasRefStr == true) {
					demandPagingSimulator.simulateOPT(ref);
				} else {
					System.out.println("There is no reference string stored yet. Set a reference string!");
				}
				System.out.println("");
			} else if (s.equals("6")) {
				if (hasRefStr == true) {
					demandPagingSimulator.simulateLRU(ref);
				} else {
					System.out.println("There is no reference string stored yet. Set a reference string!");
				}
				System.out.println("");
			} else if (s.equals("7")) {
				if (hasRefStr == true) {
					// to be implemented.
					// demandPagingSimulator.simulateLFU(ref);
				} else {
					System.out.println("There is no reference string stored yet. Set a reference string!");
				}
				System.out.println("");
			} else {
				System.out.println("Please enter a number between 0 and 7.");
				System.out.println("");
			}
		}
	}
	// called this method when main is called.
	private void setNumPhysFrames() {
		System.out.println("Set a number of physical frames: ");
		Scanner sc = new Scanner(System.in);
		num_p_frames = sc.nextInt();
	}
	// called this method when prompt is displayed.
	private void printPrompt() {
		System.out.println("0 - Exit");
		System.out.println("1 - Read reference string");
		System.out.println("2 - Generate reference string");
		System.out.println("3 - Display current reference string");
		System.out.println("4 - Simulate FIFO");
		System.out.println("5 - Simulate OPT");
		System.out.println("6 - Simulate LRU	");
		System.out.println("7 - Simulate LFU");
		System.out.print("Select option: ");
	}
	// called this method when prompt is displayed.
	private String selectOption() {
		Scanner sc = new Scanner(System.in);
		String str = sc.next();
		return str;
	}
	// called this method when option 1 is selected.
	private char[] readRef() {
		char[] array = new char[NUM_V_FRAMES];
		System.out.print("Set a reference string (less than " + NUM_V_FRAMES + " figures): ");
		Scanner sc = new Scanner(System.in);
		String str = sc.next();
		array = str.toCharArray();
		return array;
	}
	// called this method when option 2 is selected.
	private char[] generateRef() {
		char[] array = new char[NUM_V_FRAMES];
		Random random = new Random();
		String str = "";
		for (int i = 0; i < NUM_V_FRAMES; i++) {
			str = str.concat(String.valueOf(random.nextInt(10)));
		}
		array = str.toCharArray();
		return array;
	}
	// called this method when option 4 is selected.
	private void simulateFIFO (char[] array) {
		LinkedList queue = new LinkedList();
		int numPageFaults = 0;
		for (int i = 0; i < array.length; i++) {
			if (queue.contains(array[i])) {
			} else {
				queue.addLast(array[i]);
				numPageFaults++;
				if (queue.size() > num_p_frames) {
					queue.removeFirst();
				}
			}
		}
		System.out.println("");
		for (int j = 0; j < queue.size(); j++) {
			System.out.println("Physical frame " + j + ": " + queue.get(queue.size()-1-j));
		}
			System.out.println("Number of page faults: " + numPageFaults);
	}
	// called this method when option 5 is selected.
	private void simulateOPT (char[] array) {
		ArrayList list = new ArrayList(); // physical frames
		int numPageFaults = 0;
		for (int i = 0; i < array.length; i++) {
			if (list.contains(array[i])) {
			} else if (list.size() < num_p_frames) {
				list.add(array[i]);
				numPageFaults++;
			} else {
				LinkedList queue = new LinkedList(); // priority queue for victims
				for (int j = i; j < array.length; j++) {
					queue.addLast(array[j]);
				}
				int count = 0;
				for (int k = 0; k < queue.size(); k++) {
					if (list.contains(queue.get(k))) {
						count++;
						if (count >= num_p_frames) {
								list.add(list.indexOf(queue.get(k)), array[i]);
								list.remove(queue.get(k));	
								break;								
						}
					}
				}
				if (count == 0) {
					if (queue.size() == 1) {
						list.remove(0);
						list.add(0, array[i]);
						break;
					}

				}
				numPageFaults++;
			}
		}
		System.out.println("");
		for (int i = 0; i < list.size(); i++) {
			System.out.println("Physical frame " + i + ": " + list.get(i));
		}
		System.out.println("Number of page faults: " + numPageFaults);
	}
	// called this method when option 6 is selected.
	private void simulateLRU (char[] array) {
		ArrayList list = new ArrayList(); // physical frames
		int numPageFaults = 0;
		for (int i = 0; i < array.length; i++) {
			if (list.contains(array[i])) {
			} else if (list.size() < num_p_frames) {
				list.add(array[i]);
				numPageFaults++;
			} else {
				LinkedList queue = new LinkedList(); // priority queue for victims
				for (int j = 0; j < i; j++) {
					queue.addFirst(array[j]);
				}
				int count = 0;
				for (int k = 0; k < queue.size(); k++) {
					if (list.contains(queue.get(k))) {
						count++;
						if (count >= num_p_frames) {
								list.add(list.indexOf(queue.get(k)), array[i]);
								list.remove(queue.get(k));
								break;
						}
					}
				}
				if (count == 0) {
					if (queue.size() == 1) {
						list.add(0, array[i]);
						list.remove(0);
						break;
					}
				}
				numPageFaults++;
			}
		}
		System.out.println("");
		for (int i = 0; i < list.size(); i++) {
			System.out.println("Physical frame " + i + ": " + list.get(i));
		}
		System.out.println("Number of page faults: " + numPageFaults);
	}
	// called this method when option 7 is selected.
	private void simulateLFU (char[] array) {
		ArrayList list = new ArrayList(); // physical frames
		int numPageFaults = 0;
		for (int i = 0; i < array.length; i++) {
			if (list.contains(array[i])) {
			} else if (list.size() < num_p_frames) {
				list.add(array[i]);
				numPageFaults++;
			} else {
				LinkedList queue = new LinkedList(); // priority queue for victims
				for (int j = 0; j < i; j++) {
					queue.addFirst(array[j]);
				}
				TreeMap<Object, Integer> map = new TreeMap<Object, Integer>();
				Integer value = 1;
				int count = 0;
				for (int k = 0; k < queue.size(); k++) {
					Object key = queue.get(k);
					if (map.containsKey(key)) {
						value = value + 1;
						map.put(key, value);
					} else {
						map.put(key, value);
					}
				}
				System.out.println(map);
				numPageFaults++;
			}
		}
		for (int i = 0; i < list.size(); i++) {
			System.out.println("Physical frame " + i + ": " + list.get(i));
		}
		System.out.println("Number of page faults: " + numPageFaults);
	}
}