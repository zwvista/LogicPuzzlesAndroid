package com.zwstudio.logicpuzzlesandroid.common.android

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.Spinner
import android.widget.TextView
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.domain.Game
import com.zwstudio.logicpuzzlesandroid.common.domain.GameState
import com.zwstudio.logicpuzzlesandroid.home.android.realm
import org.androidannotations.annotations.*

@EActivity
abstract class GameOptionsActivity<G : Game<G, GM, GS>, GD : GameDocument<GM>, GM, GS : GameState<GM>> : BaseActivity() {
    abstract val doc: GD

    @ViewById
    lateinit var spnMarker: Spinner
    @ViewById
    lateinit var ctvAllowedObjectsOnly: CheckedTextView

    @AfterViews
    protected open fun init() {
        val lst = lstMarkers
        val adapter = object : ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, lst) {
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
        spnMarker.adapter = adapter
        spnMarker.setSelection(doc.markerOption)
        ctvAllowedObjectsOnly.isChecked = doc.isAllowedObjectsOnly
    }

    @ItemSelect
    protected open fun spnMarkerItemSelected(selected: Boolean, position: Int) {
        realm.beginTransaction()
        val rec = doc.gameProgress()
        doc.setMarkerOption(rec, position)
        realm.insertOrUpdate(rec)
        realm.commitTransaction()
    }

    @Click
    protected fun ctvAllowedObjectsOnly() {
        realm.beginTransaction()
        val rec = doc.gameProgress()
        ctvAllowedObjectsOnly.isChecked = !doc.isAllowedObjectsOnly
        doc.setAllowedObjectsOnly(rec, !doc.isAllowedObjectsOnly)
        realm.insertOrUpdate(rec)
        realm.commitTransaction()
    }

    @Click
    protected fun btnDone() {
        finish()
    }

    @Click
    protected fun btnDefault() {
        yesNoDialog("Do you really want to reset the options?") {
            realm.beginTransaction()
            val rec = doc.gameProgress()
            doc.setMarkerOption(rec, 0)
            doc.setAllowedObjectsOnly(rec, false)
            realm.insertOrUpdate(rec)
            realm.commitTransaction()
            spnMarker.setSelection(doc.markerOption)
            ctvAllowedObjectsOnly.isChecked = doc.isAllowedObjectsOnly
        }
    }

    companion object {
        var lstMarkers = listOf("No Marker", "Marker First", "Marker Last")
    }
}