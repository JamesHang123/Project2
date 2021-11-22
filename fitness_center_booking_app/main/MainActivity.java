package fitness_center_booking_app.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import java.util.LinkedList;
import java.util.List;

import fitness_center_booking_app.R;
import fitness_center_booking_app.admin.AdminHomeActivity;
import fitness_center_booking_app.entity.Account;
import fitness_center_booking_app.entity.Role;
import fitness_center_booking_app.instructor.EmployeeHomeActivity;

public class MainActivity extends AppCompatActivity {
    private EditText userNameText;
    private EditText passwordText;
    private String userName;
    private String password;

    //db attributes
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference accountsReference = db.getReference("accounts");
    List<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        userNameText = (EditText) findViewById(R.id.userNameText);
        passwordText = (EditText) findViewById(R.id.passwordText);
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
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        userName = userNameText.getText().toString();
        password = passwordText.getText().toString();
        if (isValid()) {
            for (Account account : accounts) {
                if (account.getUserName().equals(userName)) {
                    if (account.getRole() == Role.ADMIN) {
                        Intent intent = new Intent(this, AdminHomeActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    } else if (account.getRole() == Role.EMPLOYEE) {
                        Intent intent = new Intent(this, EmployeeHomeActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                }
            }
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
        if (!Account.contains(accounts, userName)) {
            Toast toast = Toast.makeText(this, "The user name doesn't exist!", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (!Account.contains(accounts, userName, password)) {
            Toast toast = Toast.makeText(this, "The password is incorrect!", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }
}