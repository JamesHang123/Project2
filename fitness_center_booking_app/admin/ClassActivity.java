package fitness_center_booking_app.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import fitness_center_booking_app.ClassEventType;
import fitness_center_booking_app.R;
import fitness_center_booking_app.entity.FitnessClass;

//create, edit and delete a class
public class ClassActivity extends AppCompatActivity {
    private EditText nameEditText, descriptionEditText;
    private String name, description;
    private List<FitnessClass> fitnessClassList;
    private ClassEventType classEventType;
    private FitnessClass classItem;

    //database
    private DatabaseReference classesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        init();
    }

    public void init() {
        Intent intent = getIntent();
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        fitnessClassList = new LinkedList<>();
        classEventType = (ClassEventType) intent.getSerializableExtra("classEventType");
        if (classEventType == ClassEventType.UPDATE_OR_DELETE) {
            classItem = (FitnessClass) intent.getSerializableExtra("classItem");
            name = classItem.getName();
            description = classItem.getDescription();
            nameEditText.setText(name);
            descriptionEditText.setText(description);
            nameEditText.setEnabled(false);
        }
        //get all class data from database
        classesReference = FirebaseDatabase.getInstance().getReference("classes");
        classesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fitnessClassList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    FitnessClass fitnessClass = child.getValue(FitnessClass.class);
                    fitnessClassList.add(fitnessClass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void save(View view) {
        name = nameEditText.getText().toString();
        description = descriptionEditText.getText().toString();
        if (isValid()) {
            if (classEventType == ClassEventType.CREATE) {
                String key = classesReference.push().getKey();
                FitnessClass fitnessClass = new FitnessClass(key, name, description);
                classesReference.child(key).setValue(fitnessClass);
            } else {
                classItem.setDescription(description);
                classesReference.child(classItem.getKey()).setValue(classItem);
            }
            finish();
        }
    }

    public void delete(View view) {
        classesReference.child(classItem.getKey()).setValue(null);
        //classesReference.child(classItem.getKey()).removeValue();
        finish();
    }

    public boolean isValid() {
        if (name.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Class name cannot be empty.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }
        if (description.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Class description cannot be empty.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }
        if (FitnessClass.contains(fitnessClassList, name) && classEventType == ClassEventType.CREATE) {
            Toast toast = Toast.makeText(getApplicationContext(), "The class has already existed.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }
        return true;
    }
}