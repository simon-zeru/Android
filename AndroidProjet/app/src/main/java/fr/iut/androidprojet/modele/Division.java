package fr.iut.androidprojet.modele;

public class Division extends Operation {

    public Division(double operande1, double operande2) {
        // Appel du constructeur parent avec l'opérateur '/'
        super(operande1, operande2, '/');
    }

    @Override
    public double calculResultat() {
        // Gestion de la division par zéro
        if (super.getOperande2() == 0) {
            // Option 1: Lever une exception (souvent préférable)
            throw new ArithmeticException("Division par zéro impossible !");

            // Option 2: Retourner une valeur spéciale (moins courant pour la logique métier)
            // return Double.NaN; // Not a Number
            // return Double.POSITIVE_INFINITY; // Ou NEGATIVE_INFINITY selon operande1
        }
        // Calcul de la division
        return super.getOperande1() / super.getOperande2();
    }

    // Optionnel : Redéfinir toString si l'affichage par défaut n'est pas idéal
    // (Celui de la classe mère affiche deux fois operande2, il y a une erreur là-bas)
    // @Override
    // public String toString() {
    //     return getOperande1() + " / " + getOperande2() + " = ";
    // }

    // CORRECTION SUGGÉRÉE pour Operation.toString() dans Operation.java:
    /*
    @Override
    public String toString() {
        // Afficher operande1 puis operande2
        return this.getOperande1() + " " + operateur + " " + this.getOperande2() + " = ";
    }
    */
}