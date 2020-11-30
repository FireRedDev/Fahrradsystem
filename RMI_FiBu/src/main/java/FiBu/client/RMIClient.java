package FiBu.client;

import FiBu.shared.OrderTransferServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

    private OrderTransferServer server;

    public void startClient() throws RemoteException, NotBoundException {
        //connection to server
        final Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        //search server
        server = (OrderTransferServer) registry.lookup("HandlebarConfigServer");
    }

    public OrderTransferServer getServer() {
        return server;
    }
}
