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

public class LabLamportivectorclockOutputMessage implements Serializable
{
	private static final long serialVersionUID = 1L;
	int[] cparam;
	String processId;
	int logicalClock;
	
	public LabLamportivectorclockOutputMessage() {}
}
