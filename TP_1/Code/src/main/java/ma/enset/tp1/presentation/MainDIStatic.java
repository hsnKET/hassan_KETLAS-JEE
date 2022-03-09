package ma.enset.tp1.presentation;

import ma.enset.tp1.dao.DaoImpl;
import ma.enset.tp1.dao.IDao;
import ma.enset.tp1.metier.IMetierImplFaible;

public class MainDIStatic {
    public static void main(String[] args) {
        // DI Static
        IDao iDao = new DaoImpl();
        IMetierImplFaible iMetierImpl = new IMetierImplFaible(iDao);
        System.out.println(iDao.getData());
    }
}
