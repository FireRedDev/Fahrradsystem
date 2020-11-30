package at.jku.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Component
public class InitDB {
    public static final String RENNRADLENKER = "Rennradlenker";
    public static final String FLATBARLENKER = "Flatbarlenker";
    public static final String ALUMINIUM = "Aluminium";
    public static final String KUNSTSTOFF = "Kunststoff";
    public static final String STAHL = "Stahl";
    public static final String KETTENSCHALTUNG = "Kettenschaltung";
    public static final String KUNSTSTOFFGRIFF = "Kunststoffgriff";
    public static final String LEDERGRIFF = "Ledergriff";
    private final List<String> availableHandlebarTypes = new ArrayList<>();
    private final List<String> availableMaterial = new ArrayList<>();
    private final List<String> availableGearshifts = new ArrayList<>();
    private final List<String> availableHandleMaterial = new ArrayList<>();
    @Autowired
    TypeRepository typerepo;
    @Autowired
    RestrictionRepository restrictionRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() {
        System.out.println("Init DB");
        availableHandlebarTypes.add(FLATBARLENKER);
        availableHandlebarTypes.add(RENNRADLENKER);
        availableHandlebarTypes.add("Bullhornlenker");

        availableMaterial.add(ALUMINIUM);
        availableMaterial.add(STAHL);
        availableMaterial.add(KUNSTSTOFF);

        availableGearshifts.add(KETTENSCHALTUNG);
        availableGearshifts.add("Nabenschaltung");
        availableGearshifts.add("Tretlagerschaltung");

        availableHandleMaterial.add(LEDERGRIFF);
        availableHandleMaterial.add("Schaumstoffgriff");
        availableHandleMaterial.add(KUNSTSTOFFGRIFF);
        typerepo.save(new TypeList("Lenker",availableHandlebarTypes));
        typerepo.save(new TypeList("Material",availableMaterial));
        typerepo.save(new TypeList("Schaltung",availableGearshifts));
        typerepo.save(new TypeList("Griff",availableHandleMaterial));


        Restriction r1 = new Restriction("Flatbarlenker","Stahl");
        Restriction r2 = new Restriction("Stahl","Flatbarlenker");
        Restriction r3 = new Restriction("Rennradlenker","Stahl");
        Restriction r4 = new Restriction("Stahl","Rennradlenker");
        Restriction r5 = new Restriction("Stahl","Nabenschaltung");
        Restriction r6 = new Restriction("Stahl","Tretlagerschaltung");
        Restriction r7 = new Restriction("Nabenschaltung","Stahl");
        Restriction r8 = new Restriction("Tretlagerschaltung","Stahl");
        Restriction r9 = new Restriction("Kunststoffgriff","Stahl");
        Restriction r10 = new Restriction("Stahl","Kunststoffgriff");
        Restriction r12 = new Restriction("Kunststoffgriff","Aluminium");
        Restriction r13 = new Restriction("Aluminium","Kunststoffgriff");
        Restriction r14 = new Restriction("Ledergriff","Flatbarlenker");
        Restriction r15 = new Restriction("Flatbarlenker","Ledergriff");
        Restriction r16 = new Restriction("Ledergriff","Bullhornlenker");
        Restriction r17 = new Restriction("Bullhornlenker","Ledergriff");

        restrictionRepository.saveAll(Arrays.asList(r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r12,r13,r14,r15,r16,r17));
    }
}
