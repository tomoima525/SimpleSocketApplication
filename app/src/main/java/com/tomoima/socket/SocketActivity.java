package com.tomoima.socket;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class SocketActivity extends Activity {

    private TextView mTextView;
    private String mHost = "google.com";
    private int mPort = 80;
    private String mPath = "";
    private String mUserAgent = "Mozilla/5.0 (Linux; U; Android 4.0.4; ja-jp; SC-06D Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        mTextView = (TextView)findViewById(R.id.socket);
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                connect();
            }
        });
    }


    public void connect(){

        //第一引数：execute()で入れるパラメータ
        //第二引数：onProgressUpdate()にいれるパラメータ
        //第三引数：onPostExecute()に入れるパラメータ
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... voids) {
                Socket connection = null;
                BufferedReader reader = null;
                BufferedWriter writer = null;
                String message = "result:";
                String url = "http://" + mHost +":" + mPort + mPath;
                try {
                    //ソケット
                    connection = new Socket(mHost, mPort);
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

                    //HTTPリクエスト
                    writer.write("GET " + url + " HTTP/1.1\r\n");
                    writer.write("Host: " + mHost + "\r\n");
                    writer.write("User-Agent: " + mUserAgent);
                    writer.write("Connection: close\r\n");
                    writer.write("\r\n");
                    writer.flush();

                    //HTTPレスポンス
                    String result;
                    while((result = reader.readLine()) != null) {
                        message += result;
                        message += "\n";
                    }

                } catch (IOException e) {
                    message = "IOException error: " + e.getMessage();
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                    message = "Exception: " + e.getMessage();

                } finally {
                    try{
                        reader.close();
                        connection.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(message);
                return message;
            }

            //doInBackGroundの結果を受け取る
            @Override
            protected void onPostExecute(String result){
                mTextView.setText(result);
            }
        }.execute();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.socket, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
