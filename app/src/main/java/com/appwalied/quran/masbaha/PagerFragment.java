package com.appwalied.quran.masbaha;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appwalied.quran.R;

public class PagerFragment extends Fragment {

    private int position;
    private int layout;

    private FrameLayout colord_frame;

    public PagerFragment() {
        // Required empty public constructor
    }

    public static PagerFragment newInstance(int position, int layout) {
        PagerFragment fragment = new PagerFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putInt("layout", layout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            position = getArguments().getInt("position");
            layout = getArguments().getInt("layout");
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.item_masbaha, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = view.findViewById(R.id.counter);
        colord_frame = view.findViewById(R.id.colord_frame);

        switch (layout) {
            case 1:
                colord_frame.setBackgroundResource(R.drawable.bk_round_item);
                break;
            case 2:
                colord_frame.setBackgroundResource(R.drawable.bk_round_item2);
                break;
            case 3:
                colord_frame.setBackgroundResource(R.drawable.bk_round_item3);
                break;
            case 4:
                colord_frame.setBackgroundResource(R.drawable.bk_round_item4);
                break;
            case 5:
                colord_frame.setBackgroundResource(R.drawable.bk_round_item5);
                break;
        }

        textView.setText(String.valueOf(position + 1));
    }
}
