package com.example.projektlodowka.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.example.projektlodowka.R;

import java.io.ByteArrayOutputStream;

@Database(entities = {Produkt.class, Przepis.class, ProduktPrzepis.class, Minutnik.class, Historia.class, PoraDnia.class},
        version = 1, exportSchema = false)
public abstract class BazaDanych extends RoomDatabase {
    public abstract ProduktDao produktDao();
    public abstract PrzepisDao przepisDao();
    public abstract ProduktPrzepisDao produktPrzepisDao();
    public abstract MinutnikDao minutnikDao();
    public abstract HistoriaDao historiaDao();
    public abstract PoraDniaDao poraDniaDao();

    private static volatile Context c;
    private static volatile BazaDanych INSTANCE;


    public static BazaDanych getBazaDanych(final Context context) {
        if(INSTANCE == null) {
            synchronized (BazaDanych.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BazaDanych.class, "BazaDanych").addCallback(sRoomDatabaseCallback).build();
                }
            }
            c = context;
        }

        return INSTANCE;
    }

    public static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    public static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ProduktDao produktDao;
        private final PrzepisDao przepisDao;
        private final ProduktPrzepisDao produktPrzepisDao;

        public PopulateDbAsync(BazaDanych db) {
            produktDao = db.produktDao();
            przepisDao = db.przepisDao();
            produktPrzepisDao = db.produktPrzepisDao();
        }

        private void insertProduktPrzepis(String produktNazwa, String przepisNazwa, int ilosc, boolean opcjonalny ) {
            int produktId = produktDao.loadNazwa(produktNazwa).getId();
            int przepisId = przepisDao.loadNazwa(przepisNazwa).getId();
            produktPrzepisDao.insert(new ProduktPrzepis(przepisId, produktId, ilosc, opcjonalny));
        }

        private void insertProdukt(String nazwa, int typ, int ilosc) {

            produktDao.insert(new Produkt(nazwa, typ, ilosc));
        }

        private void insertPrzepis(String nazwa, int czas, String opis, int poraDnia) {
            przepisDao.insert(new Przepis(nazwa, czas, opis, poraDnia));
        }

        private void insertPrzepis(String nazwa, int czas, String opis, int poraDnia, Drawable obrazek)
        {
            Przepis p = new Przepis(nazwa,czas, opis, poraDnia);
            p.setImage(getBytesFromBitmap(obrazek));
            przepisDao.insert(p);
        }

        private void insertProdukt(String nazwa, int typ, int ilosc, Drawable obrazek){
            Produkt p = new Produkt(nazwa, typ, ilosc);
            p.setImage(getBytesFromBitmap(obrazek));
            produktDao.insert(p);
        }
        private byte[] getBytesFromBitmap(Drawable d) {
            Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();
        }




        @Override
        protected Void doInBackground(final Void... params) {
            if(produktDao.count() == 0 && przepisDao.count() == 0) {

                insertProdukt("ziemniaki", 0, 0, c.getResources().getDrawable(R.drawable.ziemniak));
                insertProdukt("cebula", 2, 0, c.getResources().getDrawable(R.drawable.cebula));
                insertProdukt("mleko", 1, 0, c.getResources().getDrawable(R.drawable.mleko));
                insertProdukt("ser biały", 0, 0, c.getResources().getDrawable(R.drawable.serbialy));
                insertProdukt("makaron", 2, 0, c.getResources().getDrawable(R.drawable.makaron));
                insertProdukt("jogurt naturalny", 1, 0, c.getResources().getDrawable(R.drawable.jogurtnaturalny));
                insertProdukt("ser żółty", 0, 0, c.getResources().getDrawable(R.drawable.serzolty));
                insertProdukt("parówki", 2,0, c.getResources().getDrawable(R.drawable.parowki));
                insertProdukt("chleb", 2, 0, c.getResources().getDrawable(R.drawable.chleb));
                insertProdukt("bułki", 2, 0, c.getResources().getDrawable(R.drawable.bulka));
                insertProdukt("jabłka", 2, 0, c.getResources().getDrawable(R.drawable.jablko));
                insertProdukt("pomarańcze", 2, 0, c.getResources().getDrawable(R.drawable.pomarancze));
                insertProdukt("awokado", 2, 0, c.getResources().getDrawable(R.drawable.awokado));
                insertProdukt("fasola czarna", 0, 0, c.getResources().getDrawable(R.drawable.fasolaczarna));
                insertProdukt("fasola biała", 0, 0, c.getResources().getDrawable(R.drawable.fasolabiala));
                insertProdukt("mięso mielone", 0, 0, c.getResources().getDrawable(R.drawable.miesomielone));
                insertProdukt("schab", 0, 0, c.getResources().getDrawable(R.drawable.schab));
                insertProdukt("kiełbasa", 0, 0, c.getResources().getDrawable(R.drawable.kielbasa));
                insertProdukt("szproty w pomidorach", 2, 0, c.getResources().getDrawable(R.drawable.szprot));
                insertProdukt("sałata", 2, 0, c.getResources().getDrawable(R.drawable.salata));
                insertProdukt("soczewica", 0, 0, c.getResources().getDrawable(R.drawable.soczewica));
                insertProdukt("kefir", 1, 0, c.getResources().getDrawable(R.drawable.kefir));
                insertProdukt("przecier pomidorowy", 1, 0, c.getResources().getDrawable(R.drawable.przecier));
                insertProdukt("zioła prowansalskie", 0, 0, c.getResources().getDrawable(R.drawable.ziola));
                insertProdukt("czosnek", 2,0);
                insertProdukt("śmietana", 1,0);
                insertProdukt("mąka", 0,0);
                insertProdukt("koperek", 0,0);
                insertProdukt("natka pietruszki", 0,0);
                insertProdukt("żeberko wołowe", 0,0);
                insertProdukt("marchewka", 2,0);
                insertProdukt("pietruszka", 2,0);
                insertProdukt("seler", 2,0);
                insertProdukt("majeranek", 0,0);
                insertProdukt("ketchup", 1,0);



                insertPrzepis("parówki z ketchupem", 10, "Wrzuć do gotującej się wody parówki. Gotuj pięć minut we wrzącej wodzie." +
                        "Podawaj z chlebem i keczupem. Ewentualnie można też użyć musztardy.", 0, c.getResources().getDrawable(R.drawable.parowki_m_jez));
                insertProduktPrzepis("parówki", "parówki z ketchupem", 4000, false);
                insertProduktPrzepis("bułki", "parówki z ketchupem", 1000, true);
                insertProduktPrzepis("ketchup","parówki z ketchupem", 2000, false);

                insertPrzepis("kanapki ze szprotami", 10, "Chleb posmaruj masłem oraz połóż na nim starannie " +
                        "rozgniecione widelcem szproty." + "Dla smaku możesz położyć na nich ser biały.", 4, c.getResources().getDrawable(R.drawable.kanapki_ze_szprot));
                insertProduktPrzepis("szproty w pomidorach", "kanapki ze szprotami", 1000, false);
                insertProduktPrzepis("chleb", "kanapki ze szprotami", 300, false);
                insertProduktPrzepis("ser biały", "kanapki ze szprotami", 900, true);

                insertPrzepis("kartofle z kefirem", 30, "Ziemniaki obierz a następnie gotuj dopóki nie zmiękną. " +
                        "W przypadku młodych ziemniaków pogruchotaj je, jeśli są stare utłucz je na puree. " +
                        "Posól wedle uznania oraz zalej kefirem. Udekoruj koperkiem dla smaku.", 2, c.getResources().getDrawable(R.drawable.kart_z_kef));
                insertProduktPrzepis("ziemniaki", "kartofle z kefirem", 700, false);
                insertProduktPrzepis("kefir", "kartofle z kefirem", 350, false);
                insertProduktPrzepis("koperek", "kartofle z kefirem", 100, true);


                insertPrzepis("spaghetti bolongese", 40, "Na patelni podgrzej olej i zeszlij cebulę oraz czosnek. Dodaj mięso mielone i podsmaż je. " + "Dodaj przecier pomidorowy i duś na wolnym ogniu." +
                        "Jeżeli masz ochotę dodaj zioła prowansalskie." + "Makaron wrzuć do gotującej się wody i gotuj na wolnym ogniu około 8 minut mieszając od czasu do " +
                        "Odcedź makaron i połóż go na talerzu wraz z sosem.", 2, c.getResources().getDrawable(R.drawable.spaghetti));
                insertProduktPrzepis("mięso mielone", "spaghetti bolongese", 200, false);
                insertProduktPrzepis("cebula", "spaghetti bolongese", 1000, false);
                insertProduktPrzepis("czosnek", "spaghetti bolongese", 400, false);
                insertProduktPrzepis("przecier pomidorowy", "spaghetti bolongese", 300, false);
                insertProduktPrzepis("makaron", "spaghetti bolongese", 250, false);
                insertProduktPrzepis("zioła prowansalskie", "spaghetti bolongese", 300, true);

                insertPrzepis("schab w sosie własnym", 60, "Schab pokroić w plastry o grubości 1-2 cm" +
                        "Kawałki mięsa położyć na suchej dobrze nagrzanej patelni; Mięso powinno prawie,że się przypalić, nabrać koloru mocno brązowego z obydwu stron, to właśnie te przypalone soki nadadzą wyjątkowego smaku sosowi;Proszę się nie martwić suchością i przypalenizną, podczas duszenia mięso nabierze konsystencji takiej,że będzie się rozpływać w buzi.\n" +
                        "Gdy mięso jest dość dobrze przyrumienione, dodać posiekaną cebulkę i zalać wodą tak by przykrywało mięso, doprawić solą i pieprzem do smaku; Przykryć pokrywką i dusić na małym ogniu ok 40-60 minut. W razie potrzeby dolać wody (gdyby się wygotowała)\n" +
                        "Gdy mięso jest mięciutkie, zagęścić sos łyżką mąki wymieszaną z 50 ml zimnej wody; Następnie dodać łyżkę śmietany, posiekaną natkę pietruszki,zamięszać i wyłączyć zródło ciepła;\n" +
                        "Sos i schab gotowy - smacznego!", 2, c.getResources().getDrawable(R.drawable.schab_w_sosie));
                insertProduktPrzepis("schab", "schab w sosie własnym", 500, false);
                insertProduktPrzepis("cebula", "schab w sosie własnym", 1000, false);
                insertProduktPrzepis("mąka", "schab w sosie własnym", 50, false);
                insertProduktPrzepis("śmietana", "schab w sosie własnym", 50, false);
                insertProduktPrzepis("natka pietruszki", "schab w sosie własnym", 1000, false);

                insertPrzepis("zupa fasolowa", 40, "Dzień wcześniej namoczyć fasolę: wsypać do garnka, zalać zimną wodą w ilości około 1 litra i odstawić na całą noc do napęcznienia.\n" +
                        "Następnego dnia odcedzić fasolę. Do dużego garnka włożyć pokrojone na 2 - 3 kawałki żeberka, wlać 2,5 litra wody, dodać fasolę oraz łyżeczkę soli. Zagotować, zszumować, zmniejszyć ogień i gotować pod przykryciem przez ok. 1 godzinę i 15 minut.\n" +
                        "Dodać 1 obraną marchewkę (drugą obrać, zetrzeć na tarce o dużych oczkach i odłożyć na później), obraną pietruszkę oraz kawałek korzenia selera. Gotować przez 15 minut.\n" +
                        "Włożyć na patelnię pokrojoną w kosteczkę cebulę i mieszając smażyć przez ok. 5 minut. Pod koniec dodać rozgnieciony czosnek. Przełożyć do garnka z wywarem wypłukując patelnię częścią wywaru.\n" +
                        "Do zupy dodać odłożoną startą marchewkę, doprawić świeżo zmielonym pieprzem i w razie potrzeby solą. Gotować 10 minut.\n" +
                        "Ziemniaki obrać i pokroić w kosteczkę, dodać do zupy. Gotować przez 1/2 godziny. Na koniec dodać majeranek i chwilę pogotować. Podawać z pieczywem.", 2, c.getResources().getDrawable(R.drawable.zupa_fasolowa));
                insertProduktPrzepis("ziemniaki", "zupa fasolowa", 200, false);
                insertProduktPrzepis("żeberko wołowe", "zupa fasolowa", 500, false);
                insertProduktPrzepis("marchewka", "zupa fasolowa", 100, false);
                insertProduktPrzepis("pietruszka", "zupa fasolowa", 100, false);
                insertProduktPrzepis("seler", "zupa fasolowa", 100, false);
                insertProduktPrzepis("ziemniaki", "zupa fasolowa", 200, false);
                insertProduktPrzepis("cebula", "zupa fasolowa", 1000, false);
                insertProduktPrzepis("czosnek", "zupa fasolowa", 400, false);
                insertProduktPrzepis("majeranek", "zupa fasolowa", 20, false);
                insertProduktPrzepis("fasola czarna", "zupa fasolowa", 200, false);
                insertProduktPrzepis("fasola biała", "zupa fasolowa", 200, false);

            }
            return null;
        }
    }
}

