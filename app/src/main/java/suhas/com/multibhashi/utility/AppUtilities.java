package suhas.com.multibhashi.utility;

/**
 * Created by suhasvijay on 24/09/2017.
 */

public class AppUtilities {

/*
    public  void playAudio(File medFile, final String mediaUrl, Activity activity) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        URLConnection cn = new URL(mediaUrl).openConnection();
                        InputStream is = cn.getInputStream();

                        // create file to store audio
                        mediaFile = new File(getActivity().getCacheDir(), "mediafile");
                        FileOutputStream fos = new FileOutputStream(mediaFile);
                        byte buf[] = new byte[16 * 1024];
                        Log.i("FileOutputStream", "Download");

                        // write to file until complete
                        do {
                            int numread = is.read(buf);
                            if (numread <= 0)
                                break;
                            fos.write(buf, 0, numread);
                        } while (true);
                        fos.flush();
                        fos.close();
                        Log.i("FileOutputStream", "Saved");
                        MediaPlayer mp = new MediaPlayer();

                        // create listener to tidy up after playback complete
                        MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                // free up media player
                                mp.release();
                                Log.i("MediaPlayer.OnCompletionListener", "MediaPlayer Released");
                            }
                        };
                        mp.setOnCompletionListener(listener);

                        FileInputStream fis = new FileInputStream(mediaFile);
                        // set mediaplayer data source to file descriptor of input stream
                        mp.setDataSource(fis.getFD());
                        mp.prepare();
                        Log.i("MediaPlayer", "Start Player");
                        mp.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();


    }
*/


}
