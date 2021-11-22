package fitness_center_booking_app.instructor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fitness_center_booking_app.ClassEventType;
import fitness_center_booking_app.R;
import fitness_center_booking_app.entity.ScheduledFitnessClass;

public class AllScheduledClassesActivity extends AppCompatActivity {
    //all views
    private SearchView searchView;
    private ListView scheduledClassesListView;

    //db
    private DatabaseReference scheduledClassesReference;
    private List<ScheduledFitnessClass> scheduledFitnessClassList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_scheduled_classes);

        //init views
        searchView = (SearchView) findViewById(R.id.searchView);
        scheduledClassesListView = (ListView) findViewById(R.id.scheduledClassesListView);
        scheduledClassesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.keyTextView);
                Intent intent = new Intent(getApplicationContext(), ScheduleClassActivity.class);
                intent.putExtra("account", getIntent().getSerializableExtra("account"));
                intent.putExtra("scheduledFitnessClass", scheduledFitnessClassList.get(ScheduledFitnessClass.getIndex(scheduledFitnessClassList, textView.getText().toString())));
                intent.putExtra("eventType", ClassEventType.UPDATE_OR_DELETE);
                startActivity(intent);
            }
        });
        scheduledClassesListView.setTextFilterEnabled(true);

        //init db
        scheduledClassesReference = FirebaseDatabase.getInstance().getReference("scheduledClasses");
        scheduledFitnessClassList = new LinkedList<>();
        scheduledClassesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scheduledFitnessClassList.clear();
                List<Map<String, String>> data = new LinkedList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    ScheduledFitnessClass scheduledFitnessClass = child.getValue(ScheduledFitnessClass.class);
                    scheduledFitnessClassList.add(scheduledFitnessClass);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("key", scheduledFitnessClass.getKey());
                    map.put("className", scheduledFitnessClass.getName());
                    map.put("employeeName", scheduledFitnessClass.getEmployeeName());
                    map.put("difficulty", scheduledFitnessClass.getDifficultyLevel().toString());
                    map.put("day", scheduledFitnessClass.getDay().toString());
                    data.add(map);
                }
                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.scheduled_list_item_layout,
                        new String[] {"key", "className", "employeeName", "difficulty", "day"},
                        new int[] {R.id.keyTextView, R.id.classNameTextView, R.id.employeeNameTextView, R.id.difficultyTextView, R.id.dayTextView});
                scheduledClassesListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s == null || s.equals("")) {
                    scheduledClassesListView.clearTextFilter();
                } else {
                    scheduledClassesListView.setFilterText(s);
                }
                return false;
            }
        });
    }
}