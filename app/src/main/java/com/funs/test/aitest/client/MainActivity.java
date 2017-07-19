package com.funs.test.aitest.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.funs.test.aitest.aidl.Book;
import com.funs.test.aitest.aidl.IBookManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.size_button)
    Button sizeButton;
    @BindView(R.id.id_et)
    EditText idEt;
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        final Intent intent = new Intent();
        intent.setAction("aitest.bookService.action");
        //从 Android 5.0开始 隐式Intent绑定服务的方式已不能使用,所以这里需要设置Service所在服务端的包名
        intent.setPackage("com.funs.test.aitest.service");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);


    }

    @OnClick({R.id.button,R.id.size_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.size_button:
                try {
                    idEt.setText(iBookManager.getName(idEt.getText().toString().trim()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button:
                try {
                    List<Book> bookList = iBookManager.getBookList();
                    StringBuffer stringBuffer = new StringBuffer();
                    if(bookList != null){
                        for(int i = 0 ; i < bookList.size() ; i++){
                            Book b = bookList.get(i);
                            stringBuffer.append(" id "+b.getBookId()+" name " + b.getBookName()+"\n");
                        }
                        textView.setText(stringBuffer.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /////////////////////////////////////////
    IBookManager iBookManager;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBookManager = IBookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
