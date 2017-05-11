package com.zwstudio.logicpuzzlesandroid.home.android;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.BaseActivity;
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;
import java.util.List;

@EActivity(R.layout.activity_home_choose_game)
public class HomeChooseGameActivity extends BaseActivity {
    public HomeDocument doc() {return app.homeDocument;}

    @ViewById
    ListView lvGames;

    List<String> lstGames = Arrays.asList("Abc", "BattleShips", "BootyIsland", "BoxItAgain", "BoxItAround",
            "BoxItUp", "Bridges", "BusySeas", "Clouds", "Domino", "FenceItUp", "Hitori", "LightenUp", "Lighthouses",
            "LineSweeper", "Lits", "Loopy", "Magnets", "Masyu", "MiniLits", "Mosaik", "Neighbours", "Nurikabe",
            "Pairakabe", "Parks", "PowerGrid", "ProductSentinels", "Rooms", "Sentinels", "Skyscrapers", "SlitherLink",
            "Sumscrapers", "Tents");

    @AfterViews
    protected void init() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, lstGames);
        lvGames.setAdapter(adapter);
        String gameName = doc().gameProgress().gameName;
        lvGames.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvGames.setItemChecked(lstGames.indexOf(gameName), true);
    }

    @ItemClick
    protected void lvGamesItemClicked(int position) {
        String gameName = lstGames.get(position);
        doc().resumeGame(gameName);
        setResult(RESULT_OK, null);
        finish();
    }

    @Click
    protected void btnCancel() {
        finish();
    }

}
