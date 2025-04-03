package fr.iut.androidprojet; // Ou votre package adapter

import android.content.Context;
import android.graphics.Color;
import android.util.Log; // Pour les logs de debug
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat; // Pour récupérer les couleurs des ressources si besoin
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

// Importer les modèles (qui doivent implémenter Serializable)
import fr.iut.androidprojet.modele.Operation;

/**
 * Adaptateur pour afficher la liste des corrections dans une RecyclerView.
 */
public class CorrectionAdapter extends RecyclerView.Adapter<CorrectionAdapter.CorrectionViewHolder> {

    private Operation[] operations; // Le tableau des opérations à afficher
    private Context context;       // Contexte pour accéder aux ressources (optionnel mais utile)

    /**
     * Constructeur de l'adaptateur.
     * @param context Le contexte de l'application/activité.
     * @param operations Le tableau des opérations à afficher.
     */
    public CorrectionAdapter(Context context, Operation[] operations) {
        this.context = context;
        // Copier le tableau ou utiliser une vérification null pour la sécurité
        this.operations = (operations != null) ? operations : new Operation[0];
        Log.d("CorrectionAdapter", "Adapter créé avec " + this.operations.length + " opérations.");
    }

    /**
     * Crée un nouveau ViewHolder lorsque RecyclerView en a besoin.
     * @param parent Le ViewGroup dans lequel la nouvelle vue sera ajoutée.
     * @param viewType Le type de vue (non utilisé ici car un seul type d'item).
     * @return Un nouveau CorrectionViewHolder qui contient la vue pour un item.
     */
    @NonNull
    @Override
    public CorrectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflater le layout XML pour un seul item de la liste
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_correction, parent, false);
        return new CorrectionViewHolder(view);
    }

    /**
     * Lie les données d'une opération spécifique à un ViewHolder.
     * @param holder Le ViewHolder qui doit être mis à jour.
     * @param position La position de l'item dans le jeu de données.
     */
    @Override
    public void onBindViewHolder(@NonNull CorrectionViewHolder holder, int position) {
        // Récupérer l'objet Operation pour cette position
        Operation operation = operations[position];

        // Vérifier si l'opération est valide (sécurité)
        if (operation == null) {
            holder.tvOperation.setText("Erreur: Donnée invalide");
            holder.tvUserAnswer.setVisibility(View.GONE);
            holder.tvStatus.setVisibility(View.GONE);
            holder.tvCorrectAnswer.setVisibility(View.GONE);
            Log.e("CorrectionAdapter", "Opération null trouvée à la position: " + position);
            return;
        }

        // 1. Afficher l'opération (en utilisant toString() corrigé)
        holder.tvOperation.setText(operation.toString());

        // 2. Afficher la réponse de l'utilisateur
        Double userAnswer = operation.getReponseUtilisateur();
        if (userAnswer != null) {
            // Formatter la réponse utilisateur
            holder.tvUserAnswer.setText(String.format(Locale.getDefault(), "Votre réponse : %.2f", userAnswer));
            holder.tvUserAnswer.setVisibility(View.VISIBLE);
        } else {
            // Si l'utilisateur n'a pas répondu (ou si setReponseUtilisateur n'a pas été appelé)
            holder.tvUserAnswer.setText("Votre réponse : --"); // Ou "Non répondu"
            holder.tvUserAnswer.setVisibility(View.VISIBLE); // Garder visible pour l'alignement
        }

        // 3. Afficher le statut (Correct/Incorrect) et la bonne réponse si nécessaire
        holder.tvStatus.setVisibility(View.VISIBLE); // Le statut est toujours visible
        if (operation.isReponseJuste()) {
            holder.tvStatus.setText("Correct !");
            // Utiliser les couleurs prédéfinies ou les ressources de couleur
            // holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.correct_green)); // Si vous définissez une couleur
            holder.tvStatus.setTextColor(Color.parseColor("#006400")); // Vert foncé

            // Cacher le TextView de la réponse correcte si la réponse est juste
            holder.tvCorrectAnswer.setVisibility(View.GONE);
        } else {
            holder.tvStatus.setText("Incorrect");
            holder.tvStatus.setTextColor(Color.RED);

            // Calculer et afficher la réponse correcte
            try {
                double correctAnswer = operation.calculResultat();
                holder.tvCorrectAnswer.setText(String.format(Locale.getDefault(), "Réponse correcte : %.2f", correctAnswer));
                holder.tvCorrectAnswer.setVisibility(View.VISIBLE); // Afficher seulement si incorrect
            } catch (ArithmeticException e) {
                // Gérer le cas où même le calcul de la réponse correcte échoue (ex: division par zéro si pas filtré avant)
                Log.e("CorrectionAdapter", "Erreur calcul résultat pour correction: " + operation.toString(), e);
                holder.tvCorrectAnswer.setText("Réponse correcte : Erreur");
                holder.tvCorrectAnswer.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Retourne le nombre total d'items dans le jeu de données.
     * @return Le nombre d'opérations.
     */
    @Override
    public int getItemCount() {
        return operations.length;
    }


    /**
     * ViewHolder : Contient les références aux vues pour un seul item de la liste.
     * Cela évite les appels répétés à findViewById() lors du défilement.
     */
    public static class CorrectionViewHolder extends RecyclerView.ViewHolder {
        // Les TextViews définis dans list_item_correction.xml
        TextView tvOperation;
        TextView tvUserAnswer;
        TextView tvStatus;
        TextView tvCorrectAnswer;

        public CorrectionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Lier les variables aux vues via leurs IDs
            tvOperation = itemView.findViewById(R.id.tv_correction_operation);
            tvUserAnswer = itemView.findViewById(R.id.tv_correction_user_answer);
            tvStatus = itemView.findViewById(R.id.tv_correction_status);
            tvCorrectAnswer = itemView.findViewById(R.id.tv_correction_correct_answer);
        }
    }
}