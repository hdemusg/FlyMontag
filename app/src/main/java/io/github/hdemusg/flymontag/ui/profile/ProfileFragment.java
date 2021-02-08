package io.github.hdemusg.flymontag.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.github.hdemusg.flymontag.MainActivity;
import io.github.hdemusg.flymontag.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

/*
  Author: Sumedh Garimella
  Description: The Profile app will be where users login and check various statuses in the future.
  Currently supports Google Sign-In through Firebase.
 */

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    static SignInButton signin = null;
    static TextView email = null;
    static Button signout = null;
    private FirebaseAuth mAuth = null;

    View root = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        //final TextView textView = root.findViewById(R.id.text_profile);
        //profileViewModel.getText().observe(this, new Observer<String>() {
        //    @Override
        //    public void onChanged(@Nullable String s) {
        //       textView.setText(s);
        //    }
        //});
// ...
// Initialize Firebase Auth
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            setAccount(currentUser.getEmail());
        }

        signin = (SignInButton) root.findViewById(R.id.signin);

        signout = (Button) root.findViewById(R.id.signout);
        signout.setVisibility(View.GONE);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                setAccount(null);
            }
        });

        final GoogleSignInClient client = MainActivity.getClient();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = client.getSignInIntent();
                startActivityForResult(signInIntent, 1);
            }
        });

        email = root.findViewById(R.id.email);

        return root;
    }

    public static SignInButton getSignin() {
        return signin;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
            firebaseAuthWithGoogle(account.getIdToken());
            // Signed in successfully, show authenticated UI.
            setAccount(account.getEmail());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "Google sign in failed", e);
        }
    }

    public static void setAccount(String e) {
        if (email != null) {
            if (e != null) {
                email.setText(e);
                signout.setVisibility(View.VISIBLE);
                signin.setVisibility(View.GONE);
            } else {
                email.setText("You are not logged in.");
                signout.setVisibility(View.GONE);
                signin.setVisibility(View.VISIBLE);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            setAccount(user.getEmail());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }
}