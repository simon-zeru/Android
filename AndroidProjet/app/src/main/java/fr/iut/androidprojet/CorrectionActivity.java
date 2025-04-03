package fr.iut.androidprojet;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable; // Importer Serializable pour le cast

// Importer les modèles (qui doivent implémenter Serializable)
import fr.iut.androidprojet.modele.Operation;
// Importer l'adaptateur (assurez-vous qu'il existe dans le bon package)
// import fr.iut.androidprojet.adapter.CorrectionAdapter; // Adaptez le chemin si nécessaire

public class CorrectionActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCorrections;
    private CorrectionAdapter adapter;
    private Operation[] operationsArray; // Tableau pour stocker les opérations reçues

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correction);

        // --- Configuration de la Toolbar ---
        Toolbar toolbar = findViewById(R.id.toolbar_correction);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Afficher flèche retour
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // --- Configuration de la RecyclerView ---
        recyclerViewCorrections = findViewById(R.id.rv_corrections);
        // Définir un LayoutManager (comment les items sont arrangés : linéairement ici)
        recyclerViewCorrections.setLayoutManager(new LinearLayoutManager(this));
        // Optimisation si la taille des items ne change pas
        recyclerViewCorrections.setHasFixedSize(true);

        // --- Récupération des données depuis l'Intent ---
        Log.d("CorrectionActivity", "Tentative de récupération des données de l'intent.");
        if (getIntent() != null && getIntent().hasExtra("OPERATIONS_ARRAY")) {
            try {
                // Utiliser getSerializableExtra et caster en Operation[]
                Serializable data = getIntent().getSerializableExtra("OPERATIONS_ARRAY");
                if (data instanceof Operation[]) {
                    operationsArray = (Operation[]) data;
                    Log.i("CorrectionActivity", "Reçu " + operationsArray.length + " opérations via Serializable.");

                    // --- Création et liaison de l'Adaptateur ---
                    // Assurez-vous que CorrectionAdapter existe et est correctement implémenté
                    adapter = new CorrectionAdapter(this, operationsArray);
                    recyclerViewCorrections.setAdapter(adapter);

                } else {
                    // Si l'objet reçu n'est pas du type attendu
                    throw new ClassCastException("L'extra reçu n'est pas un Operation[]. Type reçu: " + (data != null ? data.getClass().getName() : "null"));
                }

            } catch (Exception e) { // Attraper ClassCastException ou autre
                Log.e("CorrectionActivity", "Erreur lors de la récupération/cast du tableau Serializable", e);
                handleError("Erreur de données reçues pour la correction.");
            }
        } else {
            Log.e("CorrectionActivity", "Aucun extra 'OPERATIONS_ARRAY' trouvé dans l'intent.");
            handleError("Aucune donnée d'opération fournie à l'activité.");
        }
    }

    // --- Gérer le clic sur la flèche retour de la Toolbar ---
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Ferme l'activité et retourne à OperationsActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Gère les erreurs de démarrage ou de données.
     * @param message Message d'erreur à afficher.
     */
    private void handleError(String message) {
        Log.e("CorrectionActivity", "Erreur: " + message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish(); // Fermer l'activité si elle ne peut pas afficher les données
    }
}