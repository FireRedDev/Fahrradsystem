package handlebar.client;

import handlebar.shared.HandlebarConfig;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class RunClient {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        final RMIClient client = new RMIClient();
        client.startClient();

        final Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("Choose type of handlebar. Available values are:");
            client.getServer().getAvailableHandlebarTypes().forEach(System.out::println);
            System.out.println("Input >");
            final String handlebarType = in.nextLine();

            System.out.println("Choose type of material. Available values are:");
            client.getServer().getAvailableMaterial(handlebarType).forEach(System.out::println);
            System.out.println("Input >");
            final String handlebarMaterial = in.nextLine();

            System.out.println("Choose type of gearshift. Available values are:");
            client.getServer().getAvailableGearshifts(handlebarMaterial)
                .forEach(System.out::println);
            System.out.println("Input >");
            final String handlebarGearshift = in.nextLine();

            System.out.println("Choose type of handle material. Available values are:");
            client.getServer().getAvailableHandleMaterial(handlebarType, handlebarMaterial)
                .forEach(System.out::println);
            System.out.println("Input >");
            final String handleMaterial = in.nextLine();

            try {
                final HandlebarConfig handlebarConfig = client.getServer()
                    .order(handlebarType, handlebarMaterial, handlebarGearshift, handleMaterial);
                System.out.println("Result > " + handlebarConfig);
            } catch (final Exception e) {
                System.out.println("Error > " + e.getMessage());
            }
        }
    }
}
