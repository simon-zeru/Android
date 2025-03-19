package fr.iut.androidprojet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fr.iut.androidprojet.db.DatabaseClient;
import fr.iut.androidprojet.db.User;


public class AddUserActivity extends AppCompatActivity {

    // DATA
    private DatabaseClient mDb;

    // VIEW
    private EditText editTextFirstNameView;
    private EditText editTextLastNameView;
    private Button saveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        // Récupérer les vues
        editTextFirstNameView = findViewById(R.id.editTextFirstName);
        editTextLastNameView = findViewById(R.id.editTextLastName);
        saveView = findViewById(R.id.button_save);

        // Associer un événement au bouton save
        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });
    }

    private void saveUser() {

        // Récupérer les informations contenues dans les vues
        final String sFirstName = editTextFirstNameView.getText().toString().trim();
        final String sLastName = editTextLastNameView.getText().toString().trim();

        // Vérifier les informations fournies par l'utilisateur
        if (sFirstName.isEmpty()) {
            editTextFirstNameView.setError("First name required");
            editTextFirstNameView.requestFocus();
            return;
        }

        if (sLastName.isEmpty()) {
            editTextLastNameView.setError("Last name required");
            editTextLastNameView.requestFocus();
            return;
        }

        /**
         * Création d'une classe asynchrone pour sauvegarder la tache donnée par l'utilisateur
         */
        class SaveUser extends AsyncTask<Void, Void, User> {

            @Override
            protected User doInBackground(Void... voids) {

                // creating a task
                User user = new User();
                user.setPrenom(sFirstName);
                user.setNom(sLastName);

                // adding to database
                long id = mDb.getAppDatabase()
                        .userDao()
                        .insert(user);

                // mettre à jour l'id de la tache
                // Nécessaire si on souhaite avoir accès à l'id plus tard dans l'activité
                user.setId(id);


                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);

                // Quand l'utilisateur est créé, on arrête l'activité AddUserActivity (on l'enleve de la pile d'activités)
                setResult(RESULT_OK);
                finish();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        //////////////////////////
        // IMPORTANT bien penser à executer la demande asynchrone
        SaveUser su = new SaveUser();
        su.execute();
    }

}
