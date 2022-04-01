package net.ketlas.di.xml.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Bean {
    @XmlAttribute
    private String id;
    @XmlAttribute(name = "class")
    private String clazz;
    @XmlAttribute
    private String name;
    @XmlElement(name = "constructor-arg")
    private List<ConstructorArg> constructorArgs = new ArrayList<>();
    @XmlElement(name = "property")
    private List<Property> properties = new ArrayList<>();

    public Bean(){

    }
    public Bean(String id, String clazz, String name, List<ConstructorArg> constructorArgs, List<Property> properties) {
        this.id = id;
        this.clazz = clazz;
        this.name = name;
        this.constructorArgs = constructorArgs;
        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ConstructorArg> getConstructorArgs() {
        return constructorArgs;
    }

    public void setConstructorArgs(List<ConstructorArg> constructorArgs) {
        this.constructorArgs = constructorArgs;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
