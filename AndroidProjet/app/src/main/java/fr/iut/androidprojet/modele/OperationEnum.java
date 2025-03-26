package fr.iut.androidprojet.modele;

public enum OperationEnum {
    ADDITION, SOUSTRACTION, MULTIPLICATION;

    public Operation getOperation(double operande1, double operande2) {
        switch(this) {
            case ADDITION:
                return new Addition(operande1, operande2);
            case MULTIPLICATION:
                return new Multiplication(operande1, operande2);
            default:
                return null;
        }
    }
}