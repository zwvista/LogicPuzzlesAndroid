package com.zwstudio.logicgamesandroid.lightup.android;

import android.view.View;
import android.widget.Button;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.lightup.data.LightUpDocument;
import com.zwstudio.logicgamesandroid.logicgames.android.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_lightup_main)
public class LightUpMainActivity extends BaseActivity {
    public LightUpDocument doc() {return app.lightUpDocument;}

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
        LightUpOptionsActivity_.intent(this).start();
    }

    void resumeGame() {
        doc().resumeGame();
        LightUpGameActivity_.intent(this).start();
    }
}
