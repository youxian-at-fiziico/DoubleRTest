package com.example.youxian.doublertest.MVP;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.youxian.doublertest.R;



/**
 * Created by youxian on 12/31/15.
 */
public class StackOverflowActivity extends Activity {

    private Button mLoadButton;
    private ListView mListView;
    private StackOverflowPresenter mStackOverflowPresenter;
    private StackOverflowData mStackOverflowData;
    private StackOverflowAdapter mStackOverflowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stackoverflow);
        mStackOverflowPresenter = new StackOverflowPresenter(this);
        initView();
    }

    private void initView() {
        mLoadButton = (Button) findViewById(R.id.load_button_stackoverflow);
        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStackOverflowPresenter.fetchData();
            }
        });
        mListView = (ListView) findViewById(R.id.list_stackoverflow);
    }

    public void displayData(StackOverflowData stackOverflowData) {
        mStackOverflowData = stackOverflowData;
        if (mStackOverflowAdapter == null) {
            mStackOverflowAdapter = new StackOverflowAdapter();
            mListView.setAdapter(mStackOverflowAdapter);
        } else {
            mStackOverflowAdapter.notifyDataSetChanged();
        }

    }

    private class StackOverflowAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mStackOverflowData.items.size();
        }

        @Override
        public Object getItem(int position) {
            return mStackOverflowData.items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.listrow_item, null);
                ViewHolder tag = new ViewHolder();
                tag.title = (TextView) convertView.findViewById(R.id.title_text_item);
                convertView.setTag(tag);
            }

            ViewHolder tag = (ViewHolder) convertView.getTag();

            if (mStackOverflowData.items.get(position) != null) {
                tag.title.setText(mStackOverflowData.items.get(position).title);
            }

            return convertView;
        }
    }

    private static class ViewHolder {
        TextView title;
    }

}
