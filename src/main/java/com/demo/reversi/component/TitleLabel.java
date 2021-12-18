package com.demo.reversi.component;

import com.demo.reversi.themes.Theme;
import javafx.scene.control.Label;

public class TitleLabel extends Label {
    public static final double FONT_SIZE = 25;
    public static final double PREFERRED_HEIGHT = 50;

    public TitleLabel(String title, Theme theme) {
        super(title);

        theme.bindToTitleFontFamily(fontProperty());
        theme.bindToTitleFontPaint(textFillProperty());

        setMinHeight(PREFERRED_HEIGHT);
        setPrefHeight(PREFERRED_HEIGHT);
    }
}