package fr.iut.androidprojet.modele;

public class Multiplication extends Operation {


    public Multiplication( double operande1, double operande2) {
        super(operande1, operande2, 'x');
    }
    @Override
    public double calculResultat() {
        return super.getOperande1() * super.getOperande2();
    }
}
