package fr.iut.androidprojet.modele;

public enum OperationEnum {
    ADDITION, SOUSTRACTION, MULTIPLICATION, DIVISION; // Ajout de DIVISION

    public Operation getOperation(double operande1, double operande2) {
        switch(this) {
            case ADDITION:
                return new Addition(operande1, operande2);
            case SOUSTRACTION: // Ajout du cas SOUSTRACTION
                return new Soustraction(operande1, operande2);
            case MULTIPLICATION:
                return new Multiplication(operande1, operande2);
            case DIVISION: // Ajout du cas DIVISION
                return new Division(operande1, operande2);
            default:
                // Il est préférable de lever une exception si un cas non géré arrive
                throw new IllegalArgumentException("Type d'opération inconnu: " + this);
                // ou return null; si c'est le comportement souhaité
        }
    }
}