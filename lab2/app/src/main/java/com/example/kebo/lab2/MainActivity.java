package com.example.kebo.lab2;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static android.media.MediaRecorder.VideoSource.CAMERA;
import static com.example.kebo.lab2.R.id.editText1;
import static com.example.kebo.lab2.R.id.image;

public class MainActivity extends AppCompatActivity {
    String[] strArray={"拍摄","从相册选择"};
    private RadioGroup mRadioGroup=null;
    private RadioButton studentbutton=null;
    private RadioButton teacherbutton=null;
    private ImageView mImage=null;
    private Button mLogin=null;
    private EditText number=null;
    private EditText password=null;
    private TextInputLayout number1=null;
    private TextInputLayout password1=null;
    private Button mRegister=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImage= (ImageView) findViewById(R.id.imageView);
        mImage.setOnClickListener(new View.OnClickListener(){
//            监听ImageView的点击事件，如果点击事件发生，调用onClick方法
            @Override
            public void onClick(View v){
                new AlertDialog.Builder(MainActivity.this) //根据要求，使用弹出对话框
                        .setTitle("上传头像")
                        .setItems(strArray, new DialogInterface.OnClickListener() {
                            @Override   //在弹出对话框中继续监听点击事件，调用onClick方法
                            public void onClick(DialogInterface dialog, int swhich) {
                                if(swhich==0){  //点击"拍摄"，弹出Toast，用Intent唤起设备相机进行拍摄
                                    Toast.makeText(MainActivity.this,"您选择了[拍摄]",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //额外功能
                                    startActivityForResult(intent, CAMERA); //只唤起了相机，未实现照片的使用
                                }
                                if(swhich==1){  //点击"从相册选择"，弹出Toast
                                    Toast.makeText(MainActivity.this,"您选择了[从相册选择]",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
//                        点击"取消"
                        .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog,int which){
                                Toast.makeText(MainActivity.this,"您选择了[取消]",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();    //.show()方法显示对话框

            }
        });
        mRadioGroup =(RadioGroup)findViewById(R.id.radioGroup); //引入布局元素控件
        studentbutton =(RadioButton)findViewById(R.id.radioButton1);
        teacherbutton =(RadioButton)findViewById(R.id.radioButton2);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == studentbutton.getId()) {   //选中项为"学生"
//                    弹出snackbar消息"您选择了学生"
                    Snackbar.make(mRadioGroup,"您选择了学生", Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {   //点击snackbar上的"确定"后弹出Toast
                                    Toast.makeText(MainActivity.this,"Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
                else if (checkedId == teacherbutton.getId()) {  //选中项为"教职工"，其他与上同
                    Snackbar.make(mRadioGroup,"您选择了教职工", Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(MainActivity.this,"Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
            }
        });
        mLogin=(Button)findViewById(R.id.button1);
        number=(EditText)findViewById(R.id.editText1);
        password=(EditText)findViewById(R.id.editText2);
        number1=(TextInputLayout)findViewById(R.id.editText1p);
        password1=(TextInputLayout)findViewById(R.id.editText2p);
        mLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String n=number.getText().toString();   //获取用户输入的用户名和密码
                String p=password.getText().toString();
                if (TextUtils.isEmpty(n)){
                    number1.setError("学号不能为空"); //TextInputLayout的setError方法显示报错信息
                }
                if (TextUtils.isEmpty((p))){
                    password1.setError("密码不能为空");
                }
                else if (n.equals("123456") & p.equals("6666")){    //如果输入正确的密码
                    number1.setErrorEnabled(false); //设置提示信息消失
                    password1.setErrorEnabled(false);
                    Snackbar.make(mRadioGroup,"登录成功", Snackbar.LENGTH_SHORT)    //弹出snackbar消息
                            .setAction("确定", new View.OnClickListener() {   //设置"确定"按钮动作
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(MainActivity.this,"Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
                else {  //输入错误的密码，弹出snackbar消息
                    Snackbar.make(mRadioGroup,"学号或密码错误", Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(MainActivity.this,"Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
            }
        });
        mRegister=(Button)findViewById(R.id.button2);
        mRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){    //监听点击事件
                if (mRadioGroup.getCheckedRadioButtonId() == studentbutton.getId()) {
                    //如果以"学生"点击"注册"，弹出snackbar信息
                    Snackbar.make(mRadioGroup, "学生注册功能尚未启用", Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(MainActivity.this, "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
                //如果以"教职工"点击"注册"，弹出Toast信息
                else if (mRadioGroup.getCheckedRadioButtonId() == teacherbutton.getId()) {
                    Toast.makeText(MainActivity.this, "教职工注册功能尚未启用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
