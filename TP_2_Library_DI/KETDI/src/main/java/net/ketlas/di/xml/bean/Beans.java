package net.ketlas.di.xml.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "beans")
public class Beans {

    @XmlElement(name = "bean")
    private List<Bean> bean = new ArrayList<>();;

    public List<Bean> getBean() {
        return bean;
    }

    public void setBeans(List<Bean> beans) {
        this.bean = beans;
    }

    public Beans(List<Bean> beans) {
        this.bean = beans;
    }

    public Beans(){

    }

}
