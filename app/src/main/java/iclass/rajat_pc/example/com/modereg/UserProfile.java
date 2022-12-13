package iclass.rajat_pc.example.com.modereg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private EditText userName;
    private EditText userEmail, userPassword;
    private Button logoutButton ,updateButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mref;
    private Boolean isFaculty;
    private ImageView user_profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        userName = (EditText) findViewById(R.id.username);
        userEmail = (EditText) findViewById(R.id.useremail);
        updateButton = (Button) findViewById(R.id.update_button);
        userPassword = (EditText) findViewById(R.id.userpassword);
        logoutButton = (Button) findViewById(R.id.logout_button);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mref =firebaseDatabase.getReference();
        user_profile_image = (ImageView) findViewById(R.id.user_profile_image);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(UserProfile.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
        getUserDetails();

    }

    public void getUserDetails(){
        mref.child("users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getUserName());
                userEmail.setText(user.getEmail());
                isFaculty = user.isFaculty();
                if (isFaculty){
                    user_profile_image.setImageResource(R.mipmap.professor);
                }
                else {
                    user_profile_image.setImageResource(R.mipmap.graduate);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void updateProfile(){
       String password = userPassword.getText().toString();
       String email = userEmail.getText().toString();
       String name = userName.getText().toString();
       
       //you can also authenticate
        
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail().toString(), password); // Current Login Credentials \\
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //Now change your email address \\
                        //----------------Code for Changing Email Address----------\\
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UserProfile.this, "User email updated successfully", Toast.LENGTH_SHORT).show();
                                            mref.child("users").child(mAuth.getCurrentUser().getUid()).child("email").setValue(email);
                                            mref.child("users").child(mAuth.getCurrentUser().getUid()).child("name").setValue(name);
                                        }
                                    }
                                });
                        //----------------------------------------------------------\\
                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            if (isFaculty){
                Intent intent = new Intent(this, TeacherHome.class);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }


        }


        return super.onOptionsItemSelected(item);
    }
}
