package fitness_center_booking_app.main;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import java.util.LinkedList;
import java.util.List;

import fitness_center_booking_app.R;
import fitness_center_booking_app.entity.Account;
import fitness_center_booking_app.entity.Role;

public class RegisterActivity extends AppCompatActivity {

    private EditText userNameText, passwordText;
    private RadioGroup roleGroup;

    private String userName;
    private String password;
    private Role role;

    //db
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference accountsReference = db.getReference("accounts");
    List<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Finish!");
    }

    private void init() {
        userNameText = (EditText) findViewById(R.id.userNameText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        roleGroup = (RadioGroup) findViewById(R.id.roleGroup);
        accounts = new LinkedList<>();
        accountsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accounts.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Account account = child.getValue(Account.class);
                    accounts.add(account);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void register(View view) {
        userName = userNameText.getText().toString();
        password = passwordText.getText().toString();
        if (isValid()) {
            String key = accountsReference.push().getKey();
            Account account = new Account(key, userName, password, role);
            accountsReference.child(key).setValue(account);
            finish();
        }
    }

    private boolean isValid() {
        if (userName.equals("")) {
            Toast toast = Toast.makeText(this, "User name cannot be empty!", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (password.equals("")) {
            Toast toast = Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        switch (roleGroup.getCheckedRadioButtonId()) {
            case R.id.customerBtn:
                role = Role.CUSTOMER;
                break;
            case R.id.employeeBtn:
                role = Role.EMPLOYEE;
                break;
            default:
                //have not select a role button
                Toast toast = Toast.makeText(this, "You have to choose a role!", Toast.LENGTH_SHORT);
                toast.show();
                return false;
        }
        if (Account.contains(accounts, userName)) {
            Toast toast = Toast.makeText(this, "The user name already exists!", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    public void cancel(View view) {
        finish();
    }
}