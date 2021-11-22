package fitness_center_booking_app.instructor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import fitness_center_booking_app.ClassEventType;
import fitness_center_booking_app.R;
import fitness_center_booking_app.entity.Account;
import fitness_center_booking_app.entity.DifficultyLevel;
import fitness_center_booking_app.entity.FitnessClass;
import fitness_center_booking_app.entity.ScheduledFitnessClass;

public class ScheduleClassActivity extends AppCompatActivity {
    private Account account;
    private ClassEventType eventType;
    private ScheduledFitnessClass scheduledFitnessClass;

    //all views
    private Spinner fitnessNameSpinner;
    private Spinner difficultySpinner;
    private Spinner daySpinner;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private EditText capacityEditText;
    private Button cancelBtn;

    //db
    private DatabaseReference classesReference;
    private DatabaseReference scheduledClassesReference;
    private List<FitnessClass> fitnessClassList;
    private List<ScheduledFitnessClass> scheduledFitnessClassList;

    //scheduled fitness class attributes
    private String name;
    private DifficultyLevel difficultyLevel;
    private DayOfWeek day;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private int capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_class);

        //init views
        fitnessNameSpinner = (Spinner) findViewById(R.id.fitnessNameSpinner);
        difficultySpinner = (Spinner) findViewById(R.id.difficultySpinner);
        daySpinner = (Spinner) findViewById(R.id.daySpinner);
        startTimePicker = (TimePicker) findViewById(R.id.startTimePicker);
        endTimePicker = (TimePicker) findViewById(R.id.endTimePicker);
        capacityEditText = (EditText) findViewById(R.id.capacityEditText);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        Intent intent = getIntent();
        account = (Account) intent.getSerializableExtra("account");
        eventType = (ClassEventType) intent.getSerializableExtra("eventType");
        if (eventType == ClassEventType.CREATE) {
            cancelBtn.setEnabled(false);
        } else {
            scheduledFitnessClass = (ScheduledFitnessClass) intent.getSerializableExtra("scheduledFitnessClass");
        }

        //init db
        classesReference = FirebaseDatabase.getInstance().getReference("classes");
        scheduledClassesReference = FirebaseDatabase.getInstance().getReference("scheduledClasses");
        fitnessClassList = new LinkedList<>();
        scheduledFitnessClassList = new LinkedList<>();
        List<String> names = new LinkedList<>();
        classesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fitnessClassList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    FitnessClass fitnessClass = child.getValue(FitnessClass.class);
                    fitnessClassList.add(fitnessClass);
                    names.add(fitnessClass.getName());
                }
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, names);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fitnessNameSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fitnessNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                name = names.get(i);
                System.out.println(name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        scheduledClassesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scheduledFitnessClassList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    scheduledFitnessClassList.add(child.getValue(ScheduledFitnessClass.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayAdapter difficultyAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, DifficultyLevel.values());
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                difficultyLevel = DifficultyLevel.values()[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter dayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, DayOfWeek.values());
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                day = DayOfWeek.values()[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        startTimePicker.setIs24HourView(true);
        startTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                startHour = i;
                startMinute = i1;
            }
        });

        endTimePicker.setIs24HourView(true);
        endTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                endHour = i;
                endMinute = i1;
            }
        });

        if (eventType == ClassEventType.UPDATE_OR_DELETE) {
            classesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fitnessClassList.clear();
                    names.clear();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        FitnessClass fitnessClass = child.getValue(FitnessClass.class);
                        fitnessClassList.add(fitnessClass);
                        names.add(fitnessClass.getName());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    fitnessNameSpinner.setAdapter(adapter);
                    fitnessNameSpinner.setSelection(names.indexOf(scheduledFitnessClass.getName()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            difficultySpinner.setSelection(Arrays.asList(DifficultyLevel.values()).indexOf(scheduledFitnessClass.getDifficultyLevel()));
            daySpinner.setSelection(Arrays.asList(DayOfWeek.values()).indexOf(scheduledFitnessClass.getDay()));
            startTimePicker.setHour(scheduledFitnessClass.getStartHour());
            startTimePicker.setMinute(scheduledFitnessClass.getStartMinute());
            endTimePicker.setHour(scheduledFitnessClass.getEndHour());
            endTimePicker.setMinute(scheduledFitnessClass.getEndMinute());
            capacityEditText.setText(Integer.toString(scheduledFitnessClass.getCapacity()));

            if (!scheduledFitnessClass.getEmployeeName().equals(account.getUserName())) {
                cancelBtn.setEnabled(false);
            }
        }
    }

    public void save(View view) {
        if (isValid()) {
            String key = "";
            if (eventType == ClassEventType.CREATE) {
                key = scheduledClassesReference.push().getKey();
            } else {
                key = scheduledFitnessClass.getKey();
            }
            ScheduledFitnessClass scheduledFitnessClass = new ScheduledFitnessClass(key, name, account.getUserName(), difficultyLevel, day, startHour, startMinute, endHour, endMinute, capacity);
            scheduledClassesReference.child(key).setValue(scheduledFitnessClass);
            finish();
        }
    }

    public void cancel(View view) {
        scheduledClassesReference.child(scheduledFitnessClass.getKey()).removeValue();
        finish();
    }

    private boolean isValid() {
        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        String key = "";
        if (eventType == ClassEventType.UPDATE_OR_DELETE) {
            key = scheduledFitnessClass.getKey();
        }
        if (ScheduledFitnessClass.scheduledOnTheSameDay(scheduledFitnessClassList, key,name, day)) {
            toast.setText("You cannot schedule the same fitness class on the same day.");
            toast.show();
            return false;
        }
        if (ScheduledFitnessClass.timeBefore(endHour, endMinute, startHour, startMinute)) {
            toast.setText("End time is before of start time.");
            toast.show();
            return false;
        }
        if (ScheduledFitnessClass.isConflict(scheduledFitnessClassList, key,account.getUserName(), day, startHour, startMinute, endHour, endMinute)) {
            toast.setText("Time conflict.");
            toast.show();
            return false;
        }
        try {
            capacity = Integer.parseInt(capacityEditText.getText().toString());
        } catch (NumberFormatException e) {
            toast.setText("Capacity is not valid.");
            toast.show();
            return false;
        }
        return true;
    }
}