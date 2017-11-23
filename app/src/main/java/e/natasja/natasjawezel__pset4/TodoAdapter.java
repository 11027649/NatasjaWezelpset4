package e.natasja.natasjawezel__pset4;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

/**
 * Created by Natasja on 21-11-2017.
 */

public class TodoAdapter extends ResourceCursorAdapter {
    public TodoAdapter(Context context, Cursor cursor) {
        super(context, R.layout.row_todo, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView todo = (TextView) view.findViewById(R.id.todo);
        CheckBox completed = (CheckBox) view.findViewById(R.id.completed);

        Integer title_i = cursor.getColumnIndex("title");
        Integer completed_i = cursor.getColumnIndex("completed");

        String title_value = cursor.getString(title_i);
        Integer completed_value = cursor.getInt(completed_i);

        todo.setText(title_value);

        if (completed_value == 1) {
            Log.d("MainActivity", "Checking: set false because completed_value = " + completed_value);
            todo.setPaintFlags(todo.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            completed.setChecked(false);
        } else {
            Log.d("MainActivity", "Checking: set true because completed_value = " + completed_value);
            todo.setPaintFlags(todo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            completed.setChecked(true);
        }
    }
}
