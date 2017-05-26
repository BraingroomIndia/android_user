package com.braingroom.user.view.activity;

/**
 * Created by godara on 11/05/17.
 */

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.braingroom.user.R;
import com.braingroom.user.view.adapters.FAQAdapter;
import com.braingroom.user.view.adapters.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    private FAQAdapter mAdapter;

    @NonNull
    public static Intent newIntent(Context context) {
        return new Intent(context, FAQActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);


        Question q1 = new Question(getResources().getString(R.string.q1), getResources().getString(R.string.a1));
        Question q2 = new Question(getResources().getString(R.string.q2), getResources().getString(R.string.a2));
        Question q3 = new Question(getResources().getString(R.string.q3), getResources().getString(R.string.a3));
        Question q4 = new Question(getResources().getString(R.string.q4), getResources().getString(R.string.a4));
        Question q5 = new Question(getResources().getString(R.string.q5), getResources().getString(R.string.a5));
        Question q6 = new Question(getResources().getString(R.string.q6), getResources().getString(R.string.a6));
        Question q7 = new Question(getResources().getString(R.string.q7), getResources().getString(R.string.a7));
        Question q8 = new Question(getResources().getString(R.string.q8), getResources().getString(R.string.a8));
        Question q9 = new Question(getResources().getString(R.string.q9), getResources().getString(R.string.a9));
        Question q10 = new Question(getResources().getString(R.string.q10), getResources().getString(R.string.a10));
        Question q11 = new Question(getResources().getString(R.string.q11), getResources().getString(R.string.a11));
        Question q12 = new Question(getResources().getString(R.string.q12), getResources().getString(R.string.a12));
        final List<Question> questions = Arrays.asList(q1, q2, q3, q4, q5, q6, q7, q8, q9, q10, q11, q12);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new FAQAdapter(this, questions);
        mAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @UiThread
            @Override
            public void onParentExpanded(int parentPosition) {
//                Question expandedRecipe = questions.get(parentPosition);
            }

            @UiThread
            @Override
            public void onParentCollapsed(int parentPosition) {
//                Recipe collapsedRecipe = recipes.get(parentPosition);
            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
