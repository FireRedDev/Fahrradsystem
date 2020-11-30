package at.supplier.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class HandlebarSupplierController {

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

    public HandlebarSupplierController() {
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

    @PostMapping("/supplier1/order/{1}/{2}/{3}/{4}")
    public ResponseEntity<HandlebarSupplier> supplier1Order(@PathVariable("1") final String handlebarType,
                                                   @PathVariable("2") final String handlebarMaterial,
                                                   @PathVariable("3") final String handlebarGearshift,
                                                   @PathVariable("4") final String handleMaterial) {

        int price = 0;

        switch (handlebarType) {
            case FLATBARLENKER:
                price += Math.floor(Math.random() * 10) + 15;
                break;
            case RENNRADLENKER:
                price += Math.floor(Math.random() * 40) + 60;
                break;
            case "Bullhornlenker":
                price += Math.floor(Math.random() * 10) + 25;
                break;
        }

        switch (handlebarMaterial) {
            case ALUMINIUM:
                price += Math.floor(Math.random() * 30) + 70;
                break;
            case STAHL:
                price += Math.floor(Math.random() * 20) + 40;
                break;
            case KUNSTSTOFF:
                price += Math.floor(Math.random() * 10) + 20;
                break;
        }

        switch (handlebarGearshift) {
            case KETTENSCHALTUNG:
                price += Math.floor(Math.random() * 50) + 100;
                break;
            case "Nabenschaltung":
                price += Math.floor(Math.random() * 75) + 150;
                break;
            case "Tretlagerschaltung":
                price += Math.floor(Math.random() * 100) + 250;
                break;
        }

        switch (handleMaterial) {
            case LEDERGRIFF:
                price += Math.floor(Math.random() * 20) + 30;
                break;
            case "Schaumstoffgriff":
                price += Math.floor(Math.random() * 10) + 5;
                break;
            case KUNSTSTOFFGRIFF:
                price += Math.floor(Math.random() * 10) + 10;
                break;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + ThreadLocalRandom.current().nextInt(5, 14));
        Date deliveryDate = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String deliveryDateFormatted = dateFormat.format(deliveryDate);


        if (!availableHandlebarTypes.contains(handlebarType)) {
            return getBadRequestResponseEntity(handlebarType, handlebarMaterial, handlebarGearshift,
                handleMaterial,
                "Configuration wrong: Type of Handlebar " + handlebarType + " is not available.");
        }
        if (!availableMaterial.contains(handlebarMaterial)) {
            return getBadRequestResponseEntity(handlebarType, handlebarMaterial, handlebarGearshift,
                handleMaterial,
                "Configuration wrong: Material " + handlebarMaterial + " is not available.");
        }
        if (!availableGearshifts.contains(handlebarGearshift)) {
            return getBadRequestResponseEntity(handlebarType, handlebarMaterial, handlebarGearshift,
                handleMaterial, "Configuration wrong: Type of gearshift " + handlebarGearshift
                    + " is not available.");
        }
        if (!availableHandleMaterial.contains(handleMaterial)) {
            return getBadRequestResponseEntity(handlebarType, handlebarMaterial, handlebarGearshift,
                handleMaterial,
                "Configuration wrong: Type of handle " + handleMaterial + " is not available.");
        }

        if (RENNRADLENKER.equalsIgnoreCase(handlebarType)) {
            if (!ALUMINIUM.equalsIgnoreCase(handlebarMaterial) && !KUNSTSTOFF
                .equalsIgnoreCase(handlebarMaterial)) {
                return getBadRequestResponseEntity(handlebarType, handlebarMaterial,
                    handlebarGearshift, handleMaterial,
                    "Configuration invalid: " + handlebarType + " is not compatible with material "
                        + handlebarMaterial);
            }
        }
        if (FLATBARLENKER.equalsIgnoreCase(handlebarType)) {
            if (!ALUMINIUM.equalsIgnoreCase(handlebarMaterial) && !KUNSTSTOFF
                .equalsIgnoreCase(handlebarMaterial)) {
                return getBadRequestResponseEntity(handlebarType, handlebarMaterial,
                    handlebarGearshift, handleMaterial,
                    "Configuration invalid: " + handlebarType + " is not compatible with material "
                        + handlebarMaterial);
            }
        }

        if (STAHL.equalsIgnoreCase(handlebarMaterial)) {
            if (!KETTENSCHALTUNG.equalsIgnoreCase(handlebarGearshift)) {
                return getBadRequestResponseEntity(handlebarType, handlebarMaterial,
                    handlebarGearshift, handleMaterial,
                    "Configuration invalid: " + handlebarMaterial
                        + " is not compatible with gearshift " + handlebarGearshift);
            }
        }

        if (KUNSTSTOFFGRIFF.equalsIgnoreCase(handleMaterial)) {
            if (!KUNSTSTOFF.equalsIgnoreCase(handlebarMaterial)) {
                return getBadRequestResponseEntity(handlebarType, handlebarMaterial,
                    handlebarGearshift, handleMaterial, "Configuration invalid: " + handleMaterial
                        + " is not compatible with material of handlebar " + handlebarMaterial);
            }
        }
        if (LEDERGRIFF.equalsIgnoreCase(handleMaterial)) {
            if (!RENNRADLENKER.equalsIgnoreCase(handlebarType)) {
                return getBadRequestResponseEntity(handlebarType, handlebarMaterial,
                    handlebarGearshift, handleMaterial, "Configuration invalid: " + handleMaterial
                        + " is not compatible with material of handlebar " + handlebarMaterial);
            }
        }



        final Random rnd = new Random();
        final HandlebarSupplier handlebarSupplier =
                new HandlebarSupplier(BigInteger.probablePrime(8, rnd), price, deliveryDateFormatted);
        return ResponseEntity.ok(handlebarSupplier);
    }

    @PostMapping("/supplier2/order/{1}/{2}/{3}/{4}")
    public ResponseEntity<HandlebarSupplier> supplier2Order(@PathVariable("1") final String handlebarType2,
                                                   @PathVariable("2") final String handlebarMaterial2,
                                                   @PathVariable("3") final String handlebarGearshift2,
                                                   @PathVariable("4") final String handleMaterial2) {

        int price = 0;

        switch (handlebarType2) {
            case FLATBARLENKER:
                price += Math.floor(Math.random() * 10) + 15;
                break;
            case RENNRADLENKER:
                price += Math.floor(Math.random() * 40) + 60;
                break;
            case "Bullhornlenker":
                price += Math.floor(Math.random() * 10) + 25;
                break;
        }

        switch (handlebarMaterial2) {
            case ALUMINIUM:
                price += Math.floor(Math.random() * 30) + 70;
                break;
            case STAHL:
                price += Math.floor(Math.random() * 20) + 40;
                break;
            case KUNSTSTOFF:
                price += Math.floor(Math.random() * 10) + 20;
                break;
        }

        switch (handlebarGearshift2) {
            case KETTENSCHALTUNG:
                price += Math.floor(Math.random() * 50) + 100;
                break;
            case "Nabenschaltung":
                price += Math.floor(Math.random() * 75) + 150;
                break;
            case "Tretlagerschaltung":
                price += Math.floor(Math.random() * 100) + 250;
                break;
        }

        switch (handleMaterial2) {
            case LEDERGRIFF:
                price += Math.floor(Math.random() * 20) + 30;
                break;
            case "Schaumstoffgriff":
                price += Math.floor(Math.random() * 10) + 5;
                break;
            case KUNSTSTOFFGRIFF:
                price += Math.floor(Math.random() * 10) + 10;
                break;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + ThreadLocalRandom.current().nextInt(5, 14));
        Date deliveryDate = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String deliveryDateFormatted = dateFormat.format(deliveryDate);


        if (!availableHandlebarTypes.contains(handlebarType2)) {
            return getBadRequestResponseEntity(handlebarType2, handlebarMaterial2, handlebarGearshift2,
                    handleMaterial2,
                    "Configuration wrong: Type of Handlebar " + handlebarType2 + " is not available.");
        }
        if (!availableMaterial.contains(handlebarMaterial2)) {
            return getBadRequestResponseEntity(handlebarType2, handlebarMaterial2, handlebarGearshift2,
                    handleMaterial2,
                    "Configuration wrong: Material " + handlebarMaterial2 + " is not available.");
        }
        if (!availableGearshifts.contains(handlebarGearshift2)) {
            return getBadRequestResponseEntity(handlebarType2, handlebarMaterial2, handlebarGearshift2,
                    handleMaterial2, "Configuration wrong: Type of gearshift " + handlebarGearshift2
                            + " is not available.");
        }
        if (!availableHandleMaterial.contains(handleMaterial2)) {
            return getBadRequestResponseEntity(handlebarType2, handlebarMaterial2, handlebarGearshift2,
                    handleMaterial2,
                    "Configuration wrong: Type of handle " + handleMaterial2 + " is not available.");
        }

        if (RENNRADLENKER.equalsIgnoreCase(handlebarType2)) {
            if (!ALUMINIUM.equalsIgnoreCase(handlebarMaterial2) && !KUNSTSTOFF
                    .equalsIgnoreCase(handlebarMaterial2)) {
                return getBadRequestResponseEntity(handlebarType2, handlebarMaterial2,
                        handlebarGearshift2, handleMaterial2,
                        "Configuration invalid: " + handlebarType2 + " is not compatible with material "
                                + handlebarMaterial2);
            }
        }
        if (FLATBARLENKER.equalsIgnoreCase(handlebarType2)) {
            if (!ALUMINIUM.equalsIgnoreCase(handlebarMaterial2) && !KUNSTSTOFF
                    .equalsIgnoreCase(handlebarMaterial2)) {
                return getBadRequestResponseEntity(handlebarType2, handlebarMaterial2,
                        handlebarGearshift2, handleMaterial2,
                        "Configuration invalid: " + handlebarType2 + " is not compatible with material "
                                + handlebarMaterial2);
            }
        }

        if (STAHL.equalsIgnoreCase(handlebarMaterial2)) {
            if (!KETTENSCHALTUNG.equalsIgnoreCase(handlebarGearshift2)) {
                return getBadRequestResponseEntity(handlebarType2, handlebarMaterial2,
                        handlebarGearshift2, handleMaterial2,
                        "Configuration invalid: " + handlebarMaterial2
                                + " is not compatible with gearshift " + handlebarGearshift2);
            }
        }

        if (KUNSTSTOFFGRIFF.equalsIgnoreCase(handleMaterial2)) {
            if (!KUNSTSTOFF.equalsIgnoreCase(handlebarMaterial2)) {
                return getBadRequestResponseEntity(handlebarType2, handlebarMaterial2,
                        handlebarGearshift2, handleMaterial2, "Configuration invalid: " + handleMaterial2
                                + " is not compatible with material of handlebar " + handlebarMaterial2);
            }
        }
        if (LEDERGRIFF.equalsIgnoreCase(handleMaterial2)) {
            if (!RENNRADLENKER.equalsIgnoreCase(handlebarType2)) {
                return getBadRequestResponseEntity(handlebarType2, handlebarMaterial2,
                        handlebarGearshift2, handleMaterial2, "Configuration invalid: " + handleMaterial2
                                + " is not compatible with material of handlebar " + handlebarMaterial2);
            }
        }



        final Random rnd = new Random();
        final HandlebarSupplier handlebarSupplier =
                new HandlebarSupplier(BigInteger.probablePrime(8, rnd), price, deliveryDateFormatted);
        return ResponseEntity.ok(handlebarSupplier);
    }


    private ResponseEntity getBadRequestResponseEntity(final String handlebarType,
                                                       final String handlebarMaterial, final String handlebarGearshift,
                                                       final String handleMaterial, final String errorMessage) {
        return new ResponseEntity(
                HttpStatus.BAD_REQUEST + " - " + errorMessage + " - " + handlebarType + " - "
                        + handlebarMaterial + " - " + handlebarGearshift + " - " + handleMaterial,
                HttpStatus.BAD_REQUEST);
    }

}
