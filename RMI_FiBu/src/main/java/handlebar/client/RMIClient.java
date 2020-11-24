package handlebar.client;

import handlebar.shared.HandlebarConfigServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

    private HandlebarConfigServer server;

    public void startClient() throws RemoteException, NotBoundException {
        //connection to server
        final Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        //search server
        server = (HandlebarConfigServer) registry.lookup("HandlebarConfigServer");
    }

    public HandlebarConfigServer getServer() {
        return server;
    }
}
