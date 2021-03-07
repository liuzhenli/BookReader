package com.liuzhenli.common.base.rxlife;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.reactivex.subjects.BehaviorSubject;


/**
 * @author Liuzhenli
 * @since 2019-07-06 17:08
 */
public class RxFragment extends Fragment   {
    BehaviorSubject<LifeEvent> lifeSubject = BehaviorSubject.create();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lifeSubject.onNext(LifeEvent.ATTACH);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifeSubject.onNext(LifeEvent.CREATE_VIEW);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifeSubject.onNext(LifeEvent.CREATE);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifeSubject.onNext(LifeEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifeSubject.onNext(LifeEvent.RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        lifeSubject.onNext(LifeEvent.PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        lifeSubject.onNext(LifeEvent.STOP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifeSubject.onNext(LifeEvent.DESTROY_VIEW);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifeSubject.onNext(LifeEvent.DESTROY);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lifeSubject.onNext(LifeEvent.DETACH);
    }
}
