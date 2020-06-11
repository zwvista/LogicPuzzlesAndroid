package com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.androidimport

import android.R
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain.SlitherLinkGame
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ItemSelect
import java.sql.SQLException

_
@EActivity(R.layout.activity_game_main)
class SlitherLinkMainActivity : GameMainActivity<SlitherLinkGame?, SlitherLinkDocument?, SlitherLinkGameMove?, SlitherLinkGameState?>() {
    @Bean
    protected var document: SlitherLinkDocument? = null
    override fun doc() = document

    @Click
    fun btnOptions() {
        SlitherLinkOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        SlitherLinkGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class SlitherLinkOptionsActivity : GameOptionsActivity<SlitherLinkGame?, SlitherLinkDocument?, SlitherLinkGameMove?, SlitherLinkGameState?>() {
    @Bean
    protected var document: SlitherLinkDocument? = null
    override fun doc() = document

    @AfterViews
    protected override fun init() {
        val lst: List<String> = GameOptionsActivity.lstMarkers
        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String?>(this,
                R.layout.simple_spinner_item, GameOptionsActivity.lstMarkers) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v: View = super.getView(position, convertView, parent)
                val s = lst[position]
                val tv: TextView = v.findViewById<View>(R.id.text1) as TextView
                tv.setText(s)
                return v
            }

            override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
                val v: View = super.getDropDownView(position, convertView, parent)
                val s = lst[position]
                val ctv: CheckedTextView = v.findViewById<View>(R.id.text1) as CheckedTextView
                ctv.setText(s)
                return v
            }
        }
        adapter.setDropDownViewResource(R.layout.simple_list_item_single_choice)
        spnMarker.setAdapter(adapter)
        spnMarker.setSelection(doc().getMarkerOption())
    }

    @ItemSelect
    protected override fun spnMarkerItemSelected(selected: Boolean, position: Int) {
        val rec: GameProgress = doc().gameProgress()
        doc().setMarkerOption(rec, position)
        try {
            app.daoGameProgress.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    protected fun onDefault() {
        val rec: GameProgress = doc().gameProgress()
        doc().setMarkerOption(rec, 0)
        try {
            app.daoGameProgress.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        spnMarker.setSelection(doc().getMarkerOption())
    }
}

@EActivity(R.layout.activity_game_help)
class SlitherLinkHelpActivity : GameHelpActivity<SlitherLinkGame?, SlitherLinkDocument?, SlitherLinkGameMove?, SlitherLinkGameState?>() {
    @Bean
    protected var document: SlitherLinkDocument? = null
    override fun doc() = document
}