package com.braingroom.user.view.adapters;

/**
 * Created by godara on 11/05/17.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.braingroom.user.R;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import java.util.List;

public class FAQAdapter extends ExpandableRecyclerAdapter<Question, String, FAQAdapter.QuestionViewHolder, FAQAdapter.AnswerViewHolder> {

    private LayoutInflater mInflater;

    public FAQAdapter(Context context, @NonNull List<Question> questionList) {
        super(questionList);
        mInflater = LayoutInflater.from(context);
    }

    // onCreate ...
    @Override
    public QuestionViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View questionView = mInflater.inflate(R.layout.question_view, parentViewGroup, false);
        return new QuestionViewHolder(questionView);
    }

    @Override
    public AnswerViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View answerView = mInflater.inflate(R.layout.answer_view, childViewGroup, false);
        return new AnswerViewHolder(answerView);
    }

    // onBind ...
    @Override
    public void onBindParentViewHolder(@NonNull QuestionViewHolder recipeViewHolder, int parentPosition, @NonNull Question question) {
        recipeViewHolder.bind(question);
    }

    @Override
    public void onBindChildViewHolder(@NonNull AnswerViewHolder answerViewHolder, int parentPosition, int childPosition, @NonNull String answer) {
        answerViewHolder.bind(answer);
    }

    public class QuestionViewHolder extends ParentViewHolder {

        private TextView mQuestionTextView;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            mQuestionTextView = (TextView) itemView.findViewById(R.id.question_textview);
        }

        public void bind(Question question) {
            mQuestionTextView.setText(question.getText());
        }
    }

    public class AnswerViewHolder extends ChildViewHolder {

        private TextView mAnswerTextView;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            mAnswerTextView = (TextView) itemView.findViewById(R.id.answer_textview);
        }

        public void bind(String answer) {
            mAnswerTextView.setText(answer);
        }
    }
}