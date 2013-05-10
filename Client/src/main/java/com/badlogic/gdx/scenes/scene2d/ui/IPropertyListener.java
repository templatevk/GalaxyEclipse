package com.badlogic.gdx.scenes.scene2d.ui;

/**
 * Created with IntelliJ IDEA.
 * User: Kuznetsov
 * Date: 10.05.13
 * Time: 12:25
 * To change this template use File | Settings | File Templates.
 */
public interface IPropertyListener<T> {
    void onPropertyChanged(T newValue);
}