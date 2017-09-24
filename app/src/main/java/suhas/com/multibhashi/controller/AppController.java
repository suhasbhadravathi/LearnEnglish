package suhas.com.multibhashi.controller;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import suhas.com.multibhashi.model.LessonModel;

/**
 * Created by suhasvijay on 23/09/2017.
 */

public class AppController extends Application {


    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public ArrayList<LessonModel> getLearningData() {
        return learningData;
    }

    public void setLearningData(ArrayList<LessonModel> learningData) {
        this.learningData = learningData;
    }

    private ArrayList<LessonModel> learningData = new ArrayList<LessonModel>();


}
