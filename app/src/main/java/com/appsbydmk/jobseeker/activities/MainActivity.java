package com.appsbydmk.jobseeker.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appsbydmk.jobseeker.R;
import com.appsbydmk.jobseeker.helpers.HelperConstants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.bindViews();
    }

    private void bindViews() {
        etUsername = (EditText) this.findViewById(R.id.et_username);
        etPassword = (EditText) this.findViewById(R.id.et_password);
        btnLogin = (Button) this.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                new UserLoginTask().execute();
                break;
            default:
                break;
        }
    }

    @NonNull
    private String getUsername() {
        return etUsername.getText().toString();
    }

    @NonNull
    private String getPassword() {
        return etPassword.getText().toString();
    }

    class UserLoginTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Connecting.....");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;
            URL url;
            URLConnection connection;
            try {
                url = new URL(HelperConstants.USER_CONNECTION_URL);
                connection = url.openConnection();
                String username = getUsername();
                String password = getPassword();
                connection.setDoOutput(true);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                bufferedWriter.write(username + "\n");
                bufferedWriter.write(password);
                bufferedWriter.flush();

                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                line = bufferedReader.readLine();
                if (line.equals("success")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                            Intent jobIntent = new Intent(MainActivity.this, JobSearchActivity.class);
                            startActivity(jobIntent);
                            finish();
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (bufferedWriter != null)
                        bufferedWriter.close();
                    if (bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            Toast.makeText(getBaseContext(), "Logged in successfully!", Toast.LENGTH_SHORT).show();
        }

    }
}
