package com.example.mineskills.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ProgressBar {
    private static final int BAR_LENGTH = 20;

    public static Component create(int current, int max, NamedTextColor color) {
        double percentage = Math.min(1.0, (double) current / max);
        int filled = (int) (percentage * BAR_LENGTH);
        int empty = BAR_LENGTH - filled;

        Component bar = Component.text("[").color(NamedTextColor.DARK_GRAY)
            .append(Component.text("█".repeat(filled)).color(color))
            .append(Component.text("░".repeat(empty)).color(NamedTextColor.GRAY))
            .append(Component.text("] ").color(NamedTextColor.DARK_GRAY))
            .append(Component.text(current + "/" + max).color(NamedTextColor.WHITE));

        return bar.decoration(TextDecoration.ITALIC, false);
    }

    public static Component createColored(int current, int max, NamedTextColor lowColor, NamedTextColor mediumColor, NamedTextColor highColor) {
        double percentage = Math.min(1.0, (double) current / max);
        NamedTextColor color = percentage < 0.33 ? lowColor : (percentage < 0.66 ? mediumColor : highColor);
        return create(current, max, color);
    }
}
