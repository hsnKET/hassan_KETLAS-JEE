package net.ketlas.di.xml.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Property {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String type;
    @XmlAttribute
    private String value;
    @XmlElement(name = "ref")
    private Ref ref;

    public Property(){

    }
    public Property(String name, String type, String value, Ref ref) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.ref = ref;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Ref getRef() {
        return ref;
    }

    public void setRef(Ref ref) {
        this.ref = ref;
    }
}
