package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.android

import android.R
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.TextView
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.android.NeighboursGameActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.android.NeighboursOptionsActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.data.NeighboursDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGame
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain.NeighboursGameState
import org.androidannotations.annotations.*
import java.sql.SQLException

@EActivity(R.layout.activity_game_main)
class NeighboursMainActivity : GameMainActivity<NeighboursGame, NeighboursDocument, NeighboursGameMove, NeighboursGameState>() {
    @Bean
    protected lateinit var document: NeighboursDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        NeighboursOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        NeighboursGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class NeighboursOptionsActivity : GameOptionsActivity<NeighboursGame, NeighboursDocument, NeighboursGameMove, NeighboursGameState>() {
    @Bean
    protected lateinit var document: NeighboursDocument
    override fun doc() = document

    @AfterViews
    protected override fun init() {
        val lst: List<String> = GameOptionsActivity.lstMarkers
        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, GameOptionsActivity.lstMarkers) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v: View = super.getView(position, convertView, parent)
                val s = lst[position]
                val tv: TextView = v.findViewById<TextView>(R.id.text1)
                tv.setText(s)
                return v
            }

            override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
                val v: View = super.getDropDownView(position, convertView, parent)
                val s = lst[position]
                val ctv: CheckedTextView = v.findViewById<CheckedTextView>(R.id.text1)
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
class NeighboursHelpActivity : GameHelpActivity<NeighboursGame, NeighboursDocument, NeighboursGameMove, NeighboursGameState>() {
    @Bean
    protected lateinit var document: NeighboursDocument
    override fun doc() = document
}