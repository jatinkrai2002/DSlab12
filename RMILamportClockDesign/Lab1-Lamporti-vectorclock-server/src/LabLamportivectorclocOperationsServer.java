import java.rmi.registry.*;
import java.rmi.server.*;

/* lab 1
#VectorClock Interface: This interface defines methods for managing vector clocks.
# Name: Jatin K rai
#DawID
*/

/*
 * Implement RMI functionalities:
• Use UnicastRemoteObject to create remote objects for VectorClock and RemoteProcess.
• Use the Naming class to register these remote objects on the RMI registry.
3. Develop logic for processes:
• Each process creates its own VectorClock object.
• Processes can obtain the vector clock of other processes using RMI calls to getVectorClock().
• When a process sends an event, it updates its local clock, includes the current clock value with the event data, and uses RMI to send the data to the target process.
• The receiving process updates its local clock based on the received clock using the update() method of its VectorClock object.
 * 
 */

public class LabLamportivectorclocOperationsServer
{

	public enum EventOrdering 
	{
		CONCURRENT, HAPPENS_BEFORE, HAPPENS_AFTER, IDENTICAL, NOT_COMPARABLE;
	}

		/*
	 * The main method of the class Server creates the registry, rebind the object with the operations implemented and 
	 * initialize the logical vector clock of the server process to 0.
	 * 
	 * 
	 * 
	 */

	public static void main(String args[])
	{
		// creating instance of vector clock implemented class
		VectorClockImplementation vecoperationsImplementation = new VectorClockImplementation();

		System.out.println("Process: " + vecoperationsImplementation.getProcessId() + "\tLocal logical clock: (0, " +
				vecoperationsImplementation.getLogicalClock() + " , 0) EventOrdering.NOT_COMPARABLE");
		
		// important for the rmi registry location
		String ipaddress = "127.0.0.1"; //localhost
		System.setProperty("java.rmi.server.hostname", ipaddress);
		
		Registry registry = null;
		
		try
		{
			int port = 10908; //only for vector client
			registry = LocateRegistry.createRegistry(port);

			//Local logical clock: 0
			vecoperationsImplementation.NewEvent("Create registry for LabLamportivectorclock");	// event create registry

			//local logical clock = 1
		}
		catch (Exception ex)
		{
			System.out.println();
			System.out.println("Error: " + ex.toString());
		}
		
		try
		{
			VectorClockInterface skeleton = (VectorClockInterface)UnicastRemoteObject.exportObject(vecoperationsImplementation, 0);
			
			/*
			 * java.rmi.server.UnicastRemoteObject is used for 
			 * exporting a remote object with Java Remote Method Protocol (JRMP) and obtaining a stub that communicates to the remote object.
			 * 
			 */
			String opname = "VecOperation";
			registry.rebind(opname, skeleton);

			//local logical clock = 1
			vecoperationsImplementation.NewEvent("Rebind object for LabLamportivectorclock");	// event rebind
			// local logical clock = 2
			
			System.out.println("Process: " + vecoperationsImplementation.getProcessId() + "\tLocal logical clock: (0, " +
			vecoperationsImplementation.getLogicalClock() + " , 0) EventOrdering.NOT_COMPARABLE");
	
			
			System.out.println("LabLamport ivectorclock Server has been started.");
		}
		catch (Exception ex)
		{
			System.out.println("Error: " + ex.toString());
		}
		
		return;
	}
}
