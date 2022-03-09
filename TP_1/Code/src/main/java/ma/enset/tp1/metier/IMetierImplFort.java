package ma.enset.tp1.metier;

import ma.enset.tp1.dao.DaoImpl;

/**
 * Implementation of IMetier using Tight Coupling (Couplage Fort)
 */
public class IMetierImplFort implements IMetier{

    private final DaoImpl dao;

    public IMetierImplFort(DaoImpl dao) {
        this.dao = dao;
    }
    @Override
    public double calcule() {
        double data = dao.getData();
        return Math.random()*data;
    }
}
