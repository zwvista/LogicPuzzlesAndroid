package com.zwstudio.logicgamesandroid.slitherlink.android;

import android.view.View;
import android.widget.Button;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.logicgames.android.BaseActivity;
import com.zwstudio.logicgamesandroid.slitherlink.data.SlitherLinkDocument;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_slitherlink_main)
public class SlitherLinkMainActivity extends BaseActivity {
    public SlitherLinkDocument doc() {return app.slitherlinkDocument;}

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
        SlitherLinkOptionsActivity_.intent(this).start();
    }

    void resumeGame() {
        doc().resumeGame();
        SlitherLinkGameActivity_.intent(this).start();
    }
}
