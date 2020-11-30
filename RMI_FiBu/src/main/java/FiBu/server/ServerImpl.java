package FiBu.server;

import FiBu.shared.OrderTransfer;
import FiBu.shared.OrderTransferServer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl implements OrderTransferServer {

    public static final String RENNRADLENKER = "Rennradlenker";
    public static final String FLATBARLENKER = "Flatbarlenker";
    public static final String ALUMINIUM = "Aluminium";
    public static final String KUNSTSTOFF = "Kunststoff";
    public static final String STAHL = "Stahl";
    public static final String KETTENSCHALTUNG = "Kettenschaltung";
    public static final String KUNSTSTOFFGRIFF = "Kunststoffgriff";
    public static final String LEDERGRIFF = "Ledergriff";

    public ServerImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public OrderTransfer order(final String handlebarType, final String handlebarMaterial,
                               final String handlebarGearshift, final String handleMaterial, final BigInteger orderId, final Integer price,
                               final String deliveryDate) throws RemoteException {

        //In Textdatei die übergebenen Werte speichern
        PrintWriter pWriter = null;
        try {
            pWriter = new PrintWriter(new BufferedWriter(new FileWriter("FIBU_Orders.txt", true)));
            pWriter.println("Bestellung " + orderId + " - " + handlebarType + " - " + handlebarMaterial + " - " + handlebarGearshift + " - " + handleMaterial
            + " - " + price + " - " + deliveryDate);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (pWriter != null) {
                pWriter.flush();
                pWriter.close();
            }
        }

        // Was wollen wir hier, dass zurückgegeben wird?
        OrderTransfer transfer = new OrderTransfer(orderId, handlebarType, handlebarMaterial, handlebarGearshift, handleMaterial,
                price, deliveryDate);
        System.out.println(transfer.toString());
        return transfer;
    }
}
