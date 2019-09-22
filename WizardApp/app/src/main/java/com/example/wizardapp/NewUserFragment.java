package com.example.wizardapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NewUserFragment extends Fragment {

    Button next;
    WizardFragmentInterface wizardFragmentInterface;


    public NewUserFragment() {
        // Required empty public constructor
    }
    public NewUserFragment(WizardFragmentInterface wizardFragmentInterface) {
        this.wizardFragmentInterface = wizardFragmentInterface;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_new_user, container, false);

        next = rootView.findViewById(R.id.save_user);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wizardFragmentInterface.goNext();
            }
        });

        return rootView;
    }
}
