package supinfo.com.supfitness.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import supinfo.com.supfitness.R;
import supinfo.com.supfitness.SQLite.Sqlite_Weight;
import supinfo.com.supfitness.Model.Weight;
import supinfo.com.supfitness.Adapter.WeightListAdapter;

public class WeightFragment extends Fragment implements View.OnClickListener {
    ListView listViewWeight;
    public static ArrayList<Weight> listWeight = new ArrayList();
    WeightListAdapter weightAdapter;
    Sqlite_Weight weightDB;

    public WeightFragment() {
        // Besoin d'un constructeur vide
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate le layout pour ce fragment
        View view =  inflater.inflate(R.layout.activity_weight_fragment, container, false);
        weightDB = new Sqlite_Weight(getActivity());

        //Récupère tous les poids de la BD pour les afficher
        SetList();

        //Création de l'adapteur pour la liste des poids
        weightAdapter = new WeightListAdapter(listWeight,getActivity(), this) {};
        listViewWeight = (ListView) view.findViewById(R.id.listWeight);
        listViewWeight.setAdapter(weightAdapter);

        //Button pour ajouter un poid
        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v){
        //Declaration des 3 éléments permettant de choisir son poid
        final TextView weight = new TextView(getActivity());
        Button btnAdd = new Button(getActivity());
        Button btnRemove = new Button(getActivity());

        //Création de l'alertDialog builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Please add your weight");

        //Création d'un LinearLayout pour afficher correctement les éléments
        LinearLayout linLayout = new LinearLayout(getActivity());
        linLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        layParam.gravity = Gravity.CENTER;
        linLayout.setLayoutParams(layParam);

        //Différent paramètre du boutton pour incrémenter le poid
        btnAdd.setText("+");
        btnAdd.setTypeface(null, Typeface.BOLD);
        btnAdd.setBackgroundColor(v.getResources().getColor(R.color.colorPrimaryDark));
        btnAdd.setTextColor(v.getResources().getColor(R.color.colorAccent));
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickChangeWeight(weight, "Add");
            }
        });
        //Ajout du boutton au LinearLayout
        linLayout.addView(btnAdd);

        //Différent paramètre pour l'affichage du poid sélectionné
        //Si l'utilisateur a déjà ajouté un poid, le récupère
        if(listWeight.size() != 0){
            //Récupération du dernier poid ajouté
            Weight lastWeight = listWeight.get(listWeight.size() - 1);
            weight.setText(lastWeight.getWeight().toString());
        }else{ // Sinon affiche 70 par défaut
            weight.setText("70.0");
        }
        weight.setGravity(Gravity.CENTER);
        weight.setTypeface(null, Typeface.BOLD);
        weight.setPadding(0, 100, 0, 100);
        weight.setTextColor(v.getResources().getColor(R.color.colorAccent));
        //Ajout du TextView au LinearLayout
        linLayout.addView(weight);

        //Différent paramètre du boutton pour décrémenter le poid
        btnRemove.setText("-");
        btnRemove.setTypeface(null, Typeface.BOLD);
        btnRemove.setTextColor(v.getResources().getColor(R.color.colorAccent));
        btnRemove.setBackgroundColor(v.getResources().getColor(R.color.colorPrimaryDark));
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickChangeWeight(weight,"Remove");
            }
        });
        //Ajout du boutton au LinearLayout
        linLayout.addView(btnRemove);

        //Ajout du boutton pour confirmer l'ajout du poid
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                confimAddWeight(weight);
            }
        });
        //Ajout du boutton pour annuler
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        //Création de l'AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        //Change la couleur de fond
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        //Ajoute le LinearLayout à la vue
        alertDialog.setView(linLayout);
        //Affiche l'alertDialog
        alertDialog.show();

        //Change l'aspect des buttons pour confirmer ou annuler
        Button positive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positive.setBackgroundColor(v.getResources().getColor(R.color.windowBackground));
        Button negative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negative.setBackgroundColor(v.getResources().getColor(R.color.windowBackground));
    }

    //Récupère tous les poids de la BD et les ajoute dans la liste
    public void SetList(){
        weightDB.open();
        listWeight = weightDB.getAllWeights();
        weightDB.close();
    }

    //Methode pour incrémenter ou décrémenter le poid afficher dans l'alerDialog
    public void clickChangeWeight(TextView weight, String btn){
        Double value;

        //Recupération de la valeur afficher en lui ajoutant 0.1 ou retirant 0.1 selon le boutton
        if(btn.equals("Add")){
            value = Double.parseDouble(weight.getText().toString()) + 0.1;
        }else{
            value = Double.parseDouble(weight.getText().toString()) - 0.1;
        }
        DecimalFormat df = new DecimalFormat("000.0");

        //Change le format de la valeur récupéré et l'affiche
        String formate = df.format(value);
        try {
            Number finalValue = df.parse(formate);
            weight.setText(String.valueOf(finalValue));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //Methode appelée quand l'utilisateur veut confirmer l'ajout d'un poid
    public void confimAddWeight(TextView weight){
        //Création du poid
        Weight w = new Weight();
        w.setDate();
        w.setWeight(Double.parseDouble(weight.getText().toString()));
        w.setId();

        //Si l'utilisateur a au moins ajouté un poid
        if (listWeight.size() > 0) {
            //Récupéation du dernier poid de l'utilisateur
            Weight lastWeight = listWeight.get(listWeight.size() - 1);

            //On récupère les deux date dans le même format
            SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            Date wDate = null;
            Date lastDate = null;
            Calendar c = Calendar.getInstance();

            try {
                wDate = format.parse(w.getDate());
                c.setTime(format.parse(lastWeight.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //On ajoute un jour au dernier poid enregistré par l'utilisateur
            c.add(Calendar.DATE, 1);
            lastDate = c.getTime();

            //Si l'utilisateur est 24h après le dernier poid enregistré
            if (wDate.after(lastDate)) {
                //Ajoute le poid à la BD
                AddWeight(w);
            } else {
                //Sinon affiche un message indiquant à l'utilisateur d'attendre avant d'ajouter un nouveau poid
                new AlertDialog.Builder(getActivity())
                        .setMessage("You have already add your weight today.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

            }
        } else {//Sinon l'utilisateur n'a jamais ajouté de poid
            //Ajoute le poid à la BD
            AddWeight(w);
        }
    }
    //Methode pour ajouter le poid à la BD
    public void AddWeight(Weight w){
        weightDB.open();
        weightDB.insertWeight(w);
        weightDB.close();

        //Ajoute également à la liste
        listWeight.add(w);
        //Et utilise le notify pour actualiser l'affichage de la liste
        weightAdapter.notifyDataSetChanged();
    }
}
