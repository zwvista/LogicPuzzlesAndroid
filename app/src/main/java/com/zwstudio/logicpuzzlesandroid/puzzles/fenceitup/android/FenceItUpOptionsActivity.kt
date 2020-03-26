package com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.android

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.TextView
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.data.FenceItUpDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGame
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain.FenceItUpGameState
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ItemSelect
import java.sql.SQLException

@EActivity(R.layout.activity_game_options)
open class FenceItUpOptionsActivity : GameOptionsActivity<FenceItUpGame?, FenceItUpDocument?, FenceItUpGameMove?, FenceItUpGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: FenceItUpDocument? = null
    override fun doc(): FenceItUpDocument {
        return document!!
    }

    @AfterViews
    override fun init() {
        val lst = lstMarkers
        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String?>(this,
            android.R.layout.simple_spinner_item, lstMarkers) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                val s = lst[position]
                val tv = v.findViewById<View>(android.R.id.text1) as TextView
                tv.text = s
                return v
            }

            override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
                val v = super.getDropDownView(position, convertView, parent)
                val s = lst[position]
                val ctv = v.findViewById<View>(android.R.id.text1) as CheckedTextView
                ctv.text = s
                return v
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        spnMarker.adapter = adapter
        spnMarker.setSelection(doc().markerOption)
    }

    @ItemSelect
    override fun spnMarkerItemSelected(selected: Boolean, position: Int) {
        val rec = doc().gameProgress()
        doc().setMarkerOption(rec, position)
        try {
            app.daoGameProgress.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    protected fun onDefault() {
        val rec = doc().gameProgress()
        doc().setMarkerOption(rec, 0)
        try {
            app.daoGameProgress.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        spnMarker.setSelection(doc().markerOption)
    }
}