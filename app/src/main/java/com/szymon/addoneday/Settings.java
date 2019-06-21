package com.szymon.addoneday;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Settings extends Activity {
    int i = 0;
    boolean mCzyodejmij = false;
    Intent replyIntent = new Intent();

    Switch przelacznik;
    EditText pTekstowe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);

        przelacznik = findViewById(R.id.switch1);
        pTekstowe = findViewById(R.id.editText);

        Update(); //updatuje ustawienia

        przelacznik.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    przelacznik.setText("Skok w przeszłość!");
                }else przelacznik.setText("Skok w przyszłość!");
            }
        });

        Button mZapisz = findViewById(R.id.button5);
        mZapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pobranie wartości przełącznika
                mCzyodejmij = przelacznik.isChecked();
                //pobranie wartości z pola tekstowego
                i = Integer.parseInt(pTekstowe.getText().toString());

                replyIntent.putExtra("czyodejmij",mCzyodejmij);
                replyIntent.putExtra("ilosc",i);
                setResult(RESULT_OK,replyIntent);
                Toast.makeText(getApplicationContext(),"Zapisano zmiany!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void Update(){
        Intent intent = getIntent();
        mCzyodejmij = intent.getBooleanExtra("czyodejmij",mCzyodejmij);
        przelacznik.setChecked(mCzyodejmij);

        pTekstowe = findViewById(R.id.editText);
        pTekstowe.setText(Integer.toString(Math.abs(intent.getIntExtra("ilosc",i))));
    }


    @Override
    public void onBackPressed(){
        setResult(RESULT_CANCELED);
        super.onBackPressed();

    }
}
