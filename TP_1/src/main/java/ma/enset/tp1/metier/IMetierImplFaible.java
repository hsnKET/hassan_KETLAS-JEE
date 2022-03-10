package ma.enset.tp1.metier;

import ma.enset.tp1.dao.DaoImpl;
import ma.enset.tp1.dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of IMetier using Loose Coupling (Couplage faible)
 */
@Component()
public class IMetierImplFaible implements IMetier {

    private IDao dao;

    public IMetierImplFaible() {
    }


    public IMetierImplFaible(IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcule() {
        double data = dao.getData();
        return Math.random() * data;
    }

    @Autowired
    public void setDao(IDao dao) {
        this.dao = dao;
    }
}

