package fr.iut.androidprojet.modele;

import java.util.Random;
public class OperationUtilitaire {
    private static final Random random = new Random();
    private static final int RANGE_MAX = 200;
    private static final int ARRONDI = 10;
    // Retourne une valeur de type double entre 0 et RANGE_MAX/ARRONDI
    public static double randomDouble() {
        return (double)random.nextInt(RANGE_MAX)/ARRONDI;
    }
    // Retourne la valeur double arrondi
    // Evite les erreurs de calcul du au double en Java
    public static double arrondir(double valeur) {
        return (double)Math.round(valeur * ARRONDI) / ARRONDI;
    }

    public static OperationEnum getTypeOperation(String type) {
        switch (type) {
            case "addition":
                return OperationEnum.ADDITION;
            case "multiplication":
                return OperationEnum.MULTIPLICATION;
            default:
                return null;
        }
    }
}
