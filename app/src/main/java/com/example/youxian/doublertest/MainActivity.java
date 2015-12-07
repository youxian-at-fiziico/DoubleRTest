package com.example.youxian.doublertest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends Activity implements Callback<StackOverflowQuestions> {
    private static final String TAG = MainActivity.class.getName();

    private Button mLoadButton;
    private ListView mListView;
    private TextView mText;
    private List<Question> mQuestions;
    private QuestionAdapter mQuestionAdapter;
    private Call<StackOverflowQuestions> mCall;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_main);
        mQuestions = new ArrayList<>();
        mQuestionAdapter = new QuestionAdapter(mQuestions);
        mListView.setAdapter(mQuestionAdapter);
        mLoadButton = (Button) findViewById(R.id.load_button_main);
        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadQuestions();
            }
        });
        mText = (TextView) findViewById(R.id.title_text_main);
    }

    private void loadQuestions() {
        new LoadQuestionsTask().execute();
    }


    @Override
    public void onResponse(Response<StackOverflowQuestions> response, Retrofit retrofit) {
        mQuestions.clear();
        mQuestions.addAll(response.body().items);
        mText.setText("Get new post about android: ");
        mQuestionAdapter.notifyDataSetChanged();
        Log.d(TAG, "size: " + mQuestions.size());
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private class QuestionAdapter extends BaseAdapter {
        private List<Question> questions;
        public QuestionAdapter(List<Question> list) {
            questions = list;
        }
        @Override
        public int getCount() {
            return questions.size();
        }

        @Override
        public Object getItem(int position) {
            return questions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Question question = questions.get(position);
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.listrow_item, null);
                ViewHolder tag = new ViewHolder();
                tag.title = (TextView) convertView.findViewById(R.id.title_text_item);
                convertView.setTag(tag);
            }

            ViewHolder tag = (ViewHolder) convertView.getTag();

            if (question != null) {
                tag.title.setText(question.toString());
            }

            return convertView;
        }
    }

    private static class ViewHolder {
        TextView title;
    }

    private class LoadQuestionsTask extends AsyncTask<String , Void, Void> {

        @Override
        protected Void doInBackground(String ... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.stackexchange.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // prepare call in Retrofit 2.0
            StackOverflowAPI stackOverflowAPI = retrofit.create(StackOverflowAPI.class);

            mCall = stackOverflowAPI.loadQuestions("android");
            //asynchronous call
            mCall.enqueue(MainActivity.this);



            // synchronous call would be with execute, in this case you
            // would have to perform this outside the main thread
            // call.execute()

            // to cancel a running request
            // call.cancel();
            // calls can only be used once but you can easily clone them
            //Call<StackOverflowQuestions> c = call.clone();
            //c.enqueue(this);
            return null;
        }
    }
}
