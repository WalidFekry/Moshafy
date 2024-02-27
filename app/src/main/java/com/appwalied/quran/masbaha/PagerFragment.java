package com.appwalied.quran.masbaha;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appwalied.quran.R;

public class PagerFragment extends Fragment {   int position;
    int layout;

    FrameLayout colord_frame;

    public PagerFragment(int position, int layout) {
        this.position = position;
        this.layout = layout;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_masbaha, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = getView().findViewById(R.id.counter);
        colord_frame = getView().findViewById(R.id.colord_frame);
        if (layout == 1)
            colord_frame.setBackgroundResource(R.drawable.bk_round_item);
        else if (layout == 2)
            colord_frame.setBackgroundResource(R.drawable.bk_round_item2);
        else if (layout == 3)
            colord_frame.setBackgroundResource(R.drawable.bk_round_item3);
        else if (layout == 4)
            colord_frame.setBackgroundResource(R.drawable.bk_round_item4);
        else if (layout == 5)
            colord_frame.setBackgroundResource(R.drawable.bk_round_item5);

        textView.setText(String.valueOf(position + 1));
        colord_frame.invalidate();
    }
}
