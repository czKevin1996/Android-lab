package com.example.kebo.lab7;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by kebo on 2017/12/12.
 */

public class FileEditorActivity extends AppCompatActivity {
    public Button save;
    public Button load;
    public Button clear2;
    public Button del;
    private EditText et1;
    private EditText et2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        save = (Button) findViewById(R.id.BtnSave);
        load = (Button) findViewById(R.id.BtnLoad);
        clear2 = (Button) findViewById(R.id.BtnClear2);
        del = (Button) findViewById(R.id.BtnDel);
        et1 = (EditText) findViewById(R.id.eT1);
        et2 = (EditText) findViewById(R.id.eT2);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try (FileOutputStream fileOutputStream = openFileOutput(et1.getText().toString(), MODE_PRIVATE)) {
                    String str = "";
                    str = et2.getText().toString();
                    fileOutputStream.write(str.getBytes());
                    Toast.makeText(FileEditorActivity.this, "Save successfully.", Toast.LENGTH_LONG).show();
                    Log.i("TAG", "Successfully saved file.");
                    return ;
                } catch (IOException ex) {
                    Log.e("TAG", "Fail to save file.");
                }
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try (FileInputStream fileInputStream = openFileInput(et1.getText().toString())) {
                    byte[] contents = new byte[fileInputStream.available()];

                    fileInputStream.read(contents);
                    et2.setText(new String(contents));
                    Toast.makeText(FileEditorActivity.this, "Load successfully.", Toast.LENGTH_LONG).show();
                    return ;
                } catch (IOException ex) {
                    Log.e("TAG", "Fail to read file.");
                    Toast.makeText(FileEditorActivity.this, "Fail to load file.", Toast.LENGTH_LONG).show();
                    return ;
                }
            }
        });

        clear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et2.setText("");
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getApplicationContext().deleteFile(et1.getText().toString())){
                    Toast.makeText(FileEditorActivity.this, "Delete Successfully",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(FileEditorActivity.this, "Fail to Delete", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
