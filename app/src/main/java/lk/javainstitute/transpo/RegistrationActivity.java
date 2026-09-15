package lk.javainstitute.transpo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class RegistrationActivity extends AppCompatActivity {

    EditText fullNameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    String strfullName;
    String stremail;
    String strpassword;
    String email, displayName;
    Button registerButton;
    Button googleButton, fbButton;
    TextView tv1;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private SignInClient signInClient;


    public static final String TAG = RegistrationActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        googleButton = findViewById(R.id.googleButton);
        fbButton = findViewById(R.id.fbButton);
        tv1 = findViewById(R.id.signInText);
        signInClient = Identity.getSignInClient(getApplicationContext());

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strfullName = fullNameEditText.getText().toString().trim();
                stremail = emailEditText.getText().toString().trim();
                strpassword = passwordEditText.getText().toString().trim();

                if (strfullName.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Enter your FullName", Toast.LENGTH_SHORT).show();
                }
                if (stremail.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Enter your Email", Toast.LENGTH_SHORT).show();
                }
                if (strpassword.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Enter your Password", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(stremail, strpassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.i(TAG, "createUserWithEmailAndPassword:success");
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        user.sendEmailVerification();
                                        UserProfile();
//                                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                        Toast.makeText(RegistrationActivity.this, "Please Verify Your Email..", Toast.LENGTH_LONG).show();
                                    } else {
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegistrationActivity.this, "Registration Failed! Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                }


           }
        });

                tv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    }
                });

                //fb signIn part

                fbButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(RegistrationActivity.this, FacebookLoginActivity.class));
                    }
                });


        //google signIn part

        findViewById(R.id.googleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetSignInIntentRequest getSignInIntentRequest = GetSignInIntentRequest.builder()
                        .setServerClientId(getString(R.string.web_client_id)).build();

                Task<PendingIntent> signIntent = signInClient.getSignInIntent(getSignInIntentRequest);
                signIntent.addOnSuccessListener(new OnSuccessListener<PendingIntent>() {
                    @Override
                    public void onSuccess(PendingIntent pendingIntent) {
                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(pendingIntent).build();
                        signInLauncher.launch(intentSenderRequest);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
            }

            private void UserProfile() {
                Map<String, String> userProfile = new HashMap<>();
                userProfile.put("FullName", strfullName);
                userProfile.put("Email", stremail);
                userProfile.put("Password", strpassword);

                firestore.collection("UserProfile").document(stremail).set(userProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });


            }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential(authCredential);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // Retrieve user's email and name
                        email = user.getEmail();
                        displayName = user.getDisplayName();

                        // Pass the email and name to updateUI method
                        updateUI(email, displayName);

                        // Save user data to Firestore
                        saveUserDataToFirestore(email, displayName);
                    }
                } else {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure
            }
        });
    }

    private void updateUI(String email, String displayName) {
        if (email != null && displayName != null) {

            Intent intent = new Intent(RegistrationActivity.this, SearchActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("DISPLAY_NAME", displayName);
            startActivity(intent);
            finish();
        }
    }

    private void saveUserDataToFirestore(String email, String displayName) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("FullName", displayName);

        // Add a new document with the user's UID
        firestore.collection("UserProfile")
                .document(email)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User document added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding user document", e);
                    }
                });
    }


    private void handleSignInResult(Intent intent) {
        try {
            SignInCredential signInCredential = signInClient.getSignInCredentialFromIntent(intent);
            String idToken = signInCredential.getGoogleIdToken();
            firebaseAuthWithGoogle(idToken);
        } catch (ApiException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private final ActivityResultLauncher<IntentSenderRequest> signInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            handleSignInResult(result.getData());
                        }
                    });
        }