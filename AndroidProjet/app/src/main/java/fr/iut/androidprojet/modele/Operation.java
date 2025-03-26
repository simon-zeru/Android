package fr.iut.androidprojet.modele;

public abstract class Operation {
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
//        if ( !isReponseJuste() ) {
//            throw new ErreurOperationException("La réponse n'est pas correcte, réessayez !");
//        }
    }


    public boolean isReponseJuste() {
        return reponseUtilisateur != null && reponseUtilisateur == calculResultat();
    }

    public abstract double calculResultat();

    @Override
    public String toString() {
        return this.getOperande2() + operateur + this.getOperande2() + " = ";
    }
}
