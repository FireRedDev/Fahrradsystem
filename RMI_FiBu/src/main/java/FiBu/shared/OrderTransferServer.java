package FiBu.shared;

import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OrderTransferServer extends Remote {
    OrderTransfer order(String type, String handlebarType, String handlebarMaterial, String handlebarGearshift,
                        BigInteger orderId, Integer price, String handleMaterial) throws RemoteException;
}
