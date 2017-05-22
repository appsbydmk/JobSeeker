package com.appsbydmk.jobseeker.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appsbydmk.jobseeker.R;
import com.appsbydmk.jobseeker.helpers.HelperConstants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class JobSearchActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etJobSearch;
    private Button btnJobSearch;
    private TextView tvSearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);
        this.bindViews();
        btnJobSearch.setOnClickListener(this);
    }

    private void bindViews() {
        etJobSearch = (EditText) this.findViewById(R.id.et_job_search);
        btnJobSearch = (Button) this.findViewById(R.id.btn_job_search);
        tvSearchResult = (TextView) this.findViewById(R.id.tv_job_search);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_job_search:
                new JobSearchTask().execute();
                break;
            default:
                break;
        }
    }

    class JobSearchTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(JobSearchActivity.this);
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
                url = new URL(HelperConstants.JOB_SEARCH_URL);
                connection = url.openConnection();
                String searchString = getSearchString();
                connection.setDoOutput(true);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                bufferedWriter.write(searchString.toLowerCase());
                bufferedWriter.flush();
                String result = "";
                String line;
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    result += line + "\n";
                }
                String finalResultArray[] = result.split("\n");
                final String finalResult = "Date: " + finalResultArray[0] + "\n" + "Role: " +
                        finalResultArray[1] + "\n"
                        + "Job Description: " + finalResultArray[2];
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvSearchResult.setText(finalResult);
                    }
                });
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
        }
    }

    private String getSearchString() {
        return etJobSearch.getText().toString();
    }
}
