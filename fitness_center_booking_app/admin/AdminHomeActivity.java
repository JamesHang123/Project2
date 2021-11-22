package fitness_center_booking_app.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fitness_center_booking_app.ClassEventType;
import fitness_center_booking_app.R;
import fitness_center_booking_app.entity.Account;
import fitness_center_booking_app.entity.FitnessClass;

public class AdminHomeActivity extends AppCompatActivity {
    private ListView classListView, accountListView;
    private List<FitnessClass> fitnessClassList;
    private List<Account> accountList;

    private DatabaseReference classesReference, accountsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        classListView = (ListView) findViewById(R.id.classListView);
        accountListView = (ListView) findViewById(R.id.accountListView) ;
        classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FitnessClass classItem = fitnessClassList.get(i);
                Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
                intent.putExtra("classItem", classItem);
                intent.putExtra("classEventType", ClassEventType.UPDATE_OR_DELETE);
                startActivity(intent);
            }
        });
        fitnessClassList = new LinkedList<>();
        accountList = new LinkedList<>();

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
                List<Map<String, String>> data = new LinkedList<>();
                for (FitnessClass fitnessClass : fitnessClassList) {
                    Map<String, String> classItem = new HashMap<>();
                    classItem.put("name", fitnessClass.getName());
                    classItem.put("description", fitnessClass.getDescription());
                    data.add(classItem);
                }
                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.class_list_item_layout,
                        new String[]{"name", "description"}, new int[]{R.id.nameTextView, R.id.descriptionTextView});
                classListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        accountsReference = FirebaseDatabase.getInstance().getReference("accounts");
        accountsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accountList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    accountList.add(child.getValue(Account.class));
                }
                List<Map<String, String>> data = new LinkedList<>();
                for (Account account : accountList) {
                    Map<String, String> accountItem = new HashMap<>();
                    accountItem.put("name", account.getUserName());
                    accountItem.put("password", account.getPassword());
                    data.add(accountItem);
                }
                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.account_list_item_layout,
                        new String[]{"name", "password"}, new int[]{R.id.nameTextView, R.id.passwordTextView});
                accountListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createClass(View view) {
        Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
        intent.putExtra("classEventType", ClassEventType.CREATE);
        startActivity(intent);
    }
}