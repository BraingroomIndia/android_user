package com.braingroom.user.view.adapters;

/**
 * Created by godara on 11/05/17.
 */

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


public class Question implements Parent<String> {

    @Getter
    @Setter
    private String text;
    private List<String> mAnswers = new ArrayList<>();

    public Question(String name,String answer) {
        this.text = name;
        mAnswers.add(answer);
    }

    @Override
    public List<String> getChildList() {
        return mAnswers;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}