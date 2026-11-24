package com.joker.mvvm;

import java.util.concurrent.Executor;

public final class ScoreViewModel extends ViewModel {
    private final MutableLiveData<Integer> score;

    public ScoreViewModel(Executor uiExecutor) {
        score = new MutableLiveData<>(0, uiExecutor);
    }

    public MutableLiveData<Integer> getScore() {
        return score;
    }

    public void addScore(int amount) {
        Integer current = score.getValue();
        score.setValue((current == null ? 0 : current) + amount);
    }

    @Override
    protected void onCleared() {
        score.clearObservers();
    }
}
