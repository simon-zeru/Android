package fr.iut.androidprojet.modele;

import java.io.Serializable;

public abstract class Operation implements Serializable {
    private double operande1;
    private double operande2;
    private Double reponseUtilisateur;
    private char operateur;

    public Operation(double operande1, double operande2, char operateur) {
        this.operande1 = operande1;
        this.operande2 = operande2;
        this.operateur = operateur;
    }

    public double getOperande1() {
        return operande1;
    }

    public double getOperande2() {
        return operande2;
    }

    public void setReponseUtilisateur(Double reponse) throws ErreurOperationException {
        this.reponseUtilisateur = reponse;
    }

    public Double getReponseUtilisateur() {
        return reponseUtilisateur;
    }



    public boolean isReponseJuste() {
        return reponseUtilisateur != null && reponseUtilisateur == calculResultat();
    }

    public abstract double calculResultat();

    @Override
    public String toString() {
        return this.getOperande1() + " " + operateur + " " + this.getOperande2() + " = ";
    }
}
