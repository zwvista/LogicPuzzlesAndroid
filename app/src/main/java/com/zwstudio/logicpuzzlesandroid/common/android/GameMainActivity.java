package com.zwstudio.logicpuzzlesandroid.common.android;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument;
import com.zwstudio.logicpuzzlesandroid.common.domain.Game;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static fj.data.List.iterableList;

@EActivity
public abstract class GameMainActivity<G extends Game<G, GM, GS>, GD extends GameDocument<G, GM>, GM, GS extends GameState>
        extends BaseActivity {
    public abstract GD doc();

    @ViewById
    protected TextView tvGame;
    @ViewById
    protected Button btnResumeLevel;

    int currentPage = 0;
    int countPerPage = 12;
    int numPages = 1;

    @AfterViews
    protected void init() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doc().selectedLevelID = ((Button)v).getText().toString();
                resumeGame();
            }
        };
        // http://stackoverflow.com/questions/25905086/multiple-buttons-onclicklistener-android
        for(int i = 0; i < countPerPage; i++) {
            int resID = getResources().getIdentifier("btnLevel" + (i + 1), "id", "com.zwstudio.logicpuzzlesandroid");
            Button button = (Button)findViewById(resID);
            button.setOnClickListener(onClickListener);
        }

        numPages = (doc().levels.size() + countPerPage - 1) / countPerPage;
        int index = iterableList(doc().levels).toStream().indexOf(o -> o.id.equals(doc().selectedLevelID)).orSome(0);
        currentPage = index / countPerPage;
        showCurrentPage();

        tvGame.setText(doc().gameID());

        boolean toResume = getIntent().getBooleanExtra("toResume", false);
        if (toResume) resumeGame();
    }

    protected void onResume() {
        super.onResume();
        btnResumeLevel.setText("Resume Level " + doc().selectedLevelID);
    }

    private void showCurrentPage() {
        for(int i = 0; i < countPerPage; i++) {
            int resID = getResources().getIdentifier("btnLevel" + (i + 1), "id", "com.zwstudio.logicpuzzlesandroid");
            Button button = (Button)findViewById(resID);
            int index = currentPage * countPerPage + i;
            boolean b = index < doc().levels.size();
            button.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            if (b) button.setText(doc().levels.get(index).id);
        }
    }

    @Click
    protected void btnPrevPage() {
        currentPage = (currentPage - 1 + numPages) % numPages;
        showCurrentPage();
    }

    @Click
    protected void btnNextPage() {
        currentPage = (currentPage + 1) % numPages;
        showCurrentPage();
    }

    @Click
    protected void btnResumeLevel() {
        resumeGame();
    }

    protected abstract void resumeGame();

    @Click
    protected void btnResetAllLevels() {
        yesNoDialog("Do you really want to reset the options?", () -> {
            doc().resetAllLevels();
        });
    }
}
