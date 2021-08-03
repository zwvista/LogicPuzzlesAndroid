package com.zwstudio.logicpuzzlesandroid.common.android

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.domain.Game
import com.zwstudio.logicpuzzlesandroid.common.domain.GameState
import com.zwstudio.logicpuzzlesandroid.databinding.ActivityGameOptionsBinding
import com.zwstudio.logicpuzzlesandroid.home.android.realm

abstract class GameOptionsActivity<G : Game<G, GM, GS>, GD : GameDocument<GM>, GM, GS : GameState<GM>> : AppCompatActivity() {
    abstract val doc: GD

    protected lateinit var binding: ActivityGameOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.spnMarker.adapter = adapter
        binding.spnMarker.setSelection(doc.markerOption)
        binding.ctvAllowedObjectsOnly.isChecked = doc.isAllowedObjectsOnly
        binding.spnMarker.setOnItemClickListener { parent, view, position, id ->
            realm.beginTransaction()
            val rec = doc.gameProgress()
            doc.setMarkerOption(rec, position)
            realm.insertOrUpdate(rec)
            realm.commitTransaction()
        }
        binding.ctvAllowedObjectsOnly.setOnClickListener {
            realm.beginTransaction()
            val rec = doc.gameProgress()
            binding.ctvAllowedObjectsOnly.isChecked = !doc.isAllowedObjectsOnly
            doc.setAllowedObjectsOnly(rec, !doc.isAllowedObjectsOnly)
            realm.insertOrUpdate(rec)
            realm.commitTransaction()
        }
        binding.btnDone.setOnClickListener {
            finish()
        }
        binding.btnDefault.setOnClickListener {
            yesNoDialog("Do you really want to reset the options?") {
                realm.beginTransaction()
                val rec = doc.gameProgress()
                doc.setMarkerOption(rec, 0)
                doc.setAllowedObjectsOnly(rec, false)
                realm.insertOrUpdate(rec)
                realm.commitTransaction()
                binding.spnMarker.setSelection(doc.markerOption)
                binding.ctvAllowedObjectsOnly.isChecked = doc.isAllowedObjectsOnly
            }
        }
    }

    companion object {
        var lstMarkers = listOf("No Marker", "Marker First", "Marker Last")
    }
}