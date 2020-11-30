package FiBu.client;

import FiBu.shared.OrderTransfer;

import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RunClient {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        final RMIClient client = new RMIClient();
        client.startClient();

        while (true) {

            final String handlebarType = "Flatbarlenker";
            final String handlebarMaterial = "Aluminium";
            final String handlebarGearshift = "Nabenschaltung";
            final String handleMaterial = "Schaumstoffgriff";
            final String deliveryDate = "29.11.2020";
            final Integer price = 300;
            final BigInteger orderId = BigInteger.valueOf(123);

            // part zum integrieren
            try {
                final OrderTransfer orderTransfer = client.getServer()
                    .order(handlebarType, handlebarMaterial, handlebarGearshift, handleMaterial, orderId, price, deliveryDate);
                System.out.println("Result > " + orderTransfer);
            } catch (final Exception e) {
                System.out.println("Error > " + e.getMessage());
            }
            break;
        }
    }
}
