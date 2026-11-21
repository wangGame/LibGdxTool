package com.example.game;

import junit.framework.TestCase;

import java.util.concurrent.Executor;

public class ScoreViewModelTest extends TestCase {
    public void testAddScore() {
        Executor uiExecutor = runnable -> {};
        ScoreViewModel scoreViewModel = new ScoreViewModel(uiExecutor);
        scoreViewModel.addScore(10);
        assertEquals(Integer.valueOf(10), scoreViewModel.getScore().getValue());
        scoreViewModel.addScore(5);
        assertEquals(Integer.valueOf(15), scoreViewModel.getScore().getValue());
    }

}