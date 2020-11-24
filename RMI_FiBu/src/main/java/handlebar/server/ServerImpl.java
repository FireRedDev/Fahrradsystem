package handlebar.server;

import handlebar.shared.HandlebarConfig;
import handlebar.shared.HandlebarConfigServer;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerImpl implements HandlebarConfigServer {

    public static final String RENNRADLENKER = "Rennradlenker";
    public static final String FLATBARLENKER = "Flatbarlenker";
    public static final String ALUMINIUM = "Aluminium";
    public static final String KUNSTSTOFF = "Kunststoff";
    public static final String STAHL = "Stahl";
    public static final String KETTENSCHALTUNG = "Kettenschaltung";
    public static final String KUNSTSTOFFGRIFF = "Kunststoffgriff";
    public static final String LEDERGRIFF = "Ledergriff";
    private final List<String> availableHandlebarTypes;
    private final List<String> availableMaterial;
    private final List<String> availableGearshifts;
    private final List<String> availableHandleMaterial;

    public ServerImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);

        availableHandlebarTypes = new ArrayList<>();
        availableHandlebarTypes.add(FLATBARLENKER);
        availableHandlebarTypes.add(RENNRADLENKER);
        availableHandlebarTypes.add("Bullhornlenker");

        availableMaterial = new ArrayList<>();
        availableMaterial.add(ALUMINIUM);
        availableMaterial.add(STAHL);
        availableMaterial.add(KUNSTSTOFF);

        availableGearshifts = new ArrayList<>();
        availableGearshifts.add(KETTENSCHALTUNG);
        availableGearshifts.add("Nabenschaltung");
        availableGearshifts.add("Tretlagerschaltung");

        availableHandleMaterial = new ArrayList<>();
        availableHandleMaterial.add(LEDERGRIFF);
        availableHandleMaterial.add("Schaumstoffgriff");
        availableHandleMaterial.add(KUNSTSTOFFGRIFF);
    }

    @Override
    public HandlebarConfig order(final String handlebarType, final String handlebarMaterial,
        final String handlebarGearshift, final String handleMaterial) throws RemoteException {
        if (!availableHandlebarTypes.contains(handlebarType)) {
            throw new RemoteException("Configuration wrong: Type of Handlebar " + handlebarType + " is not available.");
        }
        if (!availableMaterial.contains(handlebarMaterial)) {
            throw new RemoteException("Configuration wrong: Material " + handlebarMaterial + " is not available.");
        }
        if (!availableGearshifts.contains(handlebarGearshift)) {
            throw new RemoteException("Configuration wrong: Gearshift " + handlebarGearshift + " is not available.");
        }
        if (!availableHandleMaterial.contains(handleMaterial)) {
            throw new RemoteException("Configuration wrong: Handle " + handleMaterial + " is not available.");
        }

        if (RENNRADLENKER.equalsIgnoreCase(handlebarType)) {
            if (!ALUMINIUM.equalsIgnoreCase(handlebarMaterial) && !KUNSTSTOFF
                .equalsIgnoreCase(handlebarMaterial)) {
                throw new RemoteException(
                    "Configuration wrong: " + RENNRADLENKER + " can only be built with " + ALUMINIUM
                        + " and " + KUNSTSTOFF);
            }
        }

        if (FLATBARLENKER.equalsIgnoreCase(handlebarType)) {
            if (!ALUMINIUM.equalsIgnoreCase(handlebarMaterial) && !KUNSTSTOFF
                .equalsIgnoreCase(handlebarMaterial)) {
                throw new RemoteException(
                    "Configuration wrong: " + FLATBARLENKER + " can only be built with " + ALUMINIUM
                        + " and " + KUNSTSTOFF);
            }
        }
        if (STAHL.equalsIgnoreCase(handlebarMaterial)) {
            if (!KETTENSCHALTUNG.equalsIgnoreCase(handlebarGearshift)) {
                throw new RemoteException(
                    "Configuration wrong: " + STAHL + " can only be combined with "
                        + KETTENSCHALTUNG);
            }
        }
        if (KUNSTSTOFFGRIFF.equalsIgnoreCase(handleMaterial)) {
            if (!KUNSTSTOFF.equalsIgnoreCase(handlebarMaterial)) {
                throw new RemoteException(
                    "Configuration wrong: " + KUNSTSTOFFGRIFF + " can only be combined with "
                        + KUNSTSTOFF);
            }
        }
        if (LEDERGRIFF.equalsIgnoreCase(handleMaterial)) {
            if (!RENNRADLENKER.equalsIgnoreCase(handlebarType)) {
                throw new RemoteException(
                    "Configuration wrong: " + LEDERGRIFF + " can only be combined with "
                        + RENNRADLENKER);
            }
        }
        return new HandlebarConfig(BigInteger.probablePrime(8, new Random()), handlebarType,
            handlebarMaterial, handlebarGearshift, handleMaterial);
    }

    @Override public List<String> getAvailableHandlebarTypes() throws RemoteException {
        return availableHandlebarTypes;
    }

    @Override public List<String> getAvailableMaterial(final String handlebarType)
        throws RemoteException {
        return availableMaterial;
    }

    @Override public List<String> getAvailableGearshifts(final String handlebarMaterial)
        throws RemoteException {
        return availableGearshifts;
    }

    @Override public List<String> getAvailableHandleMaterial(final String handlebarType,
        final String handlebarMaterial) throws RemoteException {
        return availableHandleMaterial;
    }
}
