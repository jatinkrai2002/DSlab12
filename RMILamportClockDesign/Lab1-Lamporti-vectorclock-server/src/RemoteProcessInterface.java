import java.rmi.*;
/* lab 1
#VectorClock Interface: This interface defines methods for managing vector clocks.
# Name: Jatin K rai
#DawID
*/

/*
 RemoteProcess Interface: This interface defines methods for processes to interact with each other. Methods can include:
• getVectorClock(): Returns the local vector clock value (using VectorClock interface).
• sendEvent(eventId, data): Sends an event with data to another process.
 * 
 * 
 */

 /*
 * Externd remote is for Unicast
 * • Use UnicastRemoteObject to create remote objects for VectorClock and RemoteProcess.
• Use the Naming class to register these remote objects on the RMI registry.
 * 
 */
public interface RemoteProcessInterface extends Remote
{
    LabLamportivectorclockOutputMessage getVectorClock(LabLamportivectorclockInputMessage inputMessage) throws RemoteException;
    LabLamportivectorclockOutputMessage sendEvent(LabLamportivectorclockInputMessage inputMessage) throws RemoteException;
}