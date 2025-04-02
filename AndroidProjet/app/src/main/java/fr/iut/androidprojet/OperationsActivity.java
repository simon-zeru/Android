package fr.iut.androidprojet;

import android.content.Intent;
import android.graphics.Color; // Pour le feedback couleur
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale; // Pour formater les nombres décimaux
import java.util.Random;

// Importer les modèles
import fr.iut.androidprojet.modele.Operation;
import fr.iut.androidprojet.modele.OperationEnum;
// Importer l'exception si vous l'utilisez (ex: division par zéro)
// import fr.iut.androidprojet.modele.ErreurOperationException;


public class OperationsActivity extends AppCompatActivity {

    // Constantes
    private static final int NOMBRE_OPERATIONS = 10; // Nombre d'opérations par série
    private static final int MAX_OPERAND_VALUE = 12; // Valeur max pour les opérandes (ajustez si besoin)
    private static final int MIN_OPERAND_VALUE = 1;  // Valeur min

    // Données de l'activité
    private OperationEnum currentOperationType;
    private List<Operation> operationsList;
    private int currentOperationIndex = 0;
    private int score = 0;
    private Random random = new Random();

    // Vues UI
    private TextView tvOperation;
    private EditText etAnswer;
    private Button btnValidate;
    private TextView tvProgress;
    private TextView tvFeedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation); // Utiliser le nouveau layout

        // Initialiser les vues
        tvOperation = findViewById(R.id.tv_operation);
        etAnswer = findViewById(R.id.et_answer);
        btnValidate = findViewById(R.id.btn_validate);
        tvProgress = findViewById(R.id.tv_progress);
        tvFeedback = findViewById(R.id.tv_feedback);

        // Récupérer le type d'opération depuis l'Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(OperationsMenuActivity.EXTRA_OPERATION_TYPE)) {
            currentOperationType = (OperationEnum) intent.getSerializableExtra(OperationsMenuActivity.EXTRA_OPERATION_TYPE);

            if (currentOperationType != null) {
                setTitle("Exercice: " + currentOperationType.name()); // Mettre à jour le titre de l'activité
                Log.i("OperationActivity", "Type d'opération reçu : " + currentOperationType.name());

                // Générer la liste des opérations
                generateOperations(NOMBRE_OPERATIONS);

                // Afficher la première opération
                displayCurrentOperation();

            } else {
                handleError("Type d'opération invalide reçu.");
                return; // Quitter si erreur
            }
        } else {
            handleError("Aucun type d'opération fourni.");
            return; // Quitter si erreur
        }

        // Configurer le listener du bouton Valider
        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAnswer();
            }
        });
    }

    /**
     * Génère une liste d'opérations du type courant.
     * @param count Le nombre d'opérations à générer.
     */
    private void generateOperations(int count) {
        operationsList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            double operand1 = generateRandomOperand();
            double operand2 = generateRandomOperand();

            // Cas particulier pour la division: éviter division par zéro
            if (currentOperationType == OperationEnum.DIVISION) {
                while (operand2 == 0) { // Re-générer si operande2 est 0
                    operand2 = generateRandomOperand();
                    Log.d("GenerateOps", "Division: Opérande 2 était 0, regénéré: " + operand2);
                }
                // Optionnel: S'assurer que la division tombe "juste" ou dans une certaine plage?
                // Pour l'instant, on garde les doubles.
            }

            // Cas particulier pour soustraction: éviter résultats négatifs si non désiré
            if (currentOperationType == OperationEnum.SOUSTRACTION) {
                // S'assurer que operande1 >= operande2 pour éviter résultats négatifs
                if (operand1 < operand2) {
                    // Échanger les opérandes
                    double temp = operand1;
                    operand1 = operand2;
                    operand2 = temp;
                }
            }

            try {
                // Utiliser l'enum pour obtenir la bonne instance d'opération
                Operation op = currentOperationType.getOperation(operand1, operand2);
                operationsList.add(op);
            } catch (IllegalArgumentException e) {
                Log.e("GenerateOps", "Erreur lors de la création de l'opération", e);
                // Gérer l'erreur si nécessaire (ex: sauter cette opération)
            }

        }
        Log.i("GenerateOps", "Généré " + operationsList.size() + " opérations de type " + currentOperationType);
    }

    /**
     * Génère une opérande aléatoire dans la plage définie.
     * @return une valeur double aléatoire.
     */
    private double generateRandomOperand() {
        // Génère un entier entre MIN et MAX inclus
        return random.nextInt((MAX_OPERAND_VALUE - MIN_OPERAND_VALUE) + 1) + MIN_OPERAND_VALUE;
    }


    /**
     * Affiche l'opération actuelle dans l'interface utilisateur.
     */
    private void displayCurrentOperation() {
        if (currentOperationIndex < operationsList.size()) {
            Operation currentOp = operationsList.get(currentOperationIndex);

            // !! IMPORTANT: Assurez-vous d'avoir corrigé Operation.toString() !!
            // Ex: return this.getOperande1() + " " + operateur + " " + this.getOperande2() + " = ";
            tvOperation.setText(currentOp.toString());

            // Mettre à jour la progression
            tvProgress.setText(String.format(Locale.getDefault(), "Opération %d / %d", currentOperationIndex + 1, NOMBRE_OPERATIONS));

            // Vider le champ de réponse et le feedback
            etAnswer.setText("");
            tvFeedback.setText("");
            tvFeedback.setTextColor(Color.BLACK); // Remettre couleur par défaut

            // Mettre le focus sur le champ de réponse
            etAnswer.requestFocus();

        } else {
            // Toutes les opérations sont terminées
            showResults();
        }
    }

    /**
     * Valide la réponse de l'utilisateur pour l'opération actuelle.
     */
    private void validateAnswer() {
        if (currentOperationIndex >= operationsList.size()) {
            Log.w("Validate", "Validation appelée alors que toutes les opérations sont terminées.");
            return; // Ne rien faire si terminé
        }

        String answerString = etAnswer.getText().toString().trim();
        if (answerString.isEmpty()) {
            tvFeedback.setText("Veuillez entrer une réponse.");
            tvFeedback.setTextColor(Color.RED);
            return;
        }

        try {
            // Convertir la réponse en double
            double userAnswer = Double.parseDouble(answerString);
            Operation currentOp = operationsList.get(currentOperationIndex);
            double correctAnswer = currentOp.calculResultat();

            // Comparer les réponses (attention aux doubles !)
            // Utiliser une petite marge d'erreur pour les comparaisons de double
            double epsilon = 0.0001;
            if (Math.abs(userAnswer - correctAnswer) < epsilon) {
                // Réponse correcte
                score++;
                tvFeedback.setText("Correct !");
                tvFeedback.setTextColor(Color.parseColor("#006400")); // Vert foncé
            } else {
                // Réponse incorrecte
                tvFeedback.setText(String.format(Locale.getDefault(), "Incorrect. La réponse était %.2f", correctAnswer));
                tvFeedback.setTextColor(Color.RED);
            }

            // Passer à l'opération suivante après un court délai (ou immédiatement)
            // Option 1: Immédiatement
            currentOperationIndex++;
            displayCurrentOperation();

            // Option 2: Avec délai pour voir le feedback (plus complexe, nécessite un Handler)
            // new Handler(Looper.getMainLooper()).postDelayed(() -> {
            //      currentOperationIndex++;
            //      displayCurrentOperation();
            // }, 1500); // Délai de 1.5 secondes


        } catch (NumberFormatException e) {
            // L'utilisateur n'a pas entré un nombre valide
            tvFeedback.setText("Entrée invalide. Utilisez des chiffres.");
            tvFeedback.setTextColor(Color.RED);
            Log.w("Validate", "Erreur de parsing de la réponse: " + answerString, e);
        } catch (ArithmeticException e) {
            // Gérer spécifiquement la division par zéro si elle peut survenir ici
            tvFeedback.setText("Erreur: " + e.getMessage());
            tvFeedback.setTextColor(Color.RED);
            Log.e("Validate", "Erreur arithmétique", e);
            // Passer à la suivante ou bloquer ? Pour l'instant on passe.
            currentOperationIndex++;
            displayCurrentOperation();
        }
    }

    /**
     * Affiche les résultats finaux.
     */
    private void showResults() {
        Log.i("OperationActivity", "Série terminée. Score: " + score + "/" + NOMBRE_OPERATIONS);
        // Afficher le score final
        tvOperation.setText("Terminé !");
        tvFeedback.setText(String.format(Locale.getDefault(), "Votre score : %d / %d", score, NOMBRE_OPERATIONS));
        tvFeedback.setTextColor(Color.BLUE);

        // Désactiver le champ de réponse et le bouton
        etAnswer.setEnabled(false);
        btnValidate.setEnabled(false);

        // Option: Afficher un bouton pour recommencer ou retourner au menu
        // ...
    }


    /**
     * Gère les erreurs de démarrage de l'activité.
     * @param message Message d'erreur à afficher et logger.
     */
    private void handleError(String message) {
        Log.e("OperationActivity", "Erreur démarrage: " + message);
        Toast.makeText(this, "Erreur: " + message, Toast.LENGTH_LONG).show();
        finish(); // Fermer l'activité si elle ne peut pas fonctionner correctement
    }

    // N'oubliez pas de corriger Operation.toString() dans Operation.java !
    // Suggestion:
     /*
        @Override
        public String toString() {
            // Formatage pour éviter trop de décimales si opérandes sont entiers
            String op1Str = (getOperande1() == (long) getOperande1()) ? "" + (long) getOperande1() : String.format(Locale.getDefault(),"%.2f", getOperande1());
            String op2Str = (getOperande2() == (long) getOperande2()) ? "" + (long) getOperande2() : String.format(Locale.getDefault(),"%.2f", getOperande2());
            return String.format(Locale.getDefault(), "%s %c %s = ", op1Str, operateur, op2Str);
        }
     */
}