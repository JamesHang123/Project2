package fitness_center_booking_app.instructor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import fitness_center_booking_app.ClassEventType;
import fitness_center_booking_app.R;
import fitness_center_booking_app.entity.Account;

public class EmployeeHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);
    }

    public void scheduleClass(View view) {
        Intent intent = new Intent(this, ScheduleClassActivity.class);
        intent.putExtra("account", (Account)getIntent().getSerializableExtra("account"));
        intent.putExtra("eventType", ClassEventType.CREATE);
        startActivity(intent);
    }

    public void viewAllScheduledClasses(View view) {
        Intent intent = new Intent(this, AllScheduledClassesActivity.class);
        intent.putExtra("account", (Account)getIntent().getSerializableExtra("account"));
        startActivity(intent);
    }
}