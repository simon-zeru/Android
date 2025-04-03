package fr.iut.androidprojet;

import android.content.Intent;
import android.graphics.Color; // Pour le feedback couleur
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

// Importer les modèles
import fr.iut.androidprojet.db.DatabaseClient;
import fr.iut.androidprojet.modele.Operation;
import fr.iut.androidprojet.modele.OperationEnum;
import fr.iut.androidprojet.modele.TableOperation;



public class OperationsActivity extends AppCompatActivity {

    // Constantes
    private static final int NOMBRE_OPERATIONS = 10; // Nombre d'opérations par série
    private static final int MAX_OPERAND_VALUE = 12; // Valeur max pour les opérandes (ajustez si besoin)
    private static final int MIN_OPERAND_VALUE = 1;  // Valeur min

    // Données de l'activité
    private OperationEnum currentOperationType;
    private TableOperation tableOperation;
    private int currentOperationIndex = 0;
    private int score = 0;
    // private Random random = new Random();


    // Vues UI
    private TextView tvOperation;
    private EditText etAnswer;
    private Button btnValidate;
    private TextView tvProgress;
    private TextView tvFeedback;
    private Toolbar toolbar;
    private LinearLayout llEndButtons; // Layout contenant les boutons de fin
    private Button btnRestart;
    private Button btnChangeExercise;
    private Button btnViewCorrection;

    // Db instance
    private DatabaseClient mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        toolbar = findViewById(R.id.toolbar_operation); // Récupérer la Toolbar via son ID
        setSupportActionBar(toolbar); // Définir comme ActionBar
        // Afficher le bouton retour (Up button)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        // Initialiser les vues (inchangé)
        tvOperation = findViewById(R.id.tv_operation);
        etAnswer = findViewById(R.id.et_answer);
        btnValidate = findViewById(R.id.btn_validate);
        tvProgress = findViewById(R.id.tv_progress);
        tvFeedback = findViewById(R.id.tv_feedback);
        llEndButtons = findViewById(R.id.ll_end_buttons);
        btnRestart = findViewById(R.id.btn_restart);
        btnChangeExercise = findViewById(R.id.btn_change_exercise);
        btnViewCorrection = findViewById(R.id.btn_view_correction);

        mDb = DatabaseClient.getInstance(getApplicationContext()); // Décommenté si besoin

        // Récupérer le type d'opération depuis l'Intent (inchangé)
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(OperationsMenuActivity.EXTRA_OPERATION_TYPE)) {
            currentOperationType = (OperationEnum) intent.getSerializableExtra(OperationsMenuActivity.EXTRA_OPERATION_TYPE);

            if (currentOperationType != null) {
                setTitle("Exercice: " + currentOperationType.name());
                Log.i("OperationActivity", "Type d'opération reçu : " + currentOperationType.name());

                try {
                    tableOperation = new TableOperation(currentOperationType, NOMBRE_OPERATIONS);
                    Log.i("OperationActivity", "TableOperation créée avec " + tableOperation.getNbOperations() + " opérations.");
                    // Vérifier si la table a bien été remplie (facultatif)
                    if (tableOperation.getOperations() == null || tableOperation.getNbOperations() == 0 || tableOperation.getOperation(0) == null) {
                        throw new IllegalStateException("TableOperation n'a pas généré d'opérations.");
                    }
                } catch (Exception e) {
                    // Attraper les erreurs potentielles de TableOperation ou OperationUtilitaire
                    Log.e("OperationActivity", "Erreur lors de la création de TableOperation", e);
                    handleError("Impossible de générer les opérations.");
                    return;
                }
                // -----------------------------------------------------------

                // Afficher la première opération
                displayCurrentOperation();

            } else {
                handleError("Type d'opération invalide reçu.");
                return;
            }
        } else {
            handleError("Aucun type d'opération fourni.");
            return;
        }

        // Configurer le listener du bouton Valider (inchangé dans sa structure)
        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAnswer();
            }
        });

        // --- AJOUT : Configurer les listeners des nouveaux boutons ---
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartExercise();
            }
        });

        btnChangeExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simplement terminer l'activité pour retourner au menu précédent
                finish();
            }
        });

        btnViewCorrection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCorrectionActivity();
            }
        });
    }

    /*
    * Gérer le clic sur la flèche retour de la Toolbar
    * */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Gérer la flèche retour (android.R.id.home est l'ID standard)
        if (item.getItemId() == android.R.id.home) {
            finish(); // Ferme l'activité actuelle et retourne à la précédente
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Affiche l'opération actuelle dans l'interface utilisateur.
     */
    private void displayCurrentOperation() {

        if (tableOperation != null && currentOperationIndex < tableOperation.getNbOperations()) {
            // --- État "En cours d'exercice" ---
            // Afficher/Cacher les bons boutons
            btnValidate.setVisibility(View.VISIBLE);   // Valider est visible
            btnViewCorrection.setVisibility(View.GONE); // Correction est cachée
            llEndButtons.setVisibility(View.GONE);      // Boutons du bas sont cachés

            // Activer les champs/boutons nécessaires
            etAnswer.setEnabled(true);
            btnValidate.setEnabled(true); // S'assurer qu'il est actif

            // Afficher l'opération (code existant)
            Operation currentOp = tableOperation.getOperation(currentOperationIndex);
            if (currentOp != null) {
                tvOperation.setText(currentOp.toString());
                tvProgress.setText(String.format(Locale.getDefault(), "Opération %d / %d", currentOperationIndex + 1, tableOperation.getNbOperations()));
                etAnswer.setText("");
                tvFeedback.setText("");
                tvFeedback.setTextColor(Color.BLACK);
                etAnswer.requestFocus();
            } else {
                handleError("Erreur interne (opération non trouvée).");
            }
        } else {
            if (tableOperation != null) {
                showResults();
            }
        }
    }



    /**
     * Valide la réponse de l'utilisateur pour l'opération actuelle.
     */
    private void validateAnswer() {
        if (tableOperation == null || currentOperationIndex >= tableOperation.getNbOperations()) {
            Log.w("Validate", "Validation appelée alors que terminé ou tableOperation non initialisée.");
            return;
        }

        String answerString = etAnswer.getText().toString().trim();
        if (answerString.isEmpty()) {
            tvFeedback.setText("Veuillez entrer une réponse.");
            tvFeedback.setTextColor(Color.RED);
            return;
        }

        try {
            // 1. Parser la réponse de l'utilisateur
            double userAnswer = Double.parseDouble(answerString);

            // 2. Obtenir l'opération courante
            Operation currentOp = tableOperation.getOperation(currentOperationIndex);

            // ---=== MODIFICATION ICI : Enregistrer la réponse utilisateur ===---
            try {
                // 3. Enregistrer la réponse de l'utilisateur DANS l'objet Operation
                // Ceci permet à la méthode isReponseJuste() de fonctionner correctement
                // et rend getNbReponsesJustes() de TableOperation utilisable à la fin.
                currentOp.setReponseUtilisateur(userAnswer);
            } catch (Exception e) { // Utilisez ErreurOperationException si vous l'avez définie et décommentée
                Log.e("ValidateAnswer", "Erreur durant setReponseUtilisateur", e);
                // Afficher une erreur ou gérer spécifiquement si besoin
                tvFeedback.setText("Erreur interne lors de l'enregistrement de la réponse.");
                tvFeedback.setTextColor(Color.RED);
                // Décider si on doit arrêter ou continuer malgré tout
                // return; // Pourrait être une option si l'erreur est bloquante
            }
            // ---=== FIN DE LA MODIFICATION ===---


            // 4. Vérifier si la réponse enregistrée est juste en utilisant la méthode de l'objet Operation
            //    (plutôt que de refaire la comparaison avec epsilon ici)
            if (currentOp.isReponseJuste()) {
                // Réponse correcte
                score++; // Mettre à jour le score local de l'activité
                tvFeedback.setText("Correct !");
                tvFeedback.setTextColor(Color.parseColor("#006400")); // Vert foncé
            } else {
                // Réponse incorrecte
                // Récupérer la vraie réponse pour l'afficher
                double correctAnswer = currentOp.calculResultat();
                tvFeedback.setText(String.format(Locale.getDefault(), "Incorrect. La réponse était %.2f", correctAnswer));
                tvFeedback.setTextColor(Color.RED);
            }

            // 5. Passer à l'opération suivante
            currentOperationIndex++;
            displayCurrentOperation(); // Affiche la suivante ou les résultats finaux

        } catch (NumberFormatException e) {
            // L'utilisateur n'a pas entré un nombre valide
            tvFeedback.setText("Entrée invalide. Utilisez des chiffres.");
            tvFeedback.setTextColor(Color.RED);
            Log.w("Validate", "Erreur de parsing: " + answerString, e);
        } catch (ArithmeticException e) { // Ex: Division par zéro dans calculResultat (si pas déjà géré avant)
            tvFeedback.setText("Erreur: " + e.getMessage());
            tvFeedback.setTextColor(Color.RED);
            Log.e("Validate", "Erreur arithmétique", e);
            // On passe quand même à la suivante pour ne pas bloquer
            currentOperationIndex++;
            displayCurrentOperation();
        }
    }

    /**
     * Affiche les résultats finaux.
     */
    private void showResults() {
        Log.i("OperationActivity", "Série terminée. Score: " + score + "/" + tableOperation.getNbOperations());

        // Mettre à jour les textes (inchangé)
        tvOperation.setText("Terminé !");
        tvProgress.setText("Fin de la série");
        tvFeedback.setText(String.format(Locale.getDefault(), "Votre score : %d / %d", score, tableOperation.getNbOperations()));
        tvFeedback.setTextColor(Color.BLUE);

        // --- État "Fin d'exercice" ---
        // Désactiver la saisie
        etAnswer.setEnabled(false);

        // Cacher Valider, Afficher Correction au même endroit
        btnValidate.setVisibility(View.GONE);       // Cacher Valider
        btnViewCorrection.setVisibility(View.VISIBLE); // Afficher Correction

        // Afficher les boutons du bas (Recommencer / Changer)
        llEndButtons.setVisibility(View.VISIBLE);
    }

    /**
     * Réinitialise l'état et lance une nouvelle série d'opérations. (MODIFIÉ)
     */
    private void restartExercise() {
        Log.i("OperationActivity", "Redémarrage de l'exercice.");
        // Réinitialiser les compteurs
        currentOperationIndex = 0;
        score = 0;

        // Générer une NOUVELLE série d'opérations (inchangé)
        try {
            tableOperation = new TableOperation(currentOperationType, NOMBRE_OPERATIONS);
            // ... (logs et vérification) ...
        } catch (Exception e) {
            // ... (gestion erreur) ...
            return;
        }

        // Afficher la première opération de la nouvelle série
        // displayCurrentOperation va maintenant automatiquement configurer
        // la visibilité correcte des boutons (Valider visible, autres cachés).
        displayCurrentOperation();
    }

    private void launchCorrectionActivity() {
        // Vérifier si les données existent
        if (tableOperation == null || tableOperation.getOperations() == null) {
            Toast.makeText(this, "Impossible d'afficher la correction (pas de données).", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créer l'intent pour CorrectionActivity
        Intent intent = new Intent(OperationsActivity.this, CorrectionActivity.class);

        // Récupérer le tableau d'opérations depuis TableOperation
        Operation[] operations = tableOperation.getOperations();

        // ===> PAS DE CHANGEMENT NÉCESSAIRE ICI <===
        // Si Operation implements Serializable (et ses filles aussi),
        // Android sait comment mettre le tableau dans l'Intent.
        intent.putExtra("OPERATIONS_ARRAY", operations);
        // ===> FIN PAS DE CHANGEMENT NÉCESSAIRE <===


        Log.d("LaunchCorrection", "Passage de " + operations.length + " opérations (Serializable) à CorrectionActivity.");
        startActivity(intent); // Lancer l'activité de correction
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