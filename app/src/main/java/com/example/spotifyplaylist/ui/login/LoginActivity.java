package com.example.spotifyplaylist.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotifyplaylist.R;
import com.example.spotifyplaylist.ui.login.LoginViewModel;
import com.example.spotifyplaylist.ui.login.LoginViewModelFactory;
import com.example.spotifyplaylist.databinding.ActivityLoginBinding;

import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;
import com.spotify.sdk.android.auth.AuthorizationClient;
//import com.spotify.android.appremote.api.Connector;
//import com.spotify.android.appremote.api.SpotifyAppRemote;
//
//import com.spotify.protocol.client.Subscription;
//import com.spotify.protocol.types.PlayerState;
//import com.spotify.protocol.types.Track;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private static final String CLIENT_ID = "e30929e731664b3f86b922d87115dc59";
    private static final String REDIRECT_URI = "http://localhost:8888/callback/";
    private static final int REQUEST_CODE = 1337;
    String token = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        //final String CLIENT_ID = binding.username.toString();
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {// login was successful
                    updateUiWithUser(loginResult.getSuccess());
                    //Intent intent = new Intent(getApplicationContext(),LoginActivity.class); example of intent

                        final AuthorizationRequest request = new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
                                 .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
                                 .setShowDialog(true)
                                 .build();
                        //request.toUri(); testing random code
                        AuthorizationClient.openLoginActivity(LoginActivity.this, REQUEST_CODE, request); // This should try to authenticate with the 3 params, and by default if spotify isnt on the device it goes to online sign in.
                       
                        // AuthorizationClient.getResponse(Activity.RESULT_OK, AuthorizationClient.createLoginActivityIntent(LoginActivity.this,request)).getAccessToken(); // need to figure out what itent is/does
                        onActivityResult(REQUEST_CODE,Activity.RESULT_OK,AuthorizationClient.createLoginActivityIntent(LoginActivity.this,request));
                        //change code inside onActivityResult to grab playlists.
                //useful links: https://developer.spotify.com/documentation/android/guides/android-authentication/ , https://github.com/spotify/android-auth/blob/master/auth-lib/src/main/java/com/spotify/sdk/android/auth/AuthorizationClient.java , https://developer.spotify.com/documentation/android/quick-start/ ( kinda trash ) , https://spotify.github.io/android-sdk/auth-lib/docs/com/spotify/sdk/android/auth/AuthorizationClient.html
                }
                setResult(Activity.RESULT_OK);// maybe change to LoginActivity.this but probably fine

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        // We will start writing our code here.
    }

    private void connected() {
        // Then we will write some more code here.
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Aaand we will finish off here.
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    System.out.println(response.getAccessToken());
                    System.out.println("inside token");
                    // add part where we get all their playlists here? and add option to specifically set playlist uri
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
}