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
import java.util.Locale

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
            lstGameTitles = lstGameNames.map { name2title[it] ?: splitAndJoinWords(it) }
        }
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_single_choice, lstGameTitles)
        binding.lvGames.adapter = adapter
        val gameName = doc.gameProgress().gameName
        binding.lvGames.choiceMode = ListView.CHOICE_MODE_SINGLE
        val focusPosition = lstGameNames.indexOf(gameName)
        binding.lvGames.setItemChecked(focusPosition, true)

        // https://stackoverflow.com/questions/7733813/how-can-you-tell-when-a-layout-has-been-drawn/7735122#7735122
        binding.lvGames.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.lvGames.viewTreeObserver.removeOnGlobalLayoutListener(this)
                // https://stackoverflow.com/questions/5540223/center-a-listview-on-its-current-selection
                val h1 = binding.lvGames.height
                // https://stackoverflow.com/questions/3361423/android-get-listview-item-height
                val childView = adapter.getView(focusPosition, null, binding.lvGames)
                childView.measure(UNBOUNDED, UNBOUNDED)
                val h2 = childView.measuredHeight
                binding.lvGames.smoothScrollToPositionFromTop(focusPosition, h1 / 2 - h2 / 2)
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
            "ABCPath" to "ABC Path",
            "BWTapa" to "B&W Tapa",
            "CarpentersSquare" to "Carpenter's Square",
            "CarpentersWall" to "Carpenter's Wall",
            "FourMeNot" to "Four-Me-Not",
            "MaketheDifference" to "Make the Difference",
            "MiniLits" to "Mini-Lits",
            "NoughtsAndCrosses" to "Noughts & Crosses",
            "Square100" to "Square 100",
            "TapAlike" to "Tap-Alike",
            "TapARow" to "Tap-A-Row",
        )
        private val UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        fun splitAndJoinWords(input: String): String {
            val pattern = Regex("([a-z])([A-Z])")
            return input.replace(pattern, "\$1 \$2")
        }
        fun splitAndJoinWords2(input: String): String {
            val pattern = Regex("(?<=[a-z])(?=[A-Z])")
            return input.replace(pattern, " ")
        }
    }
}