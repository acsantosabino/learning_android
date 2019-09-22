package com.example.wizardapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class NewBookFragment extends Fragment {

    Button next;
    WizardFragmentInterface wizardFragmentInterface;


    public NewBookFragment() {
        // Required empty public constructor
    }
    public NewBookFragment(WizardFragmentInterface wizardFragmentInterface) {
        this.wizardFragmentInterface = wizardFragmentInterface;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_new_book, container, false);

        next = rootView.findViewById(R.id.save_book);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wizardFragmentInterface.goNext();
            }
        });

        return rootView;
    }
}
