import java.rmi.registry.*;
import java.util.*;


//This is for test automation
// import org.junit.Assert.*




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


public class LabLamportivectorclockOperationsClient
{

  
	private static int logicalClock = 0;
	private static String processId = "P0";
	private static String processTwoId = "P2";
	private static int logicalClockTwo = 0;
	private static int aparam = 0;
	private static int bparam = 0;
	private static int aparamTwo= 0;
	private static int bparamTwo = 0;

	public enum EventOrdering 
	{
		CONCURRENT, HAPPENS_BEFORE, HAPPENS_AFTER, IDENTICAL, NOT_COMPARABLE;
	}
	
	/*
	 * The main method of the class OperationsServer creates the registry, rebind the object with the operations implemented and 
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
			int port = 10908; //vector only
			Registry registry = LocateRegistry.getRegistry(ipaddress, port);

			//Logical clock == 0
			NewEvent("Get LabLamport vectorclock registry");		// event get registry
			//Logical clock increase by 1
			
			/*
			 * java.rmi.server.UnicastRemoteObject is used for 
			 * exporting a remote object with Java Remote Method Protocol (JRMP) and obtaining a stub that communicates to the remote object.
			 * 
			 */
			String opname = "VecOperation";
			VectorClockInterface stub = (VectorClockInterface)registry.lookup(opname);

			//Logical clock == 1
			NewEvent("LabLamport vectorclock Stub creation");		// event stub creation
			//Logical clock increase by 1


			//Create output and input objects for communications.
			LabLamportivectorclockOutputMessage LabLamportivectorclockoutputMessage = new LabLamportivectorclockOutputMessage();
			LabLamportivectorclockInputMessage LabLamportivectorclockinputMessage = new LabLamportivectorclockInputMessage(aparam, bparam, logicalClock, processId, "");
			LabLamportivectorclockInputMessage LabLamportivectorclockinputMessage2 = new LabLamportivectorclockInputMessage(aparamTwo, bparamTwo, logicalClockTwo, processTwoId, "");
			
			//Two events already happened.
			//Logical clock == 2
			System.out.println("Local  Process Id : " + processId + " with intennal event and logical vector clock (" + logicalClock + ", 0, 0 )" );
			

			
			while(true)
			{
				System.out.println("Choose an Vector operation: 1 ClockArray | 2 Increment Vector Clock| 3 Update Vector Clock | 4 MergeVector clock");
				Scanner inputparam = new Scanner(System.in);
				String msg = inputparam.nextLine();
				//input.close();
				//Logical clock == 2
				NewEvent("Choose Vector  Operation on the Server Process");	// event ask for operation
				//Logical clock == 3
				
				int operation = Integer.parseInt(msg);
				
			    String Receivemessage = "";
			//Logical clock == 3
				LabLamportivectorclockinputMessage.logicalClock = logicalClock;
				
				switch (operation)
				{
					case 1:
						SendMessage ("Message send to getClock method hold Process");
						LabLamportivectorclockoutputMessage = stub.getClock(LabLamportivectorclockinputMessage);
						Receivemessage = " from getClock";
						break;
					case 2:
						SendMessage ("Message send to Increment method hold Process");
						LabLamportivectorclockoutputMessage = stub.Increment(LabLamportivectorclockinputMessage);
						Receivemessage = " from Increment";
						break;
					case 3:
						SendMessage ("Message send to Update method hold Process");
						LabLamportivectorclockoutputMessage = stub.Update(LabLamportivectorclockinputMessage);
						Receivemessage = " from Update";
						break;
					case 4:
						SendMessage ("Message send to Merge method hold Process");
						LabLamportivectorclockoutputMessage = stub.Merge(LabLamportivectorclockinputMessage, LabLamportivectorclockinputMessage2);
						Receivemessage = " from Merge";
						break;
				}
				
				//Logical clock == 3 for sender
				ReceiveMessage(LabLamportivectorclockoutputMessage.processId + " of " + Receivemessage, LabLamportivectorclockoutputMessage.logicalClock); // process id and sender clock
			
				if (operation == 1)
				{
					//Print clock array
					System.out.println("Logical Clock array are:" + processId + "(" + logicalClock +"," + LabLamportivectorclockoutputMessage.logicalClock + "," + logicalClockTwo +")");
					System.out.println("Logical Clock array are:" + LabLamportivectorclockoutputMessage.processId + "(" + logicalClock +"," + LabLamportivectorclockoutputMessage.logicalClock + "," + logicalClockTwo +")");
					System.out.println("Logical Clock array are:" + processTwoId + "(" + logicalClock +"," + LabLamportivectorclockoutputMessage.logicalClock + "," + logicalClockTwo +")");
				}
				
				//Final logical clock for current process.
				System.out.println("Logical local vector Clock value for Process id:" + processId + "(" + logicalClock +"," + LabLamportivectorclockoutputMessage.logicalClock + "," + logicalClockTwo +")");
			}
		//assertEquals();
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
