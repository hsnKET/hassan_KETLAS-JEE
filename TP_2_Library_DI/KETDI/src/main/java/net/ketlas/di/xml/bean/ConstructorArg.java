package net.ketlas.di.xml.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ConstructorArg {

    @XmlAttribute
    private String name;
    @XmlAttribute
    private String value;
    @XmlAttribute
    private String type;
    @XmlElement(name = "ref")
    private Ref ref;

    public ConstructorArg(String name, String value, String type,Ref refs) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.ref = refs;
    }

    public ConstructorArg(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Ref getRef() {
        return ref;
    }

    public void setRef(Ref refs) {
        this.ref = refs;
    }
}
