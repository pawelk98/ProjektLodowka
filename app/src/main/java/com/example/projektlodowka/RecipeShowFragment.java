package com.example.projektlodowka;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.projektlodowka.database.ProduktInPrzepis;
import com.example.projektlodowka.database.Przepis;
import com.example.projektlodowka.database.ViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeShowFragment extends Fragment {

    int id;
    String przepisName;
    TextView nazwa;
    TextView czas;
    TextView opis;
    ViewModel viewModel;
    Button gotuj;
    ListView produktyInPrzepisy;
    ProduktyInPrzepisAdapter produktyInPrzepisAdapter;


    public RecipeShowFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
            przepisName = getArguments().getString("przepisName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nazwa = view.findViewById(R.id.przepisShowNazwaTextView);
        czas = view.findViewById(R.id.przepisShowCzasTextView);
        opis = view.findViewById(R.id.przepisShowOpisTextView);
        gotuj = view.findViewById(R.id.cookNow);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.setShowPrzepis(getActivity(),id);
        produktyInPrzepisy = view.findViewById(R.id.przepisSkladnikListView);
        produktyInPrzepisAdapter = new ProduktyInPrzepisAdapter(getActivity());
        produktyInPrzepisy.setAdapter(produktyInPrzepisAdapter);

        viewModel.getProduktyInPrzepis(id).observe(this, new Observer<List<ProduktInPrzepis>>() {
            @Override
            public void onChanged(@Nullable List<ProduktInPrzepis> produktInPrzepis) {
                produktyInPrzepisAdapter.setProduktInPzepis(produktInPrzepis);
                produktyInPrzepisy.setAdapter(produktyInPrzepisAdapter);
            }
        });

        gotuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.insertProduktPrzepisByName(przepisName,"cebula",1000,false);
            }
        });

    }
}
