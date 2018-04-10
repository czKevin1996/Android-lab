package com.example.kebo.lab8;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddInfoActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_info);

        findViewById(R.id.add).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.add){
            EditText editText1=(EditText)findViewById(R.id.name_edit);
            EditText editText2=(EditText)findViewById(R.id.birthday_edit);
            EditText editText3=(EditText)findViewById(R.id.gift_edit);
            String name=editText1.getText().toString();
            String birth=editText2.getText().toString();
            String gift=editText3.getText().toString();
            if(name.length()==0){
                Toast.makeText(AddInfoActivity.this,"姓名不可为空",Toast.LENGTH_SHORT).show();
            }
            else{
                boolean flag=MainActivity.myDatabase.insert(name,birth,gift);
                if(flag){
                    AddInfoActivity.this.finish();
                }
                else Toast.makeText(AddInfoActivity.this,"姓名重复，插入失败，请重新检查",Toast.LENGTH_SHORT).show();
            }
        }
    }
}