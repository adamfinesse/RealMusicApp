package com.example.spotifyplaylist.ui.login;

//import static com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotifyplaylist.HelpActivity;
import com.example.spotifyplaylist.R;
import com.example.spotifyplaylist.SuccessActivity;
import com.example.spotifyplaylist.databinding.ActivityLoginBinding;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.app.SpotifyAuthHandler;
import com.spotify.sdk.android.auth.AuthorizationResponse;
import com.spotify.sdk.android.auth.AuthorizationClient;
//import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
//import com.spotify.protocol.client.Subscription;
//import com.spotify.protocol.types.PlayerState;
//import com.spotify.protocol.types.Track;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private static final String CLIENT_ID = "e30929e731664b3f86b922d87115dc59";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";
    private static final int REQUEST_CODE = 1337;
    private Intent a;
    //private int REQUEST_CODE;
    String token = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
//        final String CLIENT_ID = binding.username.toString();
//        final String REDIRECT_URI = binding.password.toString();

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
                    //updateUiWithUser(loginResult.getSuccess());
                    //Intent intent = new Intent(getApplicationContext(),LoginActivity.class); example of intent
                    //REQUEST_CODE = Integer.parseInt(binding.editTextNumber.getText().toString());
//                    System.out.println(CLIENT_ID);
//                    System.out.println(REDIRECT_URI);
//                    System.out.println(REQUEST_CODE);
                    Log.d("random","yamuv");
                    final AuthorizationRequest request = new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
                            .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming","app-remote-control"})
                            //.setShowDialog(true)
                            .build();
                        //request.toUri(); testing random code
                    AuthorizationClient.openLoginActivity(LoginActivity.this, REQUEST_CODE, request); // This should try to authenticate with the 3 params, and by default if spotify isnt on the device it goes to web sign in.
                    a=AuthorizationClient.createLoginActivityIntent(LoginActivity.this,request);//.getExtras() might be useful for response idk

                    Bundle t2 = new Bundle();
                    t2.get("response");
                    for(String a : t2.keySet()){
                        Log.d("keys",a);
                    }
                    //AuthorizationResponse ad = new AuthorizationResponse(t2.keySet());
                    //String test2 = ad.getAccessToken();
                    //String test2 = AuthorizationResponse.fromUri(request.toUri()).getAccessToken();
                    //Log.d("requestinfo",test2);
                    // need to figure out what itent is/does
                    //a = AuthorizationClient.
                        //change code inside onActivityResult to grab playlists.
                //useful links: https://developer.spotify.com/documentation/android/guides/android-authentication/ , https://github.com/spotify/android-auth/blob/master/auth-lib/src/main/java/com/spotify/sdk/android/auth/AuthorizationClient.java , https://developer.spotify.com/documentation/android/quick-start/ ( kinda trash ) , https://spotify.github.io/android-sdk/auth-lib/docs/com/spotify/sdk/android/auth/AuthorizationClient.html
                //example response url with access token from spotify: http://localhost:8888/callback#access_token=BQDhDe3JdIbku-Sh28C_PUhNTm5WDhuyH32zU2-o_6txOeTlQl3r8DHxo6QwDztBJcACTqlu0EouougSrCkxrFBtNLOPsXk1slRVO_7RwNnoRrmthJim5cvAhCiYGPqsmF5Q7WsvZnAZo-gnZP3ZjN_-NzqritoEnZjfUzg6_4GR67B7B-NbMI_c4zjtc_ctbq0&token_type=Bearer&expires_in=3600
                //
                }
                //onActivityResult(REQUEST_CODE,Activity.RESULT_OK,AuthorizationClient.createLoginActivityIntent(LoginActivity.this,request));
                setResult(Activity.RESULT_OK);
                //onActivityResult(REQUEST_CODE,Activity.RESULT_OK,a);
                //Complete and destroy login activity once successful
                finish();
                onActivityResult(REQUEST_CODE,Activity.RESULT_OK,a);
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
        //Intent success = new Intent(this, SuccessActivity.class);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

                //startActivity(success);
                //Log.d("logged in","entered logged in page");
            }
        });
    }
    public void helpMenu(View v){
        //Button a = (Button)v;
        Intent i = new Intent(this, HelpActivity.class);
        startActivity(i);
        Log.d("helpmenu","help menu button clicked");
    }
    public void loggedIn(View v){
        Intent i = new Intent(this, SuccessActivity.class);
        startActivity(i);
        Log.d("logged in","entered logged in page");
    }
    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        // We will start writing our code here.
//    }

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
            System.out.println(response.getType().toString() +"ya");
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    System.out.println(response.getAccessToken());
                    System.out.println("inside token");


                   // response.
                    //write requests here?
                    // add part where we get all their playlists here? and add option to specifically set playlist uri
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    System.out.println("inside error");
                    break;
                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
                    // for some reason it currently goes here with no access token I think
                    System.out.println("inside default");
                    System.out.println(response.getAccessToken());
                    Bundle t2 = new Bundle();
                    t2.get("response");
                    String test2 = response.getAccessToken();
                    Log.d("requestinfo",test2);
                    // the response is saved in a bundle called EXTRA with the key response??
                    //Bundle bundle = getIntent().getExtras();
                    //Bundle d =
                    //System.out.println(.get("response").toString());

            }
        }
    }
}