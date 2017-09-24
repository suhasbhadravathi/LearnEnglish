package suhas.com.multibhashi.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import suhas.com.multibhashi.MainActivity;
import suhas.com.multibhashi.R;
import suhas.com.multibhashi.controller.AppController;
import suhas.com.multibhashi.model.LessonModel;

/**
 * Created by suhasvijay on 23/09/2017.
 */

public class FragmentLearn extends android.support.v4.app.Fragment {

    public static File mediaFile;

    private TextView tv_conceptName;
    private TextView tv_targetScript;
    private ImageView iv_background;
    private ImageView iv_play;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_learn, viewGroup, false);

        //get data from LessonModel using controller
        final AppController aController = (AppController) getActivity().getApplicationContext();
        final ArrayList<LessonModel> learningData = aController.getLearningData();

        tv_conceptName = (TextView) view.findViewById(R.id.tv_conceptName);
        tv_targetScript = (TextView) view.findViewById(R.id.tv_targetScript);
        iv_background = (ImageView) view.findViewById(R.id.iv_background);
        iv_play = (ImageView) view.findViewById(R.id.iv_play);

        //set image resource to iv_background
        iv_background.setImageResource(R.drawable.img_speak);
        iv_background.setScaleType(ImageView.ScaleType.MATRIX);

        //set image resource to iv_play
        iv_play.setImageResource(R.drawable.img_play);
        final MainActivity mainActivity = new MainActivity();

        tv_conceptName.setText(learningData.get(MainActivity.chapter).getConceptName());
        tv_targetScript.setText(learningData.get(MainActivity.chapter).getTargetScript());

        mainActivity.playAudio(learningData.get(MainActivity.chapter).getAudio_url(),mediaFile,getActivity() );

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //play audio using url
                mainActivity.playAudio(learningData.get(MainActivity.chapter).getAudio_url(),mediaFile,getActivity() );
            }
        });


        return view;
    }



}
