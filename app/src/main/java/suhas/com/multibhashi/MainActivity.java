package suhas.com.multibhashi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;

import suhas.com.multibhashi.controller.AppController;
import suhas.com.multibhashi.fragment.FragmentLearn;
import suhas.com.multibhashi.fragment.FragmentQuestion;
import suhas.com.multibhashi.model.LessonModel;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog = null;

    public static int chapter = 0;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private FloatingActionButton fab_next;

    public Context context;

    private ArrayList<LessonModel> lessonList;
    private static final String url = "http://www.akshaycrt2k.com/getLessonData.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        String languageToLoad  = "kn";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        this.context = this.getBaseContext();

        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Fetching The File....");
        progressDialog.show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#004B7D")));

        lessonList = new ArrayList<LessonModel>();

        fab_next = (FloatingActionButton) findViewById(R.id.fab_next);

        if (isNetworkAvailable()) {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new

                    Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray obj = response.getJSONArray("lesson_data");
                                for (int i = 0; i < obj.length(); i++) {

                                    JSONObject jsonObject = obj.getJSONObject(i);

                                    LessonModel learningData = new LessonModel();
                                    learningData.setType(jsonObject.getString("type"));
                                    learningData.setAudio_url(jsonObject.getString("audio_url"));
                                    learningData.setConceptName(jsonObject.getString("conceptName"));
                                    learningData.setPronunciation(jsonObject.getString("pronunciation"));
                                    learningData.setTargetScript(jsonObject.getString("targetScript"));
                                    lessonList.add(learningData);

                                    final AppController aController = (AppController) getApplicationContext();
                                    aController.setLearningData(lessonList);
                                }

                                switchFragmentToLearn();

                                progressDialog.dismiss();

                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    error.printStackTrace();


                }

            });

            AppController.getInstance().addToRequestQueue(jsonObjectRequest);


        } else {
            Toast.makeText(getApplicationContext(), "Enable internet", Toast.LENGTH_LONG).show();
        }

        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment_byID = fm.findFragmentById(R.id.fl_container);
                //check is fragmentQuestion
                //if fragmentQuestion then get next question from model
                //else replace FragmentLearn with FragmentQuestion

                if (fragment_byID instanceof FragmentQuestion) {
                    //increase chapter by one
                    if(chapter + 1== lessonList.size()){

                        showChapterCompletionDialog();

                    }else{
                        chapter = chapter + 1;
                        switchFragmentToLearn();
                    }

                } else {
                    //replace FragmentLearn with FragmentQuestion
                    chapter = chapter + 1;
                    switchFragmentToQuestion();
                }
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void switchFragmentToQuestion() {
        Fragment fragmentQuestion = null;
        fragmentQuestion = new FragmentQuestion();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.exit_to_left, 0);
        transaction.replace(R.id.fl_container, fragmentQuestion);
        transaction.commit();
    }

    private void switchFragmentToLearn() {

        Fragment fragmentLearn = new FragmentLearn();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.exit_to_left, 0);
        transaction.replace(R.id.fl_container, fragmentLearn);
        transaction.commit();

    }

    void showChapterCompletionDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.message);
        builder.setTitle(R.string.info);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        chapter = 0;

                        switchFragmentToLearn();
                    }
                });

        builder.show();
    }

    public void playAudio(final String mediaUrl, final File mediaFile, final Context context) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        URLConnection cn = new URL(mediaUrl).openConnection();
                        InputStream is = cn.getInputStream();

                        // create file to store audio


                        FileOutputStream fos = new FileOutputStream(new File(context.getCacheDir(), "mediafile"));
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

                        FileInputStream fis = new FileInputStream(new File(context.getCacheDir(), "mediafile"));
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

}