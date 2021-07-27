package sg.edu.rp.c346.id19020125.p10_getting_my_location;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class CheckRecords extends AppCompatActivity {

    Button btnRefresh, btnFav;
    TextView tvRecords;
    ListView lv;
    ArrayAdapter aa;
    ArrayList<String> al;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_records);

        btnRefresh = findViewById(R.id.btnRefresh);
        btnFav = findViewById(R.id.btnFav);
        tvRecords = findViewById(R.id.tvNumOfRecords);
        lv = findViewById(R.id.lv);

        al = new ArrayList<>();

        builder = new AlertDialog.Builder(this);

        String folder = getFilesDir().getAbsolutePath() + "/Locations";
        File file = new File(folder, "locationData.txt");
        if (file.exists()) {
            int numOfRecords = 0;
            try {
                FileReader reader = new FileReader(file);
                BufferedReader br = new BufferedReader(reader);

                String line = br.readLine();
                while (line != null) {
                    al.add(line);
                    line = br.readLine();
                    numOfRecords+=1;
                }
                aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al);
                lv.setAdapter(aa);
                tvRecords.setText("Number of records: "+numOfRecords);
                br.close();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            btnRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    al.clear();
                    File file = new File(folder, "locationData.txt");
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
                            aa = new ArrayAdapter(CheckRecords.this, android.R.layout.simple_list_item_1, al);
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

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("Check", String.valueOf(position));
                    builder.setMessage("Add this location in your favourite list?")
                            .setCancelable(false)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String line = al.get(position);
                            try {
                                String folder = getFilesDir().getAbsolutePath() + "/Locations";
                                File file = new File(folder, "favourites.txt");
                                FileWriter writer = new FileWriter(file, true);
                                writer.write(line + "\n");
                                writer.flush();
                                writer.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            btnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(CheckRecords.this, Favourite.class);
                    startActivity(i);
                }
            });

        }
    }
}