package com.example.dell.rxjava;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.w3c.dom.Text;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnRxJava;
    private TextView tvShowMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
    }

    private void init() {
        btnRxJava.setOnClickListener(this);
    }

    private void initView() {
       btnRxJava=(Button)findViewById(R.id.btn_RxJava);
        tvShowMsg=(TextView)findViewById(R.id.tv_show_msg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_RxJava:
                rxJava();
                break;
        }
    }

    private void rxJava() {
        Flowable<String> flowable=Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> emitter) throws Exception {
                int i=0;
                while (true){
                    emitter.onNext(String.valueOf(i++));
                    SystemClock.sleep(500);
                }
            }
        }, BackpressureStrategy.BUFFER);
        Subscriber<String> subscriber=new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
              tvShowMsg.setText(s);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }
}
