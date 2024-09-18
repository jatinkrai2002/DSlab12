import java.rmi.*;
/* lab 1
#VectorClock Interface: This interface defines methods for managing vector clocks.
# Name: Jatin K rai
#DawID
*/

/*
 VectorClock Interface: This interface defines methods for managing vector clocks. Methods can include:
• getClock(): Returns the current vector clock value.
• increment(processId): Increments the clock value for a specific process.
• update(remoteClock): Updates the local clock based on a received remote clock.
• merge(clock1, clock2) (optional): Merges two vector clocks (used for complex scenarios).
 */
/*
 * Externd remote is for Unicast
 * • Use UnicastRemoteObject to create remote objects for VectorClock and RemoteProcess.
• Use the Naming class to register these remote objects on the RMI registry.
 * 
 */

public interface VectorClockInterface extends Remote
{
    LabLamportivectorclockOutputMessage getClock(LabLamportivectorclockInputMessage inputMessage) throws RemoteException;
    LabLamportivectorclockOutputMessage Increment(LabLamportivectorclockInputMessage inputMessage) throws RemoteException;
    LabLamportivectorclockOutputMessage Update(LabLamportivectorclockInputMessage inputMessage) throws RemoteException;
    LabLamportivectorclockOutputMessage Merge(LabLamportivectorclockInputMessage inputMessageClock1, LabLamportivectorclockInputMessage inputMessageClock2 ) throws RemoteException;
}
