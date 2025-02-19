package fr.iut2.androidtp.exercice3Data;

import java.util.ArrayList;

public class Multiplication {

    private int operande1;
    private int operande2;
    private Integer reponseUtilisateur;
    public Multiplication(int operande1, int operande2) {
        this.operande1 = operande1;
        this.operande2 = operande2;
        this.reponseUtilisateur = null;
    }

    public int getOperande1() {
        return operande1;
    }

    public int getOperande2() {
        return operande2;
    }

    public int getResultat() {
        return operande1*operande2;
    }

    public void setReponseUtilisateur(int reponse) {
        this.reponseUtilisateur = reponse;
    }

    public boolean isReponseJuste() {
        if (reponseUtilisateur == null) {
            return false;
        }

        return getResultat() == reponseUtilisateur;
    }
}
