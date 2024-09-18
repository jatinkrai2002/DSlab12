import java.rmi.registry.*;
import java.util.*;

/* lab 1
# Remote Process Interface: This interface defines methods for managing vector clocks.
# Name: Jatin K rai
#DawID
*/

//This is for test automation
// import org.junit.Assert.*


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


public class RemoteProcessOperationsClient
{
	private static int logicalClock = 0;
	private static String processId = "RP0";
	private static int logicalClockTwo = 0;
	private static int aparam = 0;
	private static int bparam = 0;
	
	public enum EventOrdering 
	{
		CONCURRENT, HAPPENS_BEFORE, HAPPENS_AFTER, IDENTICAL, NOT_COMPARABLE;
	}

		/*
	 * The main method of the class Remote Process client process get the registry, rebind the object with the operations implemented and 
	 * initialize the logical vector clock of the client process to 0.
	 * 
	 * 
	 * 
	 */
	public static void main(String args[])
	{
		try
		{

			boolean bNeedProcessName = false; //Default. This is to handle the multiprcoess and multi client.

			if (bNeedProcessName == true)
			{
				System.out.println(" Could you please enter your process name e.g P1 or P2 or P3 or : ");
				Scanner processNameInput = new Scanner(System.in);
				String processName = processNameInput.nextLine();

				if (processName.length() > 0)
				{
					processId = processName;
				}
			//	assertEquals(processId, processName)
			}
			//assertNotNull(processId);

			System.out.println(" Current Local Process: " + processId + "\t has the Local logical vector clock: ( " + logicalClock + ", 0, 0)");
			
			String ipaddress = "127.0.0.1"; //localhost
			int port = 10909; // For remote process
			Registry registry = LocateRegistry.getRegistry(ipaddress, port);

			//Logical clock == 0
			NewEvent("Get RemotePRocessOperationsClient registry");		// event get registry
			//Logical clock increase by 1
			
			/*
			 * java.rmi.server.UnicastRemoteObject is used for 
			 * exporting a remote object with Java Remote Method Protocol (JRMP) and obtaining a stub that communicates to the remote object.
			 * 
			 */
			String opname = "RemoteOperation";
			RemoteProcessInterface stub = (RemoteProcessInterface)registry.lookup(opname);

			NewEvent("LabRemote Stub creation");		// event stub creation
			//Logical clock increase by 1

			LabLamportivectorclockOutputMessage LabLamportivectorclockoutputMessage = new LabLamportivectorclockOutputMessage();
			LabLamportivectorclockInputMessage LabLamportivectorclockinputMessage = new LabLamportivectorclockInputMessage(aparam, bparam, logicalClock, processId, "");
			
			
			while(true)
			{
				System.out.println("Choose an Vector operation: 1 GetVectorClock | 2 Send Event and Data");
				Scanner inputparam = new Scanner(System.in);
				String msg = inputparam.nextLine();
				//input.close();
				//NewEvent("Choose Vector  operation for Remote Process Server");	// event ask for operation
				
				int operation = Integer.parseInt(msg);
				String msdata = "Test data";
				String Receivemessage = "";
				LabLamportivectorclockinputMessage.logicalClock = logicalClock;
				
				switch (operation)
				{
					case 1:
						//Returns the local VectorClock object.
						SendMessage ("Message send to getClock method hold Process"); 
						LabLamportivectorclockinputMessage.logicalClock = logicalClock;
						LabLamportivectorclockoutputMessage = stub.getVectorClock(LabLamportivectorclockinputMessage);
						Receivemessage = " from getVectorClock";
						break;
					case 2:
						{

						// Remore interactive and automate with default message.

					//	Scanner eventparam = new Scanner(System.in);
					//	NewEvent("Provide Event message");	// event ask for operation
					//	String msgevent = eventparam.nextLine();
						//input.close();
					
						//String event = msgevent;
					//	NewEvent("Provide data message");	// event ask for operation
					//	Scanner dataparam = new Scanner(System.in);
					//	String msgdata1 = dataparam.nextLine();
						//input.close();
					
						//Increments the local clock for the current process, sends the event data along 
						//with the current vector clock value to the target process using RMI methods
						
						
						//When a process sends an event, it updates its local clock, includes the current clock value with the event 
						//	data, and uses RMI to send the data to the target process. 
						SendMessage ("Message send to sendEvent method hold Process");
						LabLamportivectorclockInputMessage SendMessage = new LabLamportivectorclockInputMessage(aparam, bparam, logicalClock, processId, msdata);
						LabLamportivectorclockOutputMessage objLockOutPutMessage = stub.sendEvent(SendMessage);
						
						//The receiving process updates its local clock based on the received clock using the update() method of 
						//its VectorClock object
						
						//Update local clock.
						logicalClock = objLockOutPutMessage.logicalClock;
						Receivemessage = " from Sendevent ";

						}
						break;
				}

				//Event from server.
				ReceiveMessage(LabLamportivectorclockoutputMessage.processId + " of " + Receivemessage, LabLamportivectorclockoutputMessage.logicalClock);
				if (operation == 1)
				{
						//Print clock Value
						System.out.println(" Local Logical Vector Clock value:" + processId + "(" + logicalClock +"," + LabLamportivectorclockoutputMessage.logicalClock + "," + logicalClockTwo +")");
				}
				//Final logical clock for current process.
				System.out.println("Logical local vector Clock value for Process id:" + processId + "(" + logicalClock +"," + LabLamportivectorclockoutputMessage.logicalClock + "," + logicalClockTwo +")");
			}
		}
		catch (Exception ex)
		{
			System.out.println("Err: " + ex.toString());
		}
	}
	
	/*
	 * private methods and attributes that serve us to implement the algorithm, i.e., ReceiveMessage, SendMessage, NewEvent, max, logicalClock, processId.
	 */

	//This function is for more support to make events
	private static void SendMessage(String MessageName)
	{
		NewEvent("Send message");	// send a message is an event
		System.out.println( MessageName + " Message send to process: " + processId +" \tLocal logical clock: " + logicalClock + " : EventOrdering.HAPPENS_BEFORE");
	}
	
	private static void ReceiveMessage(String processId, int senderLogicalClock)
	{
		//Logical clock == 3
		NewEvent("Receive message");	// receive a message is an event
		//Logical clock == 4
	//	int maxLogicalClock = max(logicalClock, senderLogicalClock);
	//	logicalClock = maxLogicalClock + 1;
		System.out.println("Message received from process: " + processId + "  with logical vector clock:" + senderLogicalClock
				+ " \tLocal logical vector clock: " + logicalClock + " with logcial vector clock value is (" +  logicalClock + ", " + senderLogicalClock + ",0)" );
	}
	//This function is for more support to make events
	private static int max(int aparam, int bparam)
	{
		if (aparam > bparam)
			return aparam;
		else
			return bparam;
	}
	
	private static void NewEvent(String event)
	{
		logicalClock++;
		System.out.println("Initial connection Internal Event: " + event + " for process id " +  processId + "\t Local logical clock: (" + logicalClock + ", 0, 0)");
	}
}
