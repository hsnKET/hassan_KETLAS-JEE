package net.ketlas.di.xml.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;


@XmlAccessorType(XmlAccessType.FIELD)
public class Ref {
    @XmlAttribute
    private String bean;

    public Ref(){

    }
    public Ref(String bean) {
        this.bean = bean;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }
}
