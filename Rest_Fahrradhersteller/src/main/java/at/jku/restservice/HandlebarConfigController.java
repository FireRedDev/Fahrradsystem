package at.jku.restservice;

import FiBu.shared.OrderTransferServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.MessageFormat;
import java.util.*;

@RestController
public class HandlebarConfigController {
    @Autowired
    HandlebarRepository handlebarRepository;
    @Autowired
    TypeRepository typerepo;
    @Autowired
    RestrictionRepository restrictionRepository;

    public HandlebarConfigController() {

    }

    @PostMapping("/order/{1}/{2}/{3}/{4}")
    @Transactional
    public ResponseEntity<HandlebarConfig> order(@PathVariable("1") final String handlebarType,
                                                 @PathVariable("2") final String handlebarMaterial,
                                                 @PathVariable("3") final String handlebarGearshift,
                                                 @PathVariable("4") final String handleMaterial) {

        if (!typerepo.findById("Lenker").get().options.contains(handlebarType)) {
            return getBadRequestResponseEntity(handlebarType, handlebarMaterial, handlebarGearshift,
                    handleMaterial,
                    "Configuration wrong: Type of Handlebar " + handlebarType + " is not available.");
        }
        if (!typerepo.findById("Material").get().options.contains(handlebarMaterial)) {
            return getBadRequestResponseEntity(handlebarType, handlebarMaterial, handlebarGearshift,
                    handleMaterial,
                    "Configuration wrong: Material " + handlebarMaterial + " is not available.");
        }
        if (!typerepo.findById("Schaltung").get().options.contains(handlebarGearshift)) {
            return getBadRequestResponseEntity(handlebarType, handlebarMaterial, handlebarGearshift,
                    handleMaterial, "Configuration wrong: Type of gearshift " + handlebarGearshift
                            + " is not available.");
        }
        if (!typerepo.findById("Griff").get().options.contains(handleMaterial)) {
            return getBadRequestResponseEntity(handlebarType, handlebarMaterial, handlebarGearshift,
                    handleMaterial,
                    "Configuration wrong: Type of handle " + handleMaterial + " is not available.");
        }

        for (Restriction r : restrictionRepository.findAll()) {
            if (r.a.equalsIgnoreCase(handlebarType) && (r.b.equalsIgnoreCase(handlebarGearshift) ||
                    r.b.equalsIgnoreCase(handlebarMaterial) ||
                    r.b.equalsIgnoreCase(handleMaterial)
            )) return getBadRequestResponseEntity(handlebarType, handlebarMaterial,
                    handlebarGearshift, handleMaterial,
                    "!!Configuration invalid: " + r.a + " is not compatible with "
                            + r.b + "!! ");
        }
        SupplierAnswer answer = new SupplierAnswer(1, "");
        try {
            answer = sendToSuppliers(handlebarType, handlebarMaterial, handlebarGearshift, handleMaterial);
        } catch (Exception ex) {
            System.out.println("Error during contacting suppliers");
        }
        final Random rnd = new Random();
        final HandlebarConfig handlebarConfig =
                new HandlebarConfig(BigInteger.probablePrime(8, rnd), handlebarType, handlebarMaterial,
                        handlebarGearshift, handleMaterial, new Date());
        handlebarRepository.save(handlebarConfig);
        System.out.println("Persisted Order" + handlebarConfig.toString());
        System.out.println("Send to fibu successfull? : "+callFibu(handlebarConfig, answer.price, answer.deliverydate));

        return ResponseEntity.ok(handlebarConfig);
    }

    private boolean callFibu(HandlebarConfig handlebarConfig, int price, String deliveryDate) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            OrderTransferServer server = (OrderTransferServer) registry.lookup("OrderTransfer");
            OrderTransfer responseMessage = server.order(handlebarConfig.getHandlebarType(), handlebarConfig.getHandlebarMaterial(),
                    handlebarConfig.getHandlebarGearshift(), handlebarConfig.getHandleMaterial(),
                    handlebarConfig.getOrderId(), price, deliveryDate);
            String expectedMessage = "Server Message";
            System.out.println(responseMessage);
            return true;
        } catch (RemoteException ex) {
            System.out.println(ex.getLocalizedMessage());
            return false;
        } catch (NotBoundException ex) {
            System.out.println(ex.getLocalizedMessage());
            return false;
        }

    }

    private ResponseEntity getBadRequestResponseEntity(final String handlebarType,
                                                       final String handlebarMaterial, final String handlebarGearshift,
                                                       final String handleMaterial, final String errorMessage) {
        return new ResponseEntity(
                HttpStatus.BAD_REQUEST + " - " + errorMessage + " - " + handlebarType + " - "
                        + handlebarMaterial + " - " + handlebarGearshift + " - " + handleMaterial,
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getAvailableHandlebarTypes")
    public List<String> getAvailableHandlebarTypes() {
        return typerepo.findById("Lenker").get().options;
    }

    @GetMapping("/getAvailableMaterial")
    public List<String> getAvailableMaterial(@RequestParam final String handlebarType) {
        List<String> material = typerepo.findById("Material").get().options;
        List<String> resultList = new LinkedList<>(material);
        for (Iterator<String> iterator = material.iterator(); iterator.hasNext(); ) {
            String mat = iterator.next();
            for (Restriction r : restrictionRepository.findByA(handlebarType)) {
                if ((r.a.equalsIgnoreCase(handlebarType) && r.b.equalsIgnoreCase(mat))) {
                    if (resultList.contains(mat)) resultList.remove(mat);
                }
            }
        }
        return resultList;
    }

    @GetMapping("/getAvailableGearshifts")
    public List<String> getAvailableGearshifts(@RequestParam final String handlebarMaterial) {
        List<String> schaltung = typerepo.findById("Schaltung").get().options;
        List<String> resultList = new LinkedList<>(schaltung);
        for (Iterator<String> iterator = schaltung.iterator(); iterator.hasNext(); ) {
            String sch = iterator.next();
            for (Restriction r : restrictionRepository.findByA(handlebarMaterial)) {
                if ((r.a.equalsIgnoreCase(handlebarMaterial) && r.b.equalsIgnoreCase(sch))) {
                    if (resultList.contains(sch)) resultList.remove(sch);
                }
            }
        }
        return resultList;
    }

    @GetMapping("/getAvailableHandleMaterial")
    public List<String> getAvailableHandleMaterial(@RequestParam final String handlebarType,
                                                   @RequestParam final String handlebarMaterial) {
        List<String> griff = typerepo.findById("Griff").get().options;
        List<String> resultList = new LinkedList<>(griff);

        for (Iterator<String> iterator = griff.iterator(); iterator.hasNext(); ) {
            String gr = iterator.next();
            for (Restriction r : restrictionRepository.findAll()) {
                if ((r.a.equalsIgnoreCase(handlebarType) && r.b.equalsIgnoreCase(gr))
                        || (r.a.equalsIgnoreCase(handlebarMaterial) && r.b.equalsIgnoreCase(gr))) {
                    if (resultList.contains(gr)) resultList.remove(gr);
                }
            }
        }
        return resultList;
    }


    public SupplierAnswer sendToSuppliers(String handlebarType, String handlebarMaterial, String handlebarGearshift, String handleMaterial) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpClient client = HttpClient.newHttpClient();
        Integer priceSupplier1 = null;
        String deliveryDateSupplier1 = null;
        Integer priceSupplier2 = null;
        String deliveryDateSupplier2 = null;
        String deliveryDateSupplier;
        Integer price;
        try {
            // Erste Lieferant Anfrage
            final String uri = MessageFormat
                    .format("http://localhost:8081/supplier1/order/{0}/{1}/{2}/{3}", handlebarType,
                            handlebarMaterial, handlebarGearshift, handleMaterial);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri))
                    .POST(HttpRequest.BodyPublishers.ofString("")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            final String body = response.body();
            if (response.statusCode() == 200) {
                final HandlebarSupplier handlebarSupplier =
                        objectMapper.readValue(body, HandlebarSupplier.class);
                System.out.println("Result1 > " + handlebarSupplier);
                priceSupplier1 = handlebarSupplier.getPrice();
                deliveryDateSupplier1 = handlebarSupplier.getDeliveryDate();
            } else {
                if (body != null && !body.isEmpty()) {
                    System.out.println("Something went wrong > " + body);
                } else {
                    System.out.println("Something went wrong > " + response.toString());
                }
            }

        } catch (
                final IOException e) {
            e.printStackTrace();
        } catch (
                final InterruptedException e) {
            e.printStackTrace();
        }

        // Zweite Lieferant Anfrage
        try {

            final String uri = MessageFormat
                    .format("http://localhost:8081/supplier2/order/{0}/{1}/{2}/{3}", handlebarType,
                            handlebarMaterial, handlebarGearshift, handleMaterial);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri))
                    .POST(HttpRequest.BodyPublishers.ofString("")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            final String body = response.body();
            if (response.statusCode() == 200) {
                final HandlebarSupplier handlebarSupplier =
                        objectMapper.readValue(body, HandlebarSupplier.class);
                System.out.println("Result2 > " + handlebarSupplier);
                priceSupplier2 = handlebarSupplier.getPrice();
                deliveryDateSupplier2 = handlebarSupplier.getDeliveryDate();
            } else {
                if (body != null && !body.isEmpty()) {
                    System.out.println("Something went wrong > " + body);
                } else {
                    System.out.println("Something went wrong > " + response.toString());
                }
            }

        } catch (
                final IOException e) {
            e.printStackTrace();
        } catch (
                final InterruptedException e) {
            e.printStackTrace();
        }

        //Auswahl günstigster Lieferant
        if (priceSupplier1 < priceSupplier2) {
            deliveryDateSupplier = deliveryDateSupplier1;
            price = priceSupplier1;
        } else {
            deliveryDateSupplier = deliveryDateSupplier2;
            price = priceSupplier2;
        }
        return new SupplierAnswer(price, deliveryDateSupplier);

    }
}
