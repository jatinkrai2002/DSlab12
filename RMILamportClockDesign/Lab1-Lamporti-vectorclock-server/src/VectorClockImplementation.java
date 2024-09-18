
/* lab 1
#VectorClock Interface: This interface defines methods for managing vector clocks.
# Name: Jatin K rai
#DawID
*/
/*
 * VectorClock Implementation: This class implements the VectorClock interface. 
 * It maintains an internal array representing the vector clock values for each process.
 * It defines implementations for the methods mentioned above.
• getClock(): Returns the internal clock array.
• increment(processId): Increments the value at the index corresponding to the process ID.
• update(remoteClock): Compares each element of the remote clock with the local clock and 
 updates the local clock by taking the maximum value for each element.
• merge(clock1, clock2) (optional): This method takes two vector clocks and returns a 
  new clock with the maximum value for each element from both clocks.

 * 
 */

public class VectorClockImplementation implements VectorClockInterface
{
	private static int logicalClock = 0;
	private static String processId = "P1";

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
	

	//Returns the internal clock array. 
	public LabLamportivectorclockOutputMessage getClock(LabLamportivectorclockInputMessage inputMessage)
	{
		//this.logical clock value will increase
		ReceiveMessage(inputMessage.processId, inputMessage.logicalClock);
      
		//Update the value to send back to sender process.
		//These values are place holders to use data
		int[] cparam = new int[3];
		cparam[0] = inputMessage.aparam;
		cparam[1] = inputMessage.bparam;
		cparam[2] = inputMessage.logicalClock;

	//	NewEvent("GetClock"); 

	//Increase logical clock for sending to the other process.
		SendMessage(inputMessage.processId);
		LabLamportivectorclockOutputMessage outputMessage = new LabLamportivectorclockOutputMessage(cparam, this.processId, this.logicalClock);
		return outputMessage;
	}
	
	//Increments the value at the index corresponding to the process ID. 
	public LabLamportivectorclockOutputMessage Increment(LabLamportivectorclockInputMessage inputMessage)
	{
		ReceiveMessage(inputMessage.processId, inputMessage.logicalClock);
		//These values are place holders to use data
		int[] cparam = new int[3];
		cparam[0] = inputMessage.aparam;
		cparam[1] = inputMessage.bparam;
		cparam[2] = inputMessage.logicalClock + 1; // increment the clock value of the same process.

		//incrment the clock value with the process given.
		this.logicalClock = this.logicalClock + inputMessage.logicalClock;
	//	NewEvent("Increment");
	//Increase logical clock for sending to the other process.
		SendMessage(inputMessage.processId);
		LabLamportivectorclockOutputMessage outputMessage = new LabLamportivectorclockOutputMessage(cparam, this.processId, this.logicalClock);
		return outputMessage;
	}
	
	// Compares each element of the remote clock with the local clock and updates the 
    // local clock by taking the maximum value for each element. 

	public LabLamportivectorclockOutputMessage Update(LabLamportivectorclockInputMessage inputMessage)
	{
		ReceiveMessage(inputMessage.processId, inputMessage.logicalClock);
		//These values are place holders to use data
		int[] cparam = new int[3];
		cparam[0] = inputMessage.aparam;
		cparam[1] = inputMessage.bparam;
		cparam[2] = inputMessage.logicalClock;

		int maxClock = max(inputMessage.logicalClock, this.logicalClock);
		int maxlogicalClock = max (maxClock, cparam[2]);
	
		this.logicalClock = maxlogicalClock;

	//	NewEvent("Update");
		//Increase logical clock for sending to the other process.
		SendMessage(inputMessage.processId);
		LabLamportivectorclockOutputMessage outputMessage = new LabLamportivectorclockOutputMessage(cparam, this.processId, this.logicalClock);
		return outputMessage;
	}
	
	//This method takes two vector clocks and returns a new clock with the 
	//maximum value for each element from both clocks. 
	public LabLamportivectorclockOutputMessage Merge(LabLamportivectorclockInputMessage inputMessageClock1, LabLamportivectorclockInputMessage inputMessageClock2 )
	{
		//These values are place holders to use data
		ReceiveMessage(inputMessageClock1.processId, inputMessageClock1.logicalClock);
		ReceiveMessage(inputMessageClock2.processId, inputMessageClock2.logicalClock);
		int[] cparam1 = new int[3];
		cparam1[0] = inputMessageClock1.aparam;
		cparam1[1] = inputMessageClock1.bparam;
		cparam1[2] = inputMessageClock1.logicalClock;

		//These values are place holders to use data
		int[] cparam2 = new int[3];
		cparam2[0] = inputMessageClock2.aparam;
		cparam2[1] = inputMessageClock2.bparam;
		cparam2[2] = inputMessageClock2.logicalClock;

		//These values are place holders to use data
		int[] mergeredcparam = new int[3];
		mergeredcparam[0] = max(cparam1[0], cparam2[0]);
		mergeredcparam[1] = max(cparam1[1], cparam2[1]);
		mergeredcparam[2] = max(cparam1[2], cparam2[2]);

		//maximum of the clock value
		this.logicalClock = max( mergeredcparam[2], this.logicalClock);

		//	NewEvent("Merge");
		LabLamportivectorclockOutputMessage outputMessage = new LabLamportivectorclockOutputMessage(mergeredcparam, this.processId, this.logicalClock);

		//Check for concurrency of the clock
		NewEvent("Concurrency Test for two clock");	// event create registry
		EventOrdering ordering = compareClocks(inputMessageClock1, inputMessageClock2);

		//Increase logical clock for sending to the other process.
		SendMessage(inputMessageClock1.processId);
		
		return outputMessage;
	}
	/*
	 * private methods and attributes that serve us to implement the algorithm, i.e., ReceiveMessage, SendMessage, NewEvent, max, logicalClock, processId.
	 */
	
	private void ReceiveMessage(String processId, int senderLogicalClock)
	{
		//this logical clock increase
		NewEvent("Receive message");	// receive a message is an event
		//this logical clock increased.
      // No need to take maximum value
	  // int maxLogicalClock = max(this.logicalClock, senderLogicalClock);
	  // this.logicalClock = maxLogicalClock + 1;
		System.out.println("Message received from process: " + processId + " (logical clock:" + senderLogicalClock
				+ ")\tLocal logical clock: " + logicalClock +  " with  logical vector clock value (" + senderLogicalClock + "," + logicalClock + ",0)");
	}
	
	private int max(int aparam, int bparam)
	{
		if (aparam > bparam)
			return aparam;
		else
			return bparam;
	}
	
	//This function is for more support to make events
	private void SendMessage(String processId)
	{
		NewEvent("Send message");	// send a message is an event
		System.out.println("Message send to process: " + processId + "\tLocal logical clock: " + this.logicalClock + " : EventOrdering.HAPPENS_AFTER");
	}
	
	public void NewEvent(String event)
	{
		this.logicalClock++;
		//bug - //System.out.println("Initial connection Internal Event:" + event + " for process id " + this.processId + "\t Local logical clock: (0," + this.logicalClock + ",0)");
	}

	/**
   * Compare two vector clocks and return:<br/>
   * 
   * 1. IDENTICAL if the count and values all match<br/>
   * 2. HAPPENS_BEFORE if all tstamps of clockOne happen before those of clockTwo<br/>
   * 3. HAPPENS_AFTER if all tstamps of clockOne happen after those of clockTwo<br/>
   * 4. CONCURRENT if some tstamps of clockOne and clockTwo are reverse ordered<br/>
   * 5. NOT_COMPARABLE otherwise
   */

  public static EventOrdering compareClocks(LabLamportivectorclockInputMessage inputMessageClock1, LabLamportivectorclockInputMessage inputMessageClock2 ) {
    
	EventOrdering ordering = null;
    
	if (inputMessageClock1 == null || inputMessageClock2 == null) {
      throw new IllegalArgumentException("Cannot compare null vector clocks");
    }

	// got here, so nodes and sizes are identical - let's iterate and compare each tstamp
    boolean logicaloneAfterTwo = false;
    boolean logicaltwoAfterOne = false;
    boolean concurrent = false;

	if (inputMessageClock1.logicalClock < inputMessageClock2.logicalClock)
	{
	    logicaltwoAfterOne = true;
		ordering =EventOrdering.HAPPENS_BEFORE;
	}
	else if (inputMessageClock1.logicalClock > inputMessageClock2.logicalClock)
	{
		logicaloneAfterTwo = true;
		ordering = EventOrdering.HAPPENS_AFTER;
	}
	else // equal
	{
		ordering = EventOrdering.IDENTICAL;
	}

	if  (logicaltwoAfterOne == true)
	{
	   //Makesure all other clock value of 2 should be less or equal to one otherwise concurrent
		if  ((inputMessageClock1.aparam <= inputMessageClock2.aparam ) && (inputMessageClock1.bparam <= inputMessageClock2.bparam ))
		{
			ordering =EventOrdering.HAPPENS_BEFORE;
		}
		else
		{
			concurrent = true;
			ordering = EventOrdering.CONCURRENT;
		}
	}
	
	if (logicaloneAfterTwo == true)
	{
	 //Makesure all other clock value of 1 should be greater or equal to two otherwise concurrent
		if  ((inputMessageClock1.aparam >= inputMessageClock2.aparam ) && (inputMessageClock1.bparam >= inputMessageClock2.bparam ))
		{
			ordering = EventOrdering.HAPPENS_AFTER;
		}
		else
		{
			ordering = EventOrdering.CONCURRENT;
			System.out.println(String.format("Both process %s and %s are Concurrent and system message is %s", inputMessageClock1.processId, inputMessageClock2.processId, ordering));
		}
	}
    return ordering;
  }
}
