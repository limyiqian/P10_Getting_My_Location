package sg.edu.rp.c346.id19020125.p10_getting_my_location;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.io.File;

public class MusicService extends Service {

    private MediaPlayer player = new MediaPlayer();
    private MusicBinder mBinder = new MusicBinder();

    class MusicBinder extends Binder {
        public void getMusic() {
            Log.d("MusicService","play");
            try {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder", "music.mp3");

                player.setDataSource(file.getPath());
                player.prepare();

            } catch (Exception e) {
                e.printStackTrace();
            }

            player.setLooping(true);

            player.start();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MusicService","play");
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder", "music.mp3");

            player.setDataSource(file.getPath());
            player.prepare();

        } catch (Exception e) {
            e.printStackTrace();
        }

        player.setLooping(true);

        player.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MusicService","stop");
        player.stop();
    }

}