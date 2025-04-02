package fr.iut.androidprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // Pour gérer le bouton retour de la toolbar
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Utiliser androidx.appcompat.widget.Toolbar

// Importer l'enum
import fr.iut.androidprojet.modele.OperationEnum;

public class OperationsMenuActivity extends AppCompatActivity {

    // Clé pour l'Intent Extra
    public static final String EXTRA_OPERATION_TYPE = "OPERATION_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations_menu);

        // Configurer la Toolbar (optionnel)
        Toolbar toolbar = findViewById(R.id.toolbar_operations);
        setSupportActionBar(toolbar);
        // Afficher le bouton retour dans la Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        // Récupérer les boutons
        Button btnAddition = findViewById(R.id.btn_addition);
        Button btnSoustraction = findViewById(R.id.btn_soustraction);
        Button btnMultiplication = findViewById(R.id.btn_multiplication);
        Button btnDivision = findViewById(R.id.btn_division);

        // Définir les listeners
        btnAddition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchOperationActivity(OperationEnum.ADDITION);
            }
        });

        btnSoustraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchOperationActivity(OperationEnum.SOUSTRACTION);
            }
        });

        btnMultiplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchOperationActivity(OperationEnum.MULTIPLICATION);
            }
        });

        btnDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchOperationActivity(OperationEnum.DIVISION);
            }
        });
    }

    /**
     * Lance OperationActivity en passant le type d'opération choisi.
     * @param type Le type d'opération (enum) sélectionné.
     */
    private void launchOperationActivity(OperationEnum type) {
        Intent intent = new Intent(OperationsMenuActivity.this, OperationsActivity.class);
        // Passer l'enum comme extra (il est Serializable par défaut)
        intent.putExtra(EXTRA_OPERATION_TYPE, type);
        startActivity(intent);
    }

    // Gérer le clic sur le bouton retour de la toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Gérer la flèche retour
        if (item.getItemId() == android.R.id.home) {
            finish(); // Ferme l'activité actuelle et retourne à la précédente (MenuActivity)
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}