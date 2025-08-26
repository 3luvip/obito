/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.girlkun.Manager;

import com.girlkun.jdbc.daos.PlayerDAO;
import com.girlkun.server.Client;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author MIT CHIKEN AN Trom
 */
public class Manager {

    private static Manager instance = null;

    private Manager() {
        compositeDisposable = new CompositeDisposable();
    }

    // Static method
    // Static method to create instance of Singleton class
    public static synchronized Manager getInstance() {
        if (instance == null) {
            instance = new Manager();
        }
        return instance;
    }

    private CompositeDisposable compositeDisposable;

    public void autoSave() {
        System.out.println("[AutoSaveManager] start autosave");
        Disposable subscribe = Observable.interval(60, 90, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribe(i -> {
                    this.handleAutoSave();
                }, throwable -> {
                    System.out.println("[AutoSaveManager] start autosave error: " + throwable.getLocalizedMessage());
                });
        compositeDisposable.add(subscribe);
    }

    private void handleAutoSave() {
        System.out.println("Tự động lưu dữ liệu của <" + Client.gI().players.size() + "> người chơi");
        Client.gI().getPlayers().forEach(player -> {

            PlayerDAO.updatePlayer(player);
        });
    }

    private void dispose() {
        compositeDisposable.dispose();
        compositeDisposable = null;
    }
}
