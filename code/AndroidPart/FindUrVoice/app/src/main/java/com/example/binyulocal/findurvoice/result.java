package com.example.binyulocal.findurvoice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class result extends AppCompatActivity {
    private Button back_bt;
    private FileInputStream is;
    private BufferedReader reader;
    private ArrayList arrayList;
    private ArrayAdapter adapter;
    private ListView listView;
    final static String ResultName = "result.txt";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        back_bt = (Button)findViewById(R.id.back_bt);
        arrayList = new ArrayList();

        //back to main
        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        try {
            String filePath = getFilesDir().getPath().toString() + "/"+ResultName;
            File file = new File(filePath);
            is = new FileInputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        //read result file line by line
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(line != null)
        arrayList.add(line);

        while(line != null) {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(line!= null) {
                arrayList.add(line);
                System.out.println(line);
            }

        }
        //show result
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,arrayList);
        listView = (ListView) findViewById(R.id.lv);
        listView.setAdapter(adapter);

    }







}