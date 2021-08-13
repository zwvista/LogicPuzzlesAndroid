package com.zwstudio.logicpuzzlesandroid.home.android

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.logicpuzzlesandroid.databinding.ActivityHomeChooseGameBinding
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument
import org.koin.android.ext.android.inject
import java.util.*

class HomeChooseGameActivity : AppCompatActivity() {
    val doc: HomeDocument by inject()
    private lateinit var binding: ActivityHomeChooseGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeChooseGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (lstGameNames.isEmpty()) {
            lstGameNames = assets.list("xml")!!
                .map { it.substring(0, it.length - ".xml".length) }
                .sortedBy { it.uppercase(Locale.ROOT) }
            lstGameTitles = lstGameNames.map { (name2title[it] ?: it) }
        }
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_single_choice, lstGameTitles)
        binding.lvGames.adapter = adapter
        val gameName = doc.gameProgress().gameName
        binding.lvGames.choiceMode = ListView.CHOICE_MODE_SINGLE
        val selectedPosition = lstGameNames.indexOf(gameName)
        binding.lvGames.setItemChecked(selectedPosition, true)

        // https://stackoverflow.com/questions/7733813/how-can-you-tell-when-a-layout-has-been-drawn/7735122#7735122
        binding.lvGames.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.lvGames.viewTreeObserver.removeOnGlobalLayoutListener(this)
                // https://stackoverflow.com/questions/5540223/center-a-listview-on-its-current-selection
                val h1 = binding.lvGames.height
                // https://stackoverflow.com/questions/3361423/android-get-listview-item-height
                val childView = adapter.getView(selectedPosition, null, binding.lvGames)
                childView.measure(UNBOUNDED, UNBOUNDED)
                val h2 = childView.measuredHeight
                binding.lvGames.smoothScrollToPositionFromTop(selectedPosition, h1 / 2 - h2 / 2)
            }
        })
        binding.lvGames.setOnItemClickListener { _, _, position, _ ->
            doc.resumeGame(lstGameNames[position], lstGameTitles[position])
            setResult(Activity.RESULT_OK, null)
            finish()
        }
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    companion object {
        var lstGameNames = listOf<String>()
        var lstGameTitles = listOf<String>()
        var name2title = mapOf(
            "AbstractPainting" to "Abstract Painting",
            "BalancedTapas" to "Balanced Tapas",
            "BattleShips" to "Battle Ships",
            "BootyIsland" to "Booty Island",
            "BoxItAgain" to "Box It Again",
            "BoxItAround" to "Box It Around",
            "BoxItUp" to "Box It Up",
            "BusySeas" to "Busy Seas",
            "BWTapa" to "B&W Tapa",
            "CarpentersSquare" to "Carpenter's Square",
            "CarpentersWall" to "Carpenter's Wall",
            "CastleBailey" to "Castle Bailey",
            "DigitalBattleShips" to "Digital Battle Ships",
            "DisconnectFour" to "Disconnect Four",
            "FenceItUp" to "Fence It Up",
            "FenceSentinels" to "Fence Sentinels",
            "FourMeNot" to "Four-Me-Not",
            "HolidayIsland" to "Holiday Island",
            "LightBattleShips" to "Light Battle Ships",
            "LightenUp" to "Lighten Up",
            "MineShips" to "Mine Ships",
            "MiniLits" to "Mini-Lits",
            "NorthPoleFishing" to "North Pole Fishing",
            "NoughtsAndCrosses" to "Noughts & Crosses",
            "NumberPath" to "Number Path",
            "OverUnder" to "Over Under",
            "PaintTheNurikabe" to "Paint The Nurikabe",
            "ParkLakes" to "Park Lakes",
            "ProductSentinels" to "Product Sentinels",
            "RippleEffect" to "Ripple Effect",
            "RobotCrosswords" to "Robot Crosswords",
            "RobotFences" to "Robot Fences",
            "Square100" to "Square 100",
            "TapaIslands" to "Tapa Islands",
            "TapAlike" to "Tap-Alike",
            "TapARow" to "Tap-A-Row",
            "TapDifferently" to "Tap Differently",
            "TennerGrid" to "Tenner Grid",
            "TheOddBrick" to "The Odd Brick",
            "TierraDelFuego" to "Tierra Del Fuego",
            "WallSentinels" to "Wall Sentinels"
        )
        private val UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    }
}