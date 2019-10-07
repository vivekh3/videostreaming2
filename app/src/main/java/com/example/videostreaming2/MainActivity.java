package com.example.videostreaming2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    public static final String TAG="VS2";
    public DataInputStream dataInputStream;
    ImageView imageView;
    int len;
    private Socket s;
    private PrintWriter pw;
    private boolean mconnectexception=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frame2video f2v=new frame2video();
        f2v.execute();

    }


    public class frame2video extends AsyncTask<Void,Void,Void>{

        protected void onPreExecute(){
            imageView=(ImageView)findViewById(R.id.imview);


        }
        protected Void doInBackground(Void...params){

            try{
                try{
                    s=new Socket("192.168.0.132",5000);
                    //pw=new PrintWriter(s.getOutputStream());
                    Log.d(TAG,"Connection Successful..!");

                } catch(IOException e){
                    e.printStackTrace();
                    mconnectexception=true;
                }
                if(mconnectexception)
                    //Toast.makeText(getApplicationContext(),"Connection not available", Toast.LENGTH_LONG).show();
                    Log.d(TAG,"Connection not Available");

                while(true){
                    DataInputStream dataInputStream=new DataInputStream(s.getInputStream());
                    len=Integer.parseInt(""+dataInputStream.readInt());
                    System.out.println(len);
                    byte[] buffer=new byte[len];
                    dataInputStream.readFully(buffer,0,buffer.length);
                    try {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, len);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        outputStream.flush();
                        bitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream);
                        outputStream.close();
                        imageView.setImageBitmap(bitmap);
                        imageView.invalidate();
                    }catch(IOException e){
                        e.printStackTrace();

                    }


                }

            }catch (Exception e){
                e.printStackTrace();
            }


            return null;


        }


    }
}