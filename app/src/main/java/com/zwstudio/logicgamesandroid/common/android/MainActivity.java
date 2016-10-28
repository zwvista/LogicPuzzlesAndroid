package com.zwstudio.logicgamesandroid.common.android;

import android.view.View;
import android.widget.Button;

import com.zwstudio.logicgamesandroid.common.data.GameDocument;
import com.zwstudio.logicgamesandroid.common.domain.Game;
import com.zwstudio.logicgamesandroid.common.domain.GameState;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity
public abstract class MainActivity<G extends Game<G, GM, GS>, GD extends GameDocument<G, GM, GS>, GM, GS extends GameState>
        extends BaseActivity {
    public abstract GD doc();

    protected void init(int[] levels) {
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
    protected void btnResumeGame() {
        resumeGame();
    }

    protected abstract void resumeGame();
}