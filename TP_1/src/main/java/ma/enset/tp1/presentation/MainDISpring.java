package ma.enset.tp1.presentation;

import ma.enset.tp1.metier.IMetierImplFaible;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainDISpring {
    public static void main(String[] args) {

        ApplicationContext app = new ClassPathXmlApplicationContext("spring_config.xml");
        IMetierImplFaible iMetierImpl = (IMetierImplFaible) app.getBean("metierImp");
        System.out.println(iMetierImpl.calcule());
    }
}
