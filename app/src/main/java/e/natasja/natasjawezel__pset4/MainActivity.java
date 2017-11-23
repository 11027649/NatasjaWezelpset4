package e.natasja.natasjawezel__pset4;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /* set up some global variables */
    TodoDatabase mTodoDatabase;
    private Button addButton, deleteButton;
    private EditText thingTODO;
    private ListView Todos;
    TodoAdapter adapter;
    private long deleteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* find IDs from the layout objects */
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);
        thingTODO = findViewById(R.id.thingTODO);
        Todos = findViewById(R.id.todoList);

        /* get the (only) instance of the database */
        mTodoDatabase = TodoDatabase.getInstance(this);

        Todos.setLongClickable(true);
        Todos.setOnItemLongClickListener(new ClickLong());
        Todos.setOnItemClickListener(new Click());

        /* populate the list with the items in the todoList */
        updateData();
    }

    /**
     * the delete button appears when long clicked on a ListItem. if the delete button is clicked,
     * it deletes the list item that was long clicked on.
     *
     * TODO -- make sure that items with quotes in it can be deleted as well
     * @param view
     */
    public void Delete(View view) {

        // get ItemID from the list item that was long clicked on.

            Log.d("MainActivity", "onItemLongClick: the ID = " + deleteID);
            mTodoDatabase.deleteData(deleteID);
            updateData();

        /* sets the deletebutton to invisible again */
        deleteButton.setVisibility(View.INVISIBLE);
    }

    /**
     * The text that is typed in the textview will be added to the database.
     * @param newEntry
     */
    public void AddData(String newEntry) {
        boolean insertData = mTodoDatabase.addData(newEntry);

        if (insertData) {
            toastMessage("Data successfully inserted!");
        } else {
            toastMessage("Something went wrong.. :C ");
        }
    }

    /**
     * The text the user typed in the textview will be inserted in the list and displayed. The
     * textview must be resetted to add in another item in the todoList
     * @param view
     */
    public void addItem(View view) {
        String newEntry = thingTODO.getText().toString();

        /* Escape " and ' by putting them in the database with a backslash */

        if (thingTODO.length() != 0) {
            AddData(newEntry);
            updateData();
            thingTODO.setText("");
            thingTODO.setHint("ToDo");
        } else {
            toastMessage("You must put something in the textfield.");
        }
    }

    /**
     * Customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Display the data in the listview.
     */
    private void updateData() {
        Log.d("MainActivity", "updateData: Displaying data in the listview");

        /* get the data and initialize a list */
        Cursor data = mTodoDatabase.selectAll();
        List<String> listData = new ArrayList<>();

        while (data.moveToNext()) {
            /* get the value from the database in column 1 and add it to the arraylist */
            listData.add(data.getString(1));
        }

        Cursor cursor = mTodoDatabase.selectAll();
        /* set up a new adapter */
        adapter = new TodoAdapter(this, cursor);

        Todos.setAdapter(adapter);
    }

    /** make the list listen to clicks. if the list is clicked, check the right checkbox and
     * complete the data in the SQLite database
     */
    private class Click implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Log.d("MainActivity", "onItemClick: You Clicked on: " + id);

            mTodoDatabase.update(id);
            adapter.notifyDataSetChanged();
            updateData();
        }

    }

    /**
     *  make the list listen to long clicks. if an item is long clicked, display delete button
     */
    private class ClickLong implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            deleteButton.setVisibility(View.VISIBLE);
            deleteID = id;

            updateData();
                /* save the ID to delete later on */
            Log.d("MainActivity", "onLongItemClick: You Long Clicked on " + id);

            return true;
        }
    }

}
