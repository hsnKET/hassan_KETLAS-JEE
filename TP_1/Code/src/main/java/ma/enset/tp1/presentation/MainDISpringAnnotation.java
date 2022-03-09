package ma.enset.tp1.presentation;

import ma.enset.tp1.metier.IMetierImplFaible;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainDISpringAnnotation {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext("ma.enset.tp1.dao","ma.enset.tp1.metier");
        IMetierImplFaible iMetierImpl = (IMetierImplFaible) app.getBean(IMetierImplFaible.class);
        System.out.println(iMetierImpl.calcule());
    }
}
