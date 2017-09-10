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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fj.data.Array.array;
import static fj.data.List.iterableList;

@EActivity(R.layout.activity_home_choose_game)
public class HomeChooseGameActivity extends BaseActivity {
    public HomeDocument doc() {return app.homeDocument;}

    @ViewById
    ListView lvGames;

    List<String> lstGameNames, lstGameTitles;
    public static Map<String, String> name2title = new HashMap<String, String>() {{
        put("AbstractPainting", "Abstract Painting");
        put("BalancedTapas", "Balanced Tapas");
        put("BattleShips", "Battle Ships");
        put("BootyIsland", "Booty Island");
        put("BoxItAgain", "Box It Again");
        put("BoxItAround", "Box It Around");
        put("BoxItUp", "Box It Up");
        put("BusySeas", "Busy Seas");
        put("BWTapa", "B&W Tapa");
        put("DigitalBattleShips", "Digital Battle Ships");
        put("FenceItUp", "Fence It Up");
        put("FenceSentinels", "Fence Sentinels");
        put("LightBattleShips", "Light Battle Ships");
        put("LightenUp", "Lighten Up");
        put("MineShips", "Mine Ships");
        put("MiniLits", "Mini-Lits");
        put("NumberPath", "Number Path");
        put("OverUnder", "Over Under");
        put("PaintTheNurikabe", "Paint The Nurikabe");
        put("ProductSentinels", "Product Sentinels");
        put("TapaIslands", "Tapa Islands");
        put("TapAlike", "Tap-Alike");
        put("TapARow", "Tap-A-Row");
        put("TapDifferently", "Tap Differently");
    }};

    @AfterViews
    protected void init() {
        try {
            lstGameNames = array(app.getApplicationContext().getAssets().list("xml"))
                    .map(f -> f.substring(0, f.length() - ".xml".length()))
                    .toJavaList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lstGameTitles = iterableList(lstGameNames).map(s -> {
            String t = name2title.get(s);
            return t == null ? s : t;
        }).toJavaList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, lstGameTitles);
        lvGames.setAdapter(adapter);
        String gameName = doc().gameProgress().gameName;
        lvGames.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvGames.setItemChecked(lstGameNames.indexOf(gameName), true);
    }

    @ItemClick
    protected void lvGamesItemClicked(int position) {
        doc().resumeGame(lstGameNames.get(position), lstGameTitles.get(position));
        setResult(RESULT_OK, null);
        finish();
    }

    @Click
    protected void btnCancel() {
        finish();
    }

}
