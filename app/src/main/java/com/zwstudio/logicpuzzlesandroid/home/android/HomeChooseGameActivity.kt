package com.zwstudio.logicpuzzlesandroid.home.android

import android.app.Activity
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ArrayAdapter
import android.widget.ListView
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.BaseActivity
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument
import fj.Ord
import fj.data.Array
import org.androidannotations.annotations.*
import org.apache.commons.lang3.ObjectUtils
import java.io.IOException
import java.util.*

@EActivity(R.layout.activity_home_choose_game)
open class HomeChooseGameActivity : BaseActivity() {
    fun doc(): HomeDocument? {
        return app!!.homeDocument
    }

    @ViewById
    var lvGames: ListView? = null

    @AfterViews
    protected fun init() {
        if (lstGameNames == null) {
            try {
                lstGameNames = Array.array(*app!!.applicationContext.assets.list("xml"))
                    .map { f: String? -> f!!.substring(0, f.length - ".xml".length) }
                    .toStream().sort(Ord.stringOrd.contramap { s: String? -> s!!.toUpperCase() })
                    .toJavaList()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            lstGameTitles = fj.data.List.iterableList(lstGameNames).map { s: String? -> ObjectUtils.defaultIfNull(name2title[s], s) }.toJavaList()
        }
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_single_choice, lstGameTitles)
        lvGames!!.adapter = adapter
        val gameName = doc()!!.gameProgress()!!.gameName
        lvGames!!.choiceMode = ListView.CHOICE_MODE_SINGLE
        val selectedPosition = lstGameNames!!.indexOf(gameName)
        lvGames!!.setItemChecked(selectedPosition, true)

        // https://stackoverflow.com/questions/7733813/how-can-you-tell-when-a-layout-has-been-drawn/7735122#7735122
        lvGames!!.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                lvGames!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                // https://stackoverflow.com/questions/5540223/center-a-listview-on-its-current-selection
                val h1 = lvGames!!.height
                // https://stackoverflow.com/questions/3361423/android-get-listview-item-height
                val childView = adapter.getView(selectedPosition, null, lvGames!!)
                childView.measure(UNBOUNDED, UNBOUNDED)
                val h2 = childView.measuredHeight
                lvGames!!.smoothScrollToPositionFromTop(selectedPosition, h1 / 2 - h2 / 2)
            }
        })
    }

    @ItemClick
    protected fun lvGamesItemClicked(position: Int) {
        doc()!!.resumeGame(lstGameNames!![position], lstGameTitles!![position])
        setResult(Activity.RESULT_OK, null)
        finish()
    }

    @Click
    protected fun btnCancel() {
        finish()
    }

    companion object {
        var lstGameNames: List<String?>? = null
        var lstGameTitles: List<String?>? = null
        var name2title: Map<String?, String> = object : HashMap<String?, String?>() {
            init {
                put("AbstractPainting", "Abstract Painting")
                put("BalancedTapas", "Balanced Tapas")
                put("BattleShips", "Battle Ships")
                put("BootyIsland", "Booty Island")
                put("BoxItAgain", "Box It Again")
                put("BoxItAround", "Box It Around")
                put("BoxItUp", "Box It Up")
                put("BusySeas", "Busy Seas")
                put("BWTapa", "B&W Tapa")
                put("CarpentersSquare", "Carpenter's Square")
                put("CarpentersWall", "Carpenter's Wall")
                put("CastleBailey", "Castle Bailey")
                put("DigitalBattleShips", "Digital Battle Ships")
                put("DisconnectFour", "Disconnect Four")
                put("FenceItUp", "Fence It Up")
                put("FenceSentinels", "Fence Sentinels")
                put("FourMeNot", "Four-Me-Not")
                put("HolidayIsland", "Holiday Island")
                put("LightBattleShips", "Light Battle Ships")
                put("LightenUp", "Lighten Up")
                put("MineShips", "Mine Ships")
                put("MiniLits", "Mini-Lits")
                put("NorthPoleFishing", "North Pole Fishing")
                put("NoughtsAndCrosses", "Noughts & Crosses")
                put("NumberPath", "Number Path")
                put("OverUnder", "Over Under")
                put("PaintTheNurikabe", "Paint The Nurikabe")
                put("ParkLakes", "Park Lakes")
                put("ProductSentinels", "Product Sentinels")
                put("RippleEffect", "Ripple Effect")
                put("RobotCrosswords", "Robot Crosswords")
                put("RobotFences", "Robot Fences")
                put("Square100", "Square 100")
                put("TapaIslands", "Tapa Islands")
                put("TapAlike", "Tap-Alike")
                put("TapARow", "Tap-A-Row")
                put("TapDifferently", "Tap Differently")
                put("TennerGrid", "Tenner Grid")
                put("TheOddBrick", "The Odd Brick")
                put("TierraDelFuego", "Tierra Del Fuego")
                put("WallSentinels", "Wall Sentinels")
                put("WallSentinels2", "Wall Sentinels 2")
            }
        }
        private val UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    }
}