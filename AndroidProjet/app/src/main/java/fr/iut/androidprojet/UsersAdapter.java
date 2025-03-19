package fr.iut.androidprojet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.iut.androidprojet.db.User;


public class UsersAdapter extends ArrayAdapter<User> {

    public UsersAdapter(Context mCtx, List<User> taskList) {
        super(mCtx, R.layout.template_user, taskList);
    }

    /**
     * Remplit une ligne de la listView avec les informations de la multiplication associée
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Récupération de la multiplication
        final User user = getItem(position);

        // Charge le template XML
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.template_user, parent, false);

        // Récupération des objets graphiques dans le template
        TextView textViewFirstName = (TextView) rowView.findViewById(R.id.textViewFirstName);
        TextView textViewLastName = (TextView) rowView.findViewById(R.id.textViewLastName);

        //
        textViewFirstName.setText(user.getPrenom());
        textViewLastName.setText(user.getNom());

        //
        return rowView;
    }

}