import java.rmi.registry.*;
import java.rmi.server.*;

/* lab 1
# Remote Process Server Interface: This interface defines methods for managing Remote Process clocks.
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


public class LabLamportRemoteProcessOperationsServer
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
		  
		// creating instance of implemented class
		RemoteProcessImplementation remoteprocessImplementation = new RemoteProcessImplementation();
		System.out.println("Process: " + remoteprocessImplementation.getProcessId() + "\tLocal logical clock: (0, " +
			remoteprocessImplementation.getLogicalClock() + " , 0) EventOrdering.NOT_COMPARABLE");

		
		// important for the rmi registry location
		String ipaddress = "127.0.0.1"; //localhost
		System.setProperty("java.rmi.server.hostname", ipaddress);
		
		Registry registry = null;
		
		try
		{
			int port = 10909; //remote process only
			registry = LocateRegistry.createRegistry(port);
			remoteprocessImplementation.NewEvent("Create registry for LabLamportRemoteProcess");	// event create registry
		}
		catch (Exception ex)
		{
			System.out.println();
			System.out.println("Error: " + ex.toString());
		}
		
		try
		{
			RemoteProcessInterface skeleton = (RemoteProcessInterface)UnicastRemoteObject.exportObject(remoteprocessImplementation, 0);
			
			/*
			 * java.rmi.server.UnicastRemoteObject is used for 
			 * exporting a remote object with Java Remote Method Protocol (JRMP) and obtaining a stub that communicates to the remote object.
			 * 
			 */
			String opname = "RemoteOperation";
			registry.rebind(opname, skeleton);
			remoteprocessImplementation.NewEvent("Rebind object");	// event rebind

			System.out.println("Process: " + remoteprocessImplementation.getProcessId() + "\tLocal logical clock: (0, " +
			remoteprocessImplementation.getLogicalClock() + " , 0) EventOrdering.NOT_COMPARABLE");
			
			System.out.println("LabLamport RemoteProcess Server has been started.");
		}
		catch (Exception ex)
		{
			System.out.println("Error: " + ex.toString());
		}
		
		return;
	}
}
