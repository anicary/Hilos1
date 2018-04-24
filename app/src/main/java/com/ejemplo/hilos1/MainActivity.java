package com.ejemplo.hilos1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;

    ProgressBar progressBar1;
    ProgressBar progressBar2;

    EditText edit1;
    EditText edit2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit1 =(EditText) findViewById(R.id.edit1);
        edit2 =(EditText) findViewById(R.id.edit2);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);


        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);

    }

    private void UnSegundo() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int i;
        switch (v.getId()) {
            case R.id.button4:
                AsyncTarea asyncTarea = new AsyncTarea();
                asyncTarea.elegirProgressBar(0);
                asyncTarea.agregarCantidad(Integer.parseInt(edit1.getText().toString()));
                asyncTarea.execute();
                break;
            case R.id.button5:
                AsyncTarea asyncTarea2 = new AsyncTarea();
                asyncTarea2.elegirProgressBar(1);
                asyncTarea2.agregarCantidad(Integer.parseInt(edit2.getText().toString()));
                asyncTarea2.execute();
                break;
            case R.id.button6:
                AsyncTarea asyncTareaa1 = new AsyncTarea();
                asyncTareaa1.elegirProgressBar(0);
                asyncTareaa1.agregarCantidad(Integer.parseInt(edit1.getText().toString()));
                asyncTareaa1.ambos=1;
               // asyncTareaa1.execute();
                asyncTareaa1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                AsyncTarea asyncTareaa2 = new AsyncTarea();
                asyncTareaa2.elegirProgressBar(1);
                asyncTareaa2.agregarCantidad(Integer.parseInt(edit2.getText().toString()));
                //permite un pool de hilos, ambos hilos se ejecutan al mismo tiempo
                asyncTareaa2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            default:
                break;
        }

    }

    public void hilos() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    UnSegundo();
                }
                // Para generar un error con la UI Thread
               Toast.makeText(getBaseContext(), "Segundos:5", Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "Segundos:5", Toast.LENGTH_LONG).show();
                    }

                });
            }
        }).start();
    }

    private class  AsyncTarea extends AsyncTask<Void, Integer,Boolean>{
        int limite=0;
        int eleccion=0;
        int ambos=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (eleccion==0) {
                progressBar1.setMax(100);
                progressBar1.setProgress(0);
            }else{
                progressBar2.setMax(100);
                progressBar2.setProgress(0);
            }


        }
        public void agregarCantidad(int limite){
            this.limite=limite;
        }
        public void elegirProgressBar(int eleccion){
            this.eleccion=eleccion;
        }
        @Override
        protected Boolean doInBackground(Void... params) {

            for (int i=1; i<limite; i++){
                UnSegundo();
                publishProgress(i);

                if (isCancelled()){
                    break;
                }
            }
            return true;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if (eleccion==0) {
                progressBar1.setProgress(values[0].intValue());
            }else{
                progressBar2.setProgress(values[0].intValue());
            }
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            //super.onPostExecute(aVoid);

            if (aVoid){
                Toast.makeText(getApplicationContext(),"Tarea finaliza AsyncTask",Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();

            Toast.makeText(getApplicationContext(),"Tarea NO finaliza AsyncTask",Toast.LENGTH_SHORT).show();

        }


    }
}
