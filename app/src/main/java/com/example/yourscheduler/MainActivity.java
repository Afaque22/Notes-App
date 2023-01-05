package com.example.yourscheduler;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addBtn;
    ArrayList<ModelConstructors> arrModel;
    RVAdapater adapater;
    Toolbar toolbar1;
    EditText newTitle, newNote;
    Button saveBtn;
    TextView titleTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        addBtn = findViewById(R.id.addBtn);

        toolbar1 = findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar1);

        DatabaseManager dbhelper = new DatabaseManager(MainActivity.this);
        arrModel = dbhelper.fetchData();


        adapater = new RVAdapater(arrModel, MainActivity.this);
        recyclerView.setAdapter(adapater);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_update);

                newTitle = dialog.findViewById(R.id.updateTitle);
                newNote = dialog.findViewById(R.id.updateNote);
                saveBtn = dialog.findViewById(R.id.UpsaveBtn);
                titleTxt = dialog.findViewById(R.id.titletxt);
                titleTxt.setText("Add Note");

                newNote.addTextChangedListener(gettingInputWatcher);


                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        processInsert(newTitle.getText().toString(), newNote.getText().toString());

                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_filter, menu);
        MenuItem item = menu.findItem(R.id.search_menu);

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapater.getFilter().filter(newText);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void processInsert(String title, String note) {
        String res = new DatabaseManager(getApplicationContext()).addNotes(title, note);
        arrModel.add(new ModelConstructors(title, note));
        adapater.notifyItemInserted(arrModel.size() - 1);

        newTitle.setText("");
        newNote.setText("");
        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();

    }

    private TextWatcher gettingInputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String title = newTitle.getText().toString();
            String note = newNote.getText().toString();
            saveBtn.setEnabled(!title.isEmpty() && !note.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}