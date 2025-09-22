package org.example.testfx2.utils;

import javafx.scene.control.Button;

public class ModernButton extends Button {
    public ModernButton(String text) {
        super(text);
        this.getStyleClass().add("button-modern");
    }
}
