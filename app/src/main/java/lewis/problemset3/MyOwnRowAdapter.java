package lewis.problemset3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/*
 * Implements an adapter showing a list of items, each of which can be removed by long-clicking it.
 */
public class MyOwnRowAdapter extends ArrayAdapter<String> {

    Context adapterContext;
    ArrayList<String> toDoList;

    public MyOwnRowAdapter(Context context, ArrayList<String> list) {
        super(context, R.layout.layout_single_row, list);
        adapterContext = context;
        toDoList = list;
    }

    /*
     * In case the TO DO list was edited, saves changes to toDoList.txt.
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        try {
            // open output file
            PrintStream out = new PrintStream(adapterContext.getApplicationContext()
                    .openFileOutput("toDoList.txt", adapterContext.MODE_PRIVATE));

            // put tasks on separate lines in toDoList.txt
            int length = toDoList.size();
            for (int i = 0; i < length; i++) {
                out.println(toDoList.get(i));
            }

            // close output file
            out.close();
        }

        // if file not found, show error toast
        catch (IOException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(adapterContext, R.string.errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /*
     * Gets view that displays the data at the specified position from the TO DO list, and removes
     * this data from the list on long-clicking the item.
     */
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) adapterContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.layout_single_row, parent, false);
        }

        // place items of TO DO list in textview
        TextView itemTextView = (TextView) view.findViewById(R.id.itemTextView);
        final String item = toDoList.get(position);
        itemTextView.setText(item);

        // if user long-clicks item and confirms deletion, remove item from list
        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            public boolean onLongClick(View view) {

                // create alert dialog to get confirmation from user
                AlertDialog.Builder builder = new AlertDialog.Builder(adapterContext);
                builder.setMessage(adapterContext.getString(R.string.sure) + item
                        + adapterContext.getString(R.string.fromToDo));

                // if yes is clicked, remove item from list
                builder.setPositiveButton(R.string.yesSure, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        toDoList.remove(position);
                        notifyDataSetChanged();
                    }
                });

                // if no is clicked, do nothing
                builder.setNegativeButton(R.string.noNotSure, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int it) { }
                    });

                // show alert dialog
                AlertDialog confirmDelete = builder.create();
                confirmDelete.show();

                return true;
            }
        };

        view.setOnLongClickListener(longClickListener);

        return view;
    }
}
