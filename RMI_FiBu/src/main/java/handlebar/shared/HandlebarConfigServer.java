package handlebar.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface HandlebarConfigServer extends Remote {
    HandlebarConfig order(String handlebarType, String handlebarMaterial, String handlebarGearshift,
        String handleMaterial) throws RemoteException;

    List<String> getAvailableHandlebarTypes() throws RemoteException;

    List<String> getAvailableMaterial(String handlebarType) throws RemoteException;

    List<String> getAvailableGearshifts(String handlebarMaterial) throws RemoteException;

    List<String> getAvailableHandleMaterial(String handlebarType, String handlebarMaterial)
        throws RemoteException;
}
