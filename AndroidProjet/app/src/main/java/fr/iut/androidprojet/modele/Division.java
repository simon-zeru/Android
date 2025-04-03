package fr.iut.androidprojet.modele;

import java.io.Serializable;

public class Division extends Operation implements Serializable {

    public Division(double operande1, double operande2) {
        super(operande1, operande2, '/');
    }

    @Override
    public double calculResultat() {
        // Gestion de la division par zéro
        if (super.getOperande2() == 0) {
            throw new ArithmeticException("Division par zéro impossible !");
        }

        return super.getOperande1() / super.getOperande2();
    }
}