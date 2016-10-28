package com.zwstudio.logicgamesandroid.hitori.android;

import android.view.View;
import android.widget.Button;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.hitori.data.HitoriDocument;
import com.zwstudio.logicgamesandroid.logicgames.android.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_hitori_main)
public class HitoriMainActivity extends BaseActivity {
    public HitoriDocument doc() {return app().hitoriDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String levelID = ((Button)v).getText().toString();
                doc().selectedLevelID = levelID;
                resumeGame();
            }
        };
        // http://stackoverflow.com/questions/25905086/multiple-buttons-onclicklistener-android
        for(int n : levels) {
            int resID = getResources().getIdentifier("btnLevel" + n, "id", "com.zwstudio.logicgamesandroid");
            Button button = (Button)findViewById(resID);
            button.setOnClickListener(onClickListener);
        }

        boolean toResume = getIntent().getBooleanExtra("toResume", false);
        if (toResume) resumeGame();
    }

    @Click
    void btnResumeGame() {
        resumeGame();
    }

    @Click
    void btnOptions() {
        HitoriOptionsActivity_.intent(this).start();
    }

    void resumeGame() {
        doc().resumeGame();
        HitoriGameActivity_.intent(this).start();
    }
}
