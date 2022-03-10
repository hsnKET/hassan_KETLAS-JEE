package ma.enset.tp1.presentation;

import ma.enset.tp1.dao.DaoImpl;
import ma.enset.tp1.dao.IDao;
import ma.enset.tp1.metier.IMetierImplFaible;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class MainDIDynamic {
    public static void main(String[] args) throws Exception{
        // DI Dynamic constructor
        Scanner scanner = new Scanner(new File("config.txt"));
        String daoClassName = scanner.next();
        Class cDao = Class.forName(daoClassName);
        IDao iDao = (IDao) cDao.newInstance();
        IMetierImplFaible iMetierImpl = new IMetierImplFaible(iDao);
        System.out.println(iMetierImpl.calcule());
        // DI Dynamic setter
        IMetierImplFaible iMetierImpl2 = new IMetierImplFaible();
        Method setDao = iMetierImpl2.getClass().getMethod("setDao",IDao.class);
        setDao.invoke(iMetierImpl2,iDao);
        System.out.println(iMetierImpl2.calcule());

    }
}
