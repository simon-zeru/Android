package fr.iut.androidprojet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;
// Imports de votre projet
import fr.iut.androidprojet.db.DatabaseClient;
import fr.iut.androidprojet.db.User;

// ---=== IMPORTS STATIQUES POUR LES CONSTANTES ===---
import static fr.iut.androidprojet.MainActivity.PREFS_NAME;
import static fr.iut.androidprojet.MainActivity.KEY_USER_ID;
import static fr.iut.androidprojet.MainActivity.NO_USER_LOGGED_IN;

public class MenuActivity extends AppCompatActivity {

    private DatabaseClient mDb;
    private User currentUser;
    // On utilise directement la constante importée pour l'initialisation
    private long currentUserId = NO_USER_LOGGED_IN;

    // Références aux vues
    private TextView welcomeTextView;
    private Button logoutButton;
    private Button mathButton;
    private Button cultureButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        mDb = DatabaseClient.getInstance(getApplicationContext());

        toolbar = findViewById(R.id.toolbar);
        welcomeTextView = findViewById(R.id.tv_welcome_message);
        logoutButton = findViewById(R.id.btn_logout);
        mathButton = findViewById(R.id.button_addition);
        cultureButton = findViewById(R.id.button_multiplication);

        // --- Récupérer l'utilisateur depuis les SharedPreferences ---
        // On utilise directement les constantes importées PREFS_NAME, KEY_USER_ID, NO_USER_LOGGED_IN
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentUserId = sharedPreferences.getLong(KEY_USER_ID, NO_USER_LOGGED_IN);

        // Vérifier si un utilisateur est connecté
        if (currentUserId != NO_USER_LOGGED_IN) { // Utilisation de NO_USER_LOGGED_IN importé
            Log.i("MenuActivity", "Session trouvée pour l'utilisateur ID: " + currentUserId);
            loadUserData(currentUserId);
        } else {
            Log.w("MenuActivity", "MenuActivity démarrée sans session valide ! Redirection vers MainActivity.");
            Toast.makeText(this, "Aucune session active.", Toast.LENGTH_LONG).show();
            redirectToMain();
        }

        // --- Configuration du bouton de déconnexion ---
        logoutButton.setOnClickListener(v -> performLogout());

        // --- Configuration des boutons d'activités ---
        mathButton.setOnClickListener(v -> {
            Log.d("MenuActivity", "Lancement du menu des opérations mathématiques.");
            Intent intent = new Intent(MenuActivity.this, OperationsMenuActivity.class);
            startActivity(intent);
        });

        cultureButton.setOnClickListener(v -> {
            Toast.makeText(MenuActivity.this, "Lancement Culture G pour " + currentUser.getPrenom(), Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(MenuActivity.this, CultureActivity.class);
            // startActivity(intent);
        });

        // Gestion des Insets pour EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Charge les données de l'utilisateur depuis la base de données en utilisant AsyncTask.
     * Met à jour l'interface utilisateur avant (état de chargement) et après (résultat).
     * @param userId L'ID de l'utilisateur à charger.
     */
    private void loadUserData(long userId) {

        // --- 1. Mettre l'UI en état de "chargement" ---
        // Griser les boutons d'action pour éviter les clics pendant le chargement
        if (mathButton != null) { // Ajouter des vérifications null au cas où
            mathButton.setEnabled(false);
        }
        if (cultureButton != null) {
            cultureButton.setEnabled(false);
        }
        // Afficher un message temporaire dans le TextView de bienvenue
        if (welcomeTextView != null) {
            // Assurez-vous que R.string.loading_user_data est défini dans strings.xml
            welcomeTextView.setText(R.string.loading_user_data);
        }

        // --- 2. Définir la classe pour la tâche asynchrone ---
        /**
         * Tâche Asynchrone pour récupérer un objet User depuis la DB par son ID.
         */
        class GetUserTask extends AsyncTask<Long, Void, User> {

            /**
             * Code exécuté en arrière-plan (pas sur le thread UI).
             * @param userIds L'ID de l'utilisateur (passé via .execute()).
             * @return L'objet User trouvé, ou null si non trouvé ou en cas d'erreur.
             */
            @Override
            protected User doInBackground(Long... userIds) {
                // Vérifier si un ID a été passé
                if (userIds == null || userIds.length == 0) {
                    Log.e("GetUserTask", "Aucun ID utilisateur fourni à doInBackground.");
                    return null;
                }
                long idToFetch = userIds[0];

                // Vérifier si l'instance de base de données est prête
                if (mDb == null || mDb.getAppDatabase() == null || mDb.getAppDatabase().userDao() == null) {
                    Log.e("GetUserTask", "DatabaseClient ou composants non initialisés !");
                    return null;
                }

                // Accéder à la base de données (dans un try-catch pour la robustesse)
                try {
                    Log.d("GetUserTask", "Tentative de récupération de l'utilisateur ID: " + idToFetch);
                    // Assurez-vous que votre UserDao a la méthode getUserById(int userId)
                    User user = mDb.getAppDatabase().userDao().read(idToFetch);
                    if (user == null) {
                        Log.w("GetUserTask", "Aucun utilisateur trouvé dans la DB pour l'ID: " + idToFetch);
                    }
                    return user; // Retourne l'utilisateur trouvé ou null si non trouvé
                } catch (Exception e) {
                    Log.e("GetUserTask", "Erreur d'accès à la base de données pendant getUserById", e);
                    return null; // Retourne null en cas d'exception
                }
            }

            /**
             * Code exécuté sur le thread UI APRÈS la fin de doInBackground.
             * @param user L'objet User retourné par doInBackground (peut être null).
             */
            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);

                // Vérifier si un utilisateur a été retourné
                if (user != null) {
                    // Succès ! Stocker l'utilisateur et mettre à jour l'UI
                    currentUser = user; // Stocker l'utilisateur dans la variable membre
                    Log.i("MenuActivity", "Utilisateur chargé avec succès: " + currentUser.getPrenom());

                    // Mettre à jour le message de bienvenue
                    if (welcomeTextView != null) {
                        welcomeTextView.setText(getString(R.string.bienvenue, currentUser.getPrenom()));
                    }

                    // Réactiver les boutons d'action
                    if (mathButton != null) {
                        mathButton.setEnabled(true);
                    }
                    if (cultureButton != null) {
                        cultureButton.setEnabled(true);
                    }

                } else {
                    // Échec : Utilisateur non trouvé dans la DB ou erreur DB.
                    // currentUser reste null.
                    Log.e("MenuActivity", "Échec du chargement de l'utilisateur pour l'ID: " + currentUserId + " (User est null après AsyncTask).");

                    // Afficher un message d'erreur persistant
                    if (welcomeTextView != null) {
                        welcomeTextView.setText("Erreur de chargement du profil."); // Ou utiliser une string resource
                    }
                    Toast.makeText(MenuActivity.this, "Erreur critique: Données utilisateur introuvables.", Toast.LENGTH_LONG).show();

                    // Puisque l'état est incohérent (ID en session mais pas d'utilisateur en DB),
                    // il est plus sûr de déconnecter l'utilisateur.
                    performLogout();
                }
            }
        } // Fin de la définition de la classe GetUserTask

        // --- 3. Créer une instance de la tâche et l'exécuter ---
        Log.d("MenuActivity", "Lancement de GetUserTask pour l'ID: " + userId);
        new GetUserTask().execute(userId);

    }

    private void performLogout() {
        Log.i("MenuActivity", "Déconnexion demandée.");
        // Utilisation directe des constantes importées PREFS_NAME, KEY_USER_ID
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER_ID);
        editor.apply();
        Toast.makeText(this, "Vous avez été déconnecté.", Toast.LENGTH_SHORT).show();
        redirectToMain();
    }

    private void redirectToMain() {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
