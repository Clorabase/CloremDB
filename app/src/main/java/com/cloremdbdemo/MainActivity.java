package com.cloremdbdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.clorem.db.Clorem;
import com.clorem.db.Database;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DeleteDialog.DeleteListener {
    private RecyclerView mainRv;
    private ArrayList<Demo> demoObjectsArrayList;
    private LinearLayoutManager mLinearLayoutManager;
    private DemoAdapter mAdapter;
    private Database db;
    private EditText demoTextEditText;
    private Button addDemoButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Initializing main activity recycler view
        mainRv = findViewById(R.id.main_rv);
        demoObjectsArrayList = new ArrayList<>();
        mAdapter = new DemoAdapter(this, demoObjectsArrayList);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mainRv.setLayoutManager(mLinearLayoutManager);
        mainRv.setAdapter(mAdapter);


        // Initializing database
        db = Clorem.getInstance(this, "demoDb").getDatabase();


        /*  Loading data from database  */
        /* Getting a list of all the keys in the database */
        List<String> keysList = db.getChildren();
        for (String key : keysList) { //Iterating through the keys
            db.root();
            String demoText = db.node(key).getString(key);  //Read operation from clorem db
            Demo demo = new Demo(key,demoText);
            mAdapter.addObject(demo);
        }


        demoTextEditText = findViewById(R.id.demo_text_edit_text);
        addDemoButton=findViewById(R.id.add_button);


        //Adding text change listener to edit text as we don't want as empty entry in our db
        demoTextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().trim().equals("")){
                    addDemoButton.setClickable(true);
                }
            }
        });



        //Adding a text change listener to add button as we will be using this button as update button as well
        addDemoButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("Add")){
                    addDemoButton.setOnClickListener(l->{
                        String demoText = demoTextEditText.getText().toString();
                        if (!TextUtils.isEmpty(demoText.trim())) {
                            long timestamp = System.currentTimeMillis();
                            db.newNode(String.valueOf(timestamp)).put(String.valueOf(timestamp), demoText).commit();    //Create operation from clorem db
                            demoTextEditText.setText("");
                            Demo demo = new Demo(String.valueOf(timestamp),demoText);
                            mAdapter.addObject(demo);
                        }
                    });
                }
            }
        });


    }

    //Below two methods are from our defined interfaces in delete dialog class
    @Override
    public void onDeleteClicked(String timestamp) {
        db.root();
        db.remove(timestamp).commit();  //Delete operation from clorem db
        mAdapter.deleteObject(timestamp);
    }


    @Override
    public void onUpdateClicked(String timestamp,String demoText) {
        demoTextEditText.setText(demoText);
        addDemoButton.setText("Update");
        addDemoButton.setOnClickListener(l->{
            db.root();
            db.node(timestamp).put(timestamp,demoTextEditText.getText().toString()).commit();  //Update operation from clorem db
            mAdapter.updateObject(timestamp,demoTextEditText.getText().toString());
            demoTextEditText.setText("");
            addDemoButton.setText("Add");

        });
    }
}