package lewis.problemset3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Name: Lewis Zwart
 * Student number: 10251057
 *
 * Implements a TO-DO list, saved to the phone itself, to which the user can add new tasks.
 * Items can be removed by long-clicking them.
 */
public class MainActivity extends AppCompatActivity {

    ArrayList<String> tasks;

    /*
     * Loads TO DO list from memory and implements interface for user to add/remove items from it.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign variables to layout elements
        Button addButton = (Button) findViewById(R.id.AddButton);
        ListView toDoList = (ListView) findViewById(R.id.ToDoListView);
        final EditText newToDoEditText = (EditText) findViewById(R.id.NewToDoEditText);

        // load TO DO list and create adapter for it
        tasks = loadToDoList();
        final MyOwnRowAdapter adapter = new MyOwnRowAdapter(this, tasks);
        toDoList.setAdapter(adapter);

        // On clicking the ADD button, the typed task is added to the TO DO list.
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newItem = newToDoEditText.getText().toString();

                // if nonempty task is typed, add to TO DO list
                if (!newItem.equals("")) {
                    tasks.add(newItem);
                }

                // adjust TO DO list on screen
                adapter.notifyDataSetChanged();
                newToDoEditText.setText("");
            }
        });
    }

    /*
     * Open read former TO DO list from toDoList.txt; if file not found, use empty list.
     */
    public ArrayList<String> loadToDoList(){

        // initially, the TO DO list is empty
        ArrayList<String> list = new ArrayList<>();

        // read each line of the file as one item on TO DO list
        try{
            Scanner scan = new Scanner(openFileInput("toDoList.txt"));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                list.add(line);
            }
        }
        // on first usage, file does not exist, so don't show error to user if file not found
        catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
