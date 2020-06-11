package com.zwstudio.logicpuzzlesandroid.common.android

import android.R
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.Spinner
import android.widget.TextView
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.domain.Game
import com.zwstudio.logicpuzzlesandroid.common.domain.GameState
import org.androidannotations.annotations.*
import java.sql.SQLException
import java.util.*

@EActivity
abstract class GameOptionsActivity<G : Game<G, GM, GS>?, GD : GameDocument<G, GM>?, GM, GS : GameState?> : BaseActivity() {
    abstract fun doc(): GD

    @kotlin.jvm.JvmField
    @ViewById
    var spnMarker: Spinner? = null

    @ViewById
    var ctvAllowedObjectsOnly: CheckedTextView? = null

    @AfterViews
    protected open fun init() {
        val lst = lstMarkers
        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String?>(this,
            R.layout.simple_spinner_item, lst) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                val s = lst[position]
                val tv = v.findViewById<View>(R.id.text1) as TextView
                tv.text = s
                return v
            }

            override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
                val v = super.getDropDownView(position, convertView, parent)
                val s = lst[position]
                val ctv = v.findViewById<View>(R.id.text1) as CheckedTextView
                ctv.text = s
                return v
            }
        }
        adapter.setDropDownViewResource(R.layout.simple_list_item_single_choice)
        spnMarker!!.adapter = adapter
        spnMarker!!.setSelection(doc().getMarkerOption())
        ctvAllowedObjectsOnly!!.isChecked = doc()!!.isAllowedObjectsOnly
    }

    @ItemSelect
    protected open fun spnMarkerItemSelected(selected: Boolean, position: Int) {
        val rec = doc()!!.gameProgress()
        doc()!!.setMarkerOption(rec!!, position)
        try {
            app!!.daoGameProgress!!.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    @Click
    protected fun ctvAllowedObjectsOnly() {
        val rec = doc()!!.gameProgress()
        ctvAllowedObjectsOnly!!.isChecked = !doc()!!.isAllowedObjectsOnly
        doc()!!.setAllowedObjectsOnly(rec!!, !doc()!!.isAllowedObjectsOnly)
        try {
            app!!.daoGameProgress!!.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    @Click
    protected fun btnDone() {
        finish()
    }

    @Click
    protected fun btnDefault() {
        yesNoDialog("Do you really want to reset the options?") {
            val rec = doc()!!.gameProgress()
            doc()!!.setMarkerOption(rec!!, 0)
            doc()!!.setAllowedObjectsOnly(rec, false)
            try {
                app!!.daoGameProgress!!.update(rec)
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            spnMarker!!.setSelection(doc().getMarkerOption())
            ctvAllowedObjectsOnly!!.isChecked = doc()!!.isAllowedObjectsOnly
        }
    }

    companion object {
        var lstMarkers = Arrays.asList("No Marker", "Marker First", "Marker Last")
    }
}