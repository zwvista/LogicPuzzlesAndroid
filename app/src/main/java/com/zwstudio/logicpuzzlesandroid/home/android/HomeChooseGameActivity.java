package com.zwstudio.logicpuzzlesandroid.home.android;

import android.view.View;
import android.view.ViewTreeObserver;
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

import fj.Ord;

import static fj.data.Array.array;
import static fj.data.List.iterableList;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@EActivity(R.layout.activity_home_choose_game)
public class HomeChooseGameActivity extends BaseActivity {
    public HomeDocument doc() {return app.homeDocument;}

    @ViewById
    ListView lvGames;

    static List<String> lstGameNames, lstGameTitles;
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
        put("CarpentersSquare", "Carpenter's Square");
        put("CarpentersWall", "Carpenter's Wall");
        put("DigitalBattleShips", "Digital Battle Ships");
        put("DisconnectFour", "Disconnect Four");
        put("FenceItUp", "Fence It Up");
        put("FenceSentinels", "Fence Sentinels");
        put("FourMeNot", "Four-Me-Not");
        put("HolidayIsland", "Holiday Island");
        put("LightBattleShips", "Light Battle Ships");
        put("LightenUp", "Lighten Up");
        put("MineShips", "Mine Ships");
        put("MiniLits", "Mini-Lits");
        put("NoughtsAndCrosses", "Noughts & Crosses");
        put("NumberPath", "Number Path");
        put("OverUnder", "Over Under");
        put("PaintTheNurikabe", "Paint The Nurikabe");
        put("ParkLakes", "Park Lakes");
        put("ProductSentinels", "Product Sentinels");
        put("RippleEffect", "Ripple Effect");
        put("RobotCrosswords", "Robot Crosswords");
        put("RobotFences", "Robot Fences");
        put("Square100", "Square 100");
        put("TapaIslands", "Tapa Islands");
        put("TapAlike", "Tap-Alike");
        put("TapARow", "Tap-A-Row");
        put("TapDifferently", "Tap Differently");
        put("TennerGrid", "Tenner Grid");
        put("TheOddBrick", "The Odd Brick");
        put("TierraDelFuego", "Tierra Del Fuego");
        put("WallSentinels", "Wall Sentinels");
    }};
    private static final int UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

    @AfterViews
    protected void init() {
        if (lstGameNames == null) {
            try {
                lstGameNames = array(app.getApplicationContext().getAssets().list("xml"))
                        .map(f -> f.substring(0, f.length() - ".xml".length()))
                        .toStream().sort(Ord.stringOrd.contramap(s -> s.toUpperCase()))
                        .toJavaList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            lstGameTitles = iterableList(lstGameNames).map(s -> defaultIfNull(name2title.get(s), s)).toJavaList();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, lstGameTitles);
        lvGames.setAdapter(adapter);
        String gameName = doc().gameProgress().gameName;
        lvGames.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        int selectedPosition = lstGameNames.indexOf(gameName);
        lvGames.setItemChecked(selectedPosition, true);

        // https://stackoverflow.com/questions/7733813/how-can-you-tell-when-a-layout-has-been-drawn/7735122#7735122
        lvGames.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                lvGames.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // https://stackoverflow.com/questions/5540223/center-a-listview-on-its-current-selection
                int h1 = lvGames.getHeight();
                // https://stackoverflow.com/questions/3361423/android-get-listview-item-height
                View childView = adapter.getView(selectedPosition, null, lvGames);
                childView.measure(UNBOUNDED, UNBOUNDED);
                int h2 = childView.getMeasuredHeight();
                lvGames.smoothScrollToPositionFromTop(selectedPosition, h1/2 - h2/2);
            }
        });
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
