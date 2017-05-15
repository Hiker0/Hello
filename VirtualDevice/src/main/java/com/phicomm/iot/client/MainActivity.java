package com.phicomm.iot.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button switcher = (Button) findViewById(R.id.switcher);
        Button light = (Button) findViewById(R.id.light);

        switcher.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SwitcherActivity.class);
                startActivity(intent);
            }
        });

        light.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LightActivity.class);
                startActivity(intent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress("192.168.2.223", 5858), 5000);
                    System.out.println("192.168.2.223" + socket.toString());
                    OutputStream oo = socket.getOutputStream();

                    //向服务器发送信息
                    oo.write("android 客户端".getBytes("gbk"));
                    oo.flush();
                } catch (Exception e) {
                    System.err.println("192.168.2.223 失败");
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress("192.168.2.223", 5858), 5000);
                    System.out.println("192.168.2.223" + socket.toString());
                } catch (Exception e) {
                    System.err.println("192.168.2.223 失败");
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
