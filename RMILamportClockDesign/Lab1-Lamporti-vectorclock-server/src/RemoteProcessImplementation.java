
/* lab 1
# RemoteProcessImplementation Interface: This interface defines methods for managing vector clocks.
# Name: Jatin K rai
#DawID
*/

/*
 * RemoteProcess Implementation: This class implements the RemoteProcess interface. 
 * It maintains a reference to its own VectorClock object and 
 * provides implementations for the methods mentioned above.
• getVectorClock(): Returns the local VectorClock object.
• sendEvent(eventId, data): Increments the local clock for the current process, 
	sends the event data along with the current vector clock value 
	to the target process using RMI methods.
 * 
 * 
 * 
 */
public class RemoteProcessImplementation implements RemoteProcessInterface
{
	private int logicalClock = 0;
	private String processId = "RP1";

	public enum EventOrdering 
	{
		CONCURRENT, HAPPENS_BEFORE, HAPPENS_AFTER, IDENTICAL, NOT_COMPARABLE;
	}
	
	
	public int getLogicalClock()
	{
	    return this.logicalClock;
	}
	
	public String getProcessId()
	{
	    return this.processId;
	}
	

	//Returns the local vector clock value (using VectorClock interface).
	public LabLamportivectorclockOutputMessage getVectorClock(LabLamportivectorclockInputMessage inputMessage)
	{
		ReceiveMessage(inputMessage.processId, inputMessage.logicalClock);

		//These values are place holders to use data
		int[] cparam = new int[3];
		cparam[0] = inputMessage.aparam;
		cparam[1] = inputMessage.bparam;
		cparam[2] = inputMessage.logicalClock;

	//	NewEvent("GetVectorClock");
	//	SendMessage(inputMessage.processId);

		// creating instance of vector clock implemented class
		VectorClockImplementation vecoperationsImplementation = new VectorClockImplementation();

		System.out.println("Process: " + vecoperationsImplementation.getProcessId() + "\tLocal logical clock: (0, " +
				vecoperationsImplementation.getLogicalClock() + " , 0) EventOrdering.NOT_COMPARABLE");
		
		//Send to other process.
		SendMessage( " Send message: VectorClock getClock method called from Remote Process");
		//Vector clock interface has clock value and return the local clock value.
		LabLamportivectorclockOutputMessage outputMessage = vecoperationsImplementation.getClock(inputMessage);
		ReceiveMessage(inputMessage.processId + " Receive message: VectorClock getClock method return from Vector Clock", inputMessage.logicalClock);

		//This is local clok value
		outputMessage.logicalClock = logicalClock;
		//Increase logical clock for sending to the other process.
		SendMessage(inputMessage.processId);
		return outputMessage;
	}
	
	
	// Sends an event with data to another process. 
	public LabLamportivectorclockOutputMessage sendEvent(LabLamportivectorclockInputMessage inputMessage)
	{
		ReceiveMessage(inputMessage.processId, inputMessage.logicalClock);

		//These values are place holders to use data
		int[] cparam = new int[3];
		cparam[0] = inputMessage.aparam;
		cparam[1] = inputMessage.bparam;
		cparam[2] = inputMessage.logicalClock + 1; // increment the clock value of the same process.

		//	NewEvent("SendEvent");
		//	SendMessage(inputMessage.processId);

		// Update the local clock
		this.logicalClock = inputMessage.logicalClock;

		// creating instance of vector clock implemented class
		VectorClockImplementation vecoperationsImplementation = new VectorClockImplementation();

		System.out.println("Process: " + vecoperationsImplementation.getProcessId() + "\tLocal logical clock: (0, " +
				vecoperationsImplementation.getLogicalClock() + " , 0) EventOrdering.NOT_COMPARABLE");
		
		//Send to other process.
		SendMessage( " Send message: VectorClock Update method called from Remote Process");
		LabLamportivectorclockOutputMessage outputMessage = vecoperationsImplementation.Update(inputMessage);
		ReceiveMessage(inputMessage.processId + " Receive message: VectorClock Update method return from Vector Clock", inputMessage.logicalClock);

		//Increase logical clock for sending to the other process.
		SendMessage(inputMessage.processId);
		
		return outputMessage;
	}
	
	/*
	 * private methods and attributes that serve us to implement the algorithm, i.e., ReceiveMessage, SendMessage, NewEvent, max, logicalClock, processId.
	*/
	

	private void ReceiveMessage(String processId, int senderLogicalClock)
	{
		NewEvent("Receive message");	// receive a message is an event
		System.out.println("Message received from process: " + processId + " (logical clock:" + senderLogicalClock + ")\tLocal logical clock: " + logicalClock +  " with  logical vector clock value (" + senderLogicalClock + "," + logicalClock + ",0)");
	}
	
	
	private void SendMessage(String processId)
	{
		NewEvent("Send message");	// send a message is an event
		System.out.println("Message send to process: " + processId + "\tLocal logical clock: " + this.logicalClock + " : EventOrdering.HAPPENS_AFTER");
	}
	
	public void NewEvent(String event)
	{
		this.logicalClock++;
		//System.out.println("Initial connection Internal Event:" + event + " for process id " + this.processId + "\t Local logical clock: (0," + this.logicalClock + ",0)");
	}
}
