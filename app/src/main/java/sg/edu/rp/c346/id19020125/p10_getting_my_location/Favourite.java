package sg.edu.rp.c346.id19020125.p10_getting_my_location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Favourite extends AppCompatActivity {


    Button btnRefresh;
    TextView tvRecords;
    ListView lv;
    ArrayAdapter aa;
    ArrayList<String> al;
    ActionBar actionBar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        btnRefresh = findViewById(R.id.btnRefresh2);
        tvRecords = findViewById(R.id.tvNumOfFavRecords);
        lv = findViewById(R.id.lvFav);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        al = new ArrayList<>();

        String folder = getFilesDir().getAbsolutePath() + "/Locations";
        File file = new File(folder, "favourites.txt");
        if (file.exists()) {
            int numOfRecords = 0;
            try {
                FileReader reader = new FileReader(file);
                BufferedReader br = new BufferedReader(reader);

                String line = br.readLine();
                while (line != null) {
                    al.add(line);
                    line = br.readLine();
                    numOfRecords += 1;
                }
                aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al);
                lv.setAdapter(aa);
                tvRecords.setText("Number of records: " + numOfRecords);
                br.close();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            btnRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    al.clear();
                    File file = new File(folder, "favourites.txt");
                    if (file.exists()) {
                        int numOfRecords = 0;
                        try {
                            FileReader reader = new FileReader(file);
                            BufferedReader br = new BufferedReader(reader);

                            String line = br.readLine();
                            while (line != null) {
                                al.add(line);
                                line = br.readLine();
                                numOfRecords += 1;
                            }
                            aa = new ArrayAdapter(Favourite.this, android.R.layout.simple_list_item_1, al);
                            lv.setAdapter(aa);
                            tvRecords.setText("Number of records: " + numOfRecords);
                            br.close();
                            reader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

    }

}

