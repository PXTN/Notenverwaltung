package com.factbz.notenverwaltung;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.factbz.notenverwaltung.Adapter.SemesterAdapter;
import com.factbz.notenverwaltung.Data.DBAdapter;
import com.factbz.notenverwaltung.Dialog.AddSemesterDialogFragment;
import com.factbz.notenverwaltung.Model.Semester;

import java.util.ArrayList;

public class SemesterActivity extends AppCompatActivity implements AddSemesterDialogFragment.SemesterDialogListener {

    public DBAdapter dbAdapter;
    private SemesterAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester);

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        ArrayList<Semester> mArrayList = new ArrayList<Semester>();
        try {
            Cursor mCursor = dbAdapter.getAllSemesters();
            for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
                // The Cursor is now set to the right position
                mArrayList.add(new Semester(mCursor.getInt(0),mCursor.getString(1)));
            }
        }catch (Exception e){
            // ignore
            DialogFragment d = new AddSemesterDialogFragment();
            d.show(getFragmentManager(),"Semester");
        }

        adapter = new SemesterAdapter(this, mArrayList);

        ListView listView = (ListView) findViewById(R.id.lvSemester);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), SubjectActivity.class);
                intent.putExtra("SemesterID", adapter.getItem(i).id);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSemester);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment d = new AddSemesterDialogFragment();
                d.show(getFragmentManager(),"Semester");
            }
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog,String name) {
        dbAdapter.insertSemester(name);
        adapter.add(new Semester(dbAdapter.getSemesterByName(name).getInt(0),name));
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Cancel
    }
}
