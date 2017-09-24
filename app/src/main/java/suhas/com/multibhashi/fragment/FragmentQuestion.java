package suhas.com.multibhashi.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import suhas.com.multibhashi.MainActivity;
import suhas.com.multibhashi.R;
import suhas.com.multibhashi.controller.AppController;
import suhas.com.multibhashi.model.LessonModel;

/**
 * Created by suhasvijay on 23/09/2017.
 */

public class FragmentQuestion extends android.support.v4.app.Fragment {

    private File mediaFile;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView tv_question;
    private TextView tv_targetScript;
    private String concept;
    private ImageView iv_speak_background;
    private ImageView iv_speak;
    private ImageView img_question_play;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_question, viewGroup, false);

        //get data from LessonModel using controller
        final AppController aController = (AppController) getActivity().getApplicationContext();
        ArrayList<LessonModel> newChapterController = aController.getLearningData();

        tv_question = (TextView) view.findViewById(R.id.tv_question);
        iv_speak_background = (ImageView) view.findViewById(R.id.iv_speak_background);
        iv_speak = (ImageView) view.findViewById(R.id.ib_speak);
        img_question_play = (ImageView)view.findViewById(R.id.img_question_play);
        tv_targetScript = (TextView)view.findViewById(R.id.tv_targetScript);

        //set image resource to iv_speak_background
        iv_speak_background.setImageResource(R.drawable.img_speak);
        iv_speak_background.setScaleType(ImageView.ScaleType.MATRIX);

        //set image resource to img_mic
        iv_speak.setImageResource(R.drawable.img_mic);

        concept = newChapterController.get(MainActivity.chapter).getConceptName();

        final String audioUrl = newChapterController.get(MainActivity.chapter).getAudio_url();

        img_question_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity mainActivity = new MainActivity();
                mainActivity.playAudio(audioUrl, mediaFile, getActivity());
            }
        });

        iv_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        tv_question.setText(concept);
        tv_targetScript.setText(newChapterController.get(MainActivity.chapter).getTargetScript());


        return view;
    }

    private void promptSpeechInput() {


        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getResources().getString(R.string.speak));
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            String appPackageName = "com.google.android.googlequicksearchbox";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == resultCode && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        tv_question.setText(result.get(0));

                        if (result.get(0).equalsIgnoreCase(concept)) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.correct) + " !!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.wrong) + " !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }

            }
        }
    }


}
