package com.zwstudio.logicpuzzlesandroid.common.android;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.domain.Game;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameState;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity
public abstract class GameMainActivity<G extends Game<G, GM, GS>, GD extends GameDocument<G, GM>, GM, GS extends GameState>
        extends BaseActivity {
    public abstract GD doc();

    @ViewById
    protected TextView tvGame;

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
            int resID = getResources().getIdentifier("btnLevel" + n, "id", "com.zwstudio.logicpuzzlesandroid");
            Button button = (Button)findViewById(resID);
            button.setOnClickListener(onClickListener);
        }

        tvGame.setText(doc().gameProgress().gameID);

        boolean toResume = getIntent().getBooleanExtra("toResume", false);
        if (toResume) resumeGame();
    }

    @Click
    protected void btnResumeGame() {
        resumeGame();
    }

    protected abstract void resumeGame();
}
