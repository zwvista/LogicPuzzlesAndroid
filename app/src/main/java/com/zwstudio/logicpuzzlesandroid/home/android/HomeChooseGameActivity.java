package com.zwstudio.logicpuzzlesandroid.home.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.BaseActivity;
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;
import java.util.List;

@EActivity(R.layout.activity_home_choose_game)
public class HomeChooseGameActivity extends BaseActivity {
    public HomeDocument doc() {return app.homeDocument;}

    @ViewById
    ListView lvGames;

    @AfterViews
    protected void init() {
        List<String> lst = Arrays.asList("Abc", "Bridges", "Clouds", "Hitori", "LightUp",
                "Masyu", "Nurikabe", "Skyscrapers", "SlitherLink");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, lst) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                String s = lst.get(position);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);
                tv.setText(s);
                return v;
            }
        };
        lvGames.setAdapter(adapter);
        lvGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String gameName = lst.get(i);
                doc().resumeGame(gameName);
                Intent data = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("gameName", gameName);
                data.putExtras(bundle);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    @Click
    protected void btnCancel() {
        finish();
    }

}
