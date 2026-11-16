package com.libGdx.test.livedata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.example.game.ScoreViewModel;
import com.example.mvvm.Observer;
import com.example.mvvm.ViewModel;
import com.example.mvvm.ViewModelProvider;
import com.example.mvvm.ViewModelStore;
import com.libGdx.test.action.ActionDemo;
import com.libGdx.test.base.LibGdxTestMain;

import java.util.concurrent.Executor;

public class LiveDataApp extends LibGdxTestMain {
    private ViewModelStore viewModelStore;
    private ViewModelProvider viewModelProvider;
    private ScoreViewModel scoreViewModel;
//    private final Observer<Integer> scoreObserver =value -> System.out.println("当前分数：" + value);

    public static void main(String[] args) {
        LiveDataApp demo = new LiveDataApp();
        demo.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        viewModelStore = new ViewModelStore();
        // LibGDX 主线程执行器
        Executor uiExecutor = runnable -> Gdx.app.postRunnable(runnable);
        // 自定义创建工厂
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
                    @Override
                    public <T extends ViewModel> T create(Class<T> modelClass) {
                        if (modelClass == ScoreViewModel.class) {
                            return modelClass.cast(new ScoreViewModel(uiExecutor));
                        }
                        throw new IllegalArgumentException("Unknown ViewModel: " + modelClass);
                    }
                };
        viewModelProvider = new ViewModelProvider(viewModelStore, factory);
        scoreViewModel = getScoreViewModel();
        scoreViewModel.getScore().observe((integer)-> System.out.println("当前分数：" + integer));

        scoreViewModel.addScore(1);
    }

    public ScoreViewModel getScoreViewModel() {
        return viewModelProvider.get(ScoreViewModel.class);
    }

    @Override
    public void dispose() {
        // 游戏结束，清理所有 ViewModel
        viewModelStore.clear();

        super.dispose();
    }
}
