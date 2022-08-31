package com.bugisoft.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import java.security.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Long.parseLong;

public class ServiceSms extends Service {
    public ServiceSms() {
    }

    private Timer timer = new Timer();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ultimosSms();
                Log.d("salida", "9 SECONDS");
            }
        }, 0, 9000);


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        Toast.makeText(this, "Servicio cancelado", Toast.LENGTH_SHORT).show();
        Log.d("salida", "On destroy: Cancelado");
    }

    private void ultimosSms(){
        Uri sms = Uri.parse("content://sms/inbox");
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(sms, null, null, null, null);
        if(cur.getCount() > 0){
            int menIndex = cur.getColumnIndex(Telephony.Sms.BODY);
            int envIndex = cur.getColumnIndex(Telephony.Sms.DATE_SENT);
            int i = 0;

            while (cur.moveToNext() && i < 5){
                String men = cur.getString(menIndex);
                String env = cur.getString(envIndex);

                Timestamp fecha = new Timestamp(Long.parseLong(env));

                Log.d("salida", "Fecha: " + fecha + " Mensaje: " + men);
                i = i + 1;
            }
        }
    }
}