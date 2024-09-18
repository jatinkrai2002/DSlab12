import java.io.Serializable;

/* lab 1
#VectorClock Interface: This interface defines methods for managing vector clocks.
# Name: Jatin K rai
#DawID
*/

/* 
 * Data to synchronize and serailize.
 * Distributed synchronization: RMI facilitates communication and clock updates between processes.
• Scalability: The design allows for adding more processes without significant changes.
  Serialization of objects for RMI communication, and thread synchronization for concurrent access to vector clocks.
 */

public class LabLamportivectorclockInputMessage implements Serializable
{
	//These values are place holders to use data
	private static final long serialVersionUID = 1L;
	int aparam;
	int bparam;
	int logicalClock;
	String processId;
	String data;
	
	public LabLamportivectorclockInputMessage(int aparam, int bparam, int logicalClock, String processId, String data)
	{
		//These values are place holders to use data
		this.aparam = aparam;
		this.bparam = bparam;
		this.logicalClock = logicalClock;
		this.processId = processId;
		this.data = data;
	}
}
