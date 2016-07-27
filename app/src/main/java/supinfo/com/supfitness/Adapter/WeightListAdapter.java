package supinfo.com.supfitness.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import supinfo.com.supfitness.Fragment.WeightFragment;
import supinfo.com.supfitness.R;
import supinfo.com.supfitness.SQLite.Sqlite_Weight;
import supinfo.com.supfitness.Model.Weight;

public class WeightListAdapter extends BaseAdapter {

    private List<Weight> weights;
    private WeightFragment fragment;
    View view;
    LayoutInflater layoutInflater;
    Context context;
    Sqlite_Weight weightDB;

    public WeightListAdapter(List<Weight> weights, Context context, WeightFragment fragment) {
        this.weights = weights;
        this.fragment = fragment;
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return weights.size();
    }

    @Override
    public Object getItem(int position) {
        return weights.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            view = layoutInflater.inflate(R.layout.list_weight, parent, false);
        }else{
            view = convertView;
        }

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.weightLayout);
        TextView date = (TextView) view.findViewById(R.id.date);
        final TextView weight = (TextView) view.findViewById(R.id.weight);
        Button btnDelete = (Button) view.findViewById(R.id.btnDelete);

        //Récuperation du poid
        final Weight item = (Weight) getItem(position);

        //Assigne les différents parramètre du poid
        date.setText(item.getDate());
        weight.setText(item.getWeight().toString());
        weight.setTypeface(null, Typeface.BOLD);
        weight.setTextColor(fragment.getResources().getColor(R.color.colorAccent));
        date.setTextColor(fragment.getResources().getColor(R.color.colorAccent));

        //Change le fond du TextView un poid sur deux
        if(position %2 == 0)
            linearLayout.setBackgroundResource(R.color.colorPrimaryDark);
        else
            linearLayout.setBackgroundResource(R.color.colorPrimary);

        //Boutton pour supprimer un poid
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Création de l'alertDialog builder
                AlertDialog.Builder alertDailogBuilder = new AlertDialog.Builder(context);
                alertDailogBuilder.setMessage("Are you sure to delete this weight ?");
                alertDailogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Suppression du poid dans la liste et la BD
                        RemoveWeight(item, position);

                    }
                });
                alertDailogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                //Affiche le poid sélectionné désirant être supprimer
                TextView textView = new TextView(context);
                textView.setText(item.getDate().toString() + " Weight : " + item.getWeight().toString());
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(0, 100, 0, 0);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setTextColor(v.getResources().getColor(R.color.colorAccent));
                //Ajoute le TextView à l'alertDialog
                alertDailogBuilder.setView(textView);

                //Création de l'AlertDialog
                AlertDialog alertDialog = alertDailogBuilder.create();
                //Change la couleur de fond
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
                //Affiche l'alertDialog
                alertDialog.show();

                //Change l'aspect des buttons pour confirmer ou annuler
                Button positive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positive.setBackgroundColor(v.getResources().getColor(R.color.windowBackground));
                Button negative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setBackgroundColor(v.getResources().getColor(R.color.windowBackground));
            }
        });

        return view;
    }

    //Methode pour la suppression du poid dans la liste et la BD
    public void RemoveWeight(Weight w, int position){
        weightDB = new Sqlite_Weight(context);
        weightDB.open();
        weightDB.removeWeight(w.getId());
        weightDB.close();

        WeightFragment.listWeight.remove(position);
        //Utilise le notify pour actualiser l'affichage de la liste
        notifyDataSetChanged();
    }
}
