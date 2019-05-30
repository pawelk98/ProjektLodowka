package com.example.projektlodowka;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.projektlodowka.database.ViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeShowFragment extends Fragment {

    int id;
    TextView nazwa;
    TextView czas;
    TextView opis;
    Button edytuj;
    Button gotuj;
    ViewModel viewModel;

    public RecipeShowFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
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
        edytuj = view.findViewById(R.id.editRecipe);
        gotuj = view.findViewById(R.id.cookNow);

        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.setShowPrzepis(getActivity(),id);

        edytuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                RecipeEditFragment fragment = new RecipeEditFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame, fragment);
                fragmentTransaction.addToBackStack("przepisyShowcase").commit();
            }
        });
    }
}
