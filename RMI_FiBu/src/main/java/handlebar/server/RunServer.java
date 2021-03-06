package handlebar.server;

import handlebar.shared.HandlebarConfigServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunServer {

    public static void main(final String[] args) throws RemoteException, AlreadyBoundException {
        final HandlebarConfigServer server = new ServerImpl();
        final Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("HandlebarConfigServer", server);
        System.out.println("Server started");
    }
}
