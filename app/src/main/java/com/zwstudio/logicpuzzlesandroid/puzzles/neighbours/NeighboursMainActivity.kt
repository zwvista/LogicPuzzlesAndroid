package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.TextView
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.home.android.realm
import org.koin.android.ext.android.inject

class NeighboursMainActivity : GameMainActivity<NeighboursGame, NeighboursDocument, NeighboursGameMove, NeighboursGameState>() {
    private val document: NeighboursDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, NeighboursOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, NeighboursGameActivity::class.java))
    }
}

class NeighboursOptionsActivity : GameOptionsActivity<NeighboursGame, NeighboursDocument, NeighboursGameMove, NeighboursGameState>() {
    private val document: NeighboursDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lst: List<String> = GameOptionsActivity.lstMarkers
        val adapter = object : ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, GameOptionsActivity.lstMarkers) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                val s = lst[position]
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = s
                return v
            }

            override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
                val v = super.getDropDownView(position, convertView, parent)
                val s = lst[position]
                val ctv = v.findViewById<CheckedTextView>(android.R.id.text1)
                ctv.text = s
                return v
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        binding.spnMarker.adapter = adapter
        binding.spnMarker.setSelection(doc.markerOption)
        binding.spnMarker.setOnItemClickListener { _, _, position, _ ->
            realm.beginTransaction()
            val rec = doc.gameProgress()
            doc.setMarkerOption(rec, position)
            realm.insertOrUpdate(rec)
            realm.commitTransaction()
        }
    }

    protected fun onDefault() {
        realm.beginTransaction()
        val rec = doc.gameProgress()
        doc.setMarkerOption(rec, 0)
        realm.insertOrUpdate(rec)
        realm.commitTransaction()
        binding.spnMarker.setSelection(doc.markerOption)
    }
}

class NeighboursHelpActivity : GameHelpActivity<NeighboursGame, NeighboursDocument, NeighboursGameMove, NeighboursGameState>() {
    private val document: NeighboursDocument by inject()
    override val doc get() = document
}