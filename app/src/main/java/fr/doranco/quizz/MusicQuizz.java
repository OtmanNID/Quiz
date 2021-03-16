package fr.doranco.quizz;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;

public class MusicQuizz extends Service {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MediaPlayer ring;

    public MusicQuizz() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ring = MediaPlayer.create(this, R.raw.sleep);
        ring.setLooping(true);
        ring.start();

        return START_STICKY;
    }

    @Override
    public  void onDestroy() {
        ring.stop();
    }
}