package lk.javainstitute.transpo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    String stremail;
    String strLogpassword;
    String strfullName;
    Button loginButton, fbButton;
    Button googleButton;
    TextView tv;
    TextView forgotPasswordText;

    Intent intent;
    String email, displayName;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private SignInClient signInClient;
    public static final String TAG = RegistrationActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        tv = findViewById(R.id.signupText);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        googleButton = findViewById(R.id.googleButton);
        fbButton = findViewById(R.id.fbButton);
        signInClient = Identity.getSignInClient(getApplicationContext());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stremail = username.getText().toString().trim();
                strLogpassword = password.getText().toString().trim();

                if (stremail.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter your Email", Toast.LENGTH_SHORT).show();
                }
                if (strLogpassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter your Password", Toast.LENGTH_SHORT).show();
                } else {

                    if (!stremail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(stremail).matches()) {
                        firebaseAuth.signInWithEmailAndPassword(stremail, strLogpassword)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
                                    //    intent.putExtra("email", username.toString());
                                        intent.putExtra("email", stremail);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "Login Failed! Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        //fb signIn part

        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, FacebookLoginActivity.class));
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
                    // Handle unsuccessful authentication
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

            Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("DISPLAY_NAME", displayName);
            startActivity(intent);
            finish();
        }
    }

    private void saveUserDataToFirestore(String email, String displayName) {
        // Create a new user document in the 'UserProfile' collection
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
