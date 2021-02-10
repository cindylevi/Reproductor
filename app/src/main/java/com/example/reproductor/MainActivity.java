package com.example.reproductor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnPlay, btnPause, btnStop, btnAntes, btnDesp, BsonidoKid, BsonidoRayo, BsonidoErupto, BsonidoGun;;
    MediaPlayer mp;
    int pos = 0;
    int sonidoKid, sonidoRayo, sonidoErupto, sonidoGun;
    SeekBar sb;
    SoundPool soundPool;
    int totalTime;

    MediaPlayer vectormp[] =  new MediaPlayer[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAtt =new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            soundPool= new SoundPool.Builder().setMaxStreams(4).setAudioAttributes(audioAtt).build();
        }else{
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        }
        CargarSounpoolSonidos();
        ObtenerReferencias();
        CargarDatos();
        SetearListeners();
        totalTime = vectormp[pos].getDuration();
        sb.setMax(totalTime);
    }

    private void CargarSounpoolSonidos() {
        sonidoErupto=soundPool.load(this, R.raw.burp,1);
        sonidoGun=soundPool.load(this, R.raw.gun,1);
        sonidoKid=soundPool.load(this, R.raw.kid,1);
        sonidoRayo=soundPool.load(this, R.raw.thunder,1);

    }

    private void SetearListeners() {
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vectormp[pos].isPlaying()){
                    vectormp[pos].pause();
                }
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vectormp[pos].start();

            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vectormp[pos] != null){
                    vectormp[pos].stop();
                    pos=0;
                    sb.setProgress(0);
                    CargarDatos();

                }
            }
        });
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    vectormp[pos].seekTo(progress);
                    sb.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnDesp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( pos < 3 ){
                    if (vectormp[pos].isPlaying()){
                        vectormp[pos].stop();
                        CargarDatos();
                        pos++;
                        vectormp[pos].start();
                        totalTime = vectormp[pos].getDuration();
                        sb.setMax(totalTime);
                        sb.setProgress(0);
                    }else{
                        pos++;
                        vectormp[pos].start();
                        totalTime = vectormp[pos].getDuration();
                        sb.setMax(totalTime);
                        sb.setProgress(0);
                    }

                }else {
                    vectormp[pos].stop();
                    CargarDatos();
                    pos = 0;
                    vectormp[pos].start();
                    totalTime = vectormp[pos].getDuration();
                    sb.setMax(totalTime);
                    sb.setProgress(0);
                }
            }
        });
        btnAntes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( pos > 0){
                    if (vectormp[pos].isPlaying()){
                        vectormp[pos].stop();
                        CargarDatos();
                        pos--;
                        vectormp[pos].start();
                        totalTime = vectormp[pos].getDuration();
                        sb.setMax(totalTime);
                        sb.setProgress(0);
                    }else{
                        pos--;
                        vectormp[pos].start();
                        totalTime = vectormp[pos].getDuration();
                        sb.setMax(totalTime);
                        sb.setProgress(0);
                    }

                }else {
                    vectormp[pos].stop();
                    CargarDatos();
                    pos = 3;
                    vectormp[pos].start();
                    totalTime = vectormp[pos].getDuration();
                    sb.setMax(totalTime);
                    sb.setProgress(0);
                }
            }
        });
        BsonidoRayo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog alertRayo = SonidosAlertDialog("rayo", sonidoRayo);
                alertRayo.show();

            }
        });
        BsonidoKid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog alertKid = SonidosAlertDialog("chico", sonidoKid);
                alertKid.show();

            }
        });
        BsonidoGun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog alertGun = SonidosAlertDialog("arma", sonidoGun);
                alertGun.show();

            }
        });
        BsonidoErupto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog alertPlay = SonidosAlertDialog("erupto", sonidoErupto);
                alertPlay.show();
            }
        });


    }
    public Dialog SonidosAlertDialog(String nombreSonido, final int sonidoElegido){
        AlertDialog.Builder blder = new AlertDialog.Builder(MainActivity.this);
        blder.setTitle("Poner " +nombreSonido)
                .setMessage("Queres poner el " +nombreSonido +"?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Pusiste play", Toast.LENGTH_SHORT);
                soundPool.play(sonidoElegido,1,1,0, 0,1);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "No quiste reproducir", Toast.LENGTH_SHORT);
            }
        });
        return blder.create();
    }

    private void CargarDatos() {
        vectormp[0]=MediaPlayer.create(this, R.raw.drexler);
        vectormp[1]=MediaPlayer.create(this, R.raw.bailemos);
        vectormp[2]=MediaPlayer.create(this, R.raw.shakeandpop);
        vectormp[3]=MediaPlayer.create(this, R.raw.galway);
    }

    private void ObtenerReferencias() {
        btnPlay=findViewById(R.id.btnPlay);
        btnPause=findViewById(R.id.btnPausa);
        btnStop=findViewById(R.id.btnStop);
        btnAntes=findViewById(R.id.btnAntes);
        btnDesp=findViewById(R.id.btnDesp);
        BsonidoErupto=findViewById(R.id.btnBurp);
        BsonidoGun=findViewById(R.id.btnGun);
        BsonidoKid=findViewById(R.id.btnKid);
        BsonidoRayo=findViewById(R.id.btnThunder);
        sb=findViewById(R.id.seekBar);
    }

}