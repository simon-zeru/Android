package fr.iut.androidprojet.modele;

import java.io.Serializable;

public class Soustraction extends Operation implements Serializable {

    public Soustraction(double operande1, double operande2) {
        super(operande1, operande2, '-');
    }

    @Override
    public double calculResultat() {
        return super.getOperande1() - super.getOperande2();
    }
}