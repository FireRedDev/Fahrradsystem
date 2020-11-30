package at.jku.restservice;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TypeList {
    @Id
    String name;
    @ElementCollection
    List<String> options =  new ArrayList<>();

    public TypeList() {
    }

    public TypeList(String name, List<String> options) {
        this.name = name; this.options = options;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
