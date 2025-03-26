package fr.iut.androidprojet.modele;

public class TableOperation {
    private Operation[] operations;
    private OperationEnum typeOperation;

    private static final int NB_OPERATIONS = 10;
    public TableOperation(OperationEnum typeOperation) {
        this.typeOperation = typeOperation;
        this.operations = new Operation[NB_OPERATIONS];
        initialisation();
    }

    private void initialisation() {
        for (int i = 0; i < NB_OPERATIONS; i++) {
            operations[i] = typeOperation.getOperation(OperationUtilitaire.randomDouble(), OperationUtilitaire.randomDouble());
        }
    }

    public int getNbOperations() {
        return operations.length;
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
