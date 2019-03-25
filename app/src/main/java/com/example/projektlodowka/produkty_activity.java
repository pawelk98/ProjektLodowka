package com.example.projektlodowka;


import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projektlodowka.database.BazaDanych;
import com.example.projektlodowka.database.Produkt;

import java.util.List;


public class produkty_activity extends AppCompatActivity {

    BazaDanych baza;

    EditText produktNazwa;
    EditText produktIlosc;
    EditText produktTyp;
    Button produktDodaj;
    Spinner produktWybierz;

    Toast produktDodano;

    ProduktyAdapter produktyAdapter;
    ListView produktyLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_produkty_activity);

        baza = Room.databaseBuilder(getApplicationContext(), BazaDanych.class,
                "newDatabase").allowMainThreadQueries().build();
        produktNazwa = findViewById(R.id.produktNazwaEditText);
        produktIlosc = findViewById(R.id.produktIloscEditText);
        produktTyp = findViewById(R.id.produktTypEditText);
        produktDodaj = findViewById(R.id.produktDodajButton);
        produktyLista = findViewById(R.id.produktyLista);
        setListView();

        produktDodano = Toast.makeText(getApplicationContext(), "Dodano produkt", Toast.LENGTH_SHORT);

        produktDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!produktNazwa.getText().toString().trim().isEmpty() && !produktIlosc.getText().toString().trim().isEmpty()
                        && !produktTyp.getText().toString().trim().isEmpty()) {

                    Produkt p = new Produkt(produktNazwa.getText().toString(),
                            Integer.parseInt(produktTyp.getText().toString()),
                            Integer.parseInt(produktIlosc.getText().toString()));
                    baza.produktDao().insert(p);
                    setListView();

                    produktNazwa.setText("");
                    produktTyp.setText("");
                    produktIlosc.setText("");
                    produktDodano.show();
                }
            }
        });
    }

    void setListView() {
        List<Produkt> p = baza.produktDao().loadAllOrderNazwa();
        produktyAdapter = new ProduktyAdapter(getApplicationContext(), p);
        produktyLista.setAdapter(produktyAdapter);
    }
}
