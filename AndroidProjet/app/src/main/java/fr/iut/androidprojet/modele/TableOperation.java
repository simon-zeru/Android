package fr.iut.androidprojet.modele;

public class TableOperation {
    private Operation[] operations;
    private OperationEnum typeOperation;
    private int nbOperations;
    private static final int NB_OPERATIONS = 10;
    public TableOperation(OperationEnum typeOperation, int nbOperations) {
        this.typeOperation = typeOperation;
        this.operations = new Operation[nbOperations];
        this.nbOperations = nbOperations;
        initialisation();
    }

    public TableOperation(OperationEnum typeOperation) {
        this(typeOperation, NB_OPERATIONS);
    }

    private void initialisation() {
        for (int i = 0; i < nbOperations; i++) {
            double operande1 = OperationUtilitaire.randomDouble(); // Ou randomInt(min, max)
            double operande2 = OperationUtilitaire.randomDouble(); // Ou randomInt(min, max)

            // Logique spécifique avant création
            if (typeOperation == OperationEnum.DIVISION) {
                while (Math.abs(operande2) < 0.0001) { // Vérifier si proche de zéro pour double
                    operande2 = OperationUtilitaire.randomDouble(); // Re-générer
                }
            }
            operations[i] = typeOperation.getOperation(operande1, operande2);
        }
    }

    public int getNbOperations() {
        return nbOperations;
    }

    public Operation[] getOperations() {
        return operations;
    }

    public Operation getOperation(int index) {
        return operations[index];
    }

    public int getNbReponsesJustes() {
        int nbReponsesJustes = 0;
        for (Operation operation : operations) {
            if (operation.isReponseJuste()) {
                nbReponsesJustes++;
            } else {
            }
        }
        return nbReponsesJustes;
    }
}
