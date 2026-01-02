package com.example.mineskills.models;

public record ActionProgress(
    int combat,
    int mining,
    int movement
) {
    public ActionProgress withCombat(int value) {
        return new ActionProgress(value, mining, movement);
    }

    public ActionProgress withMining(int value) {
        return new ActionProgress(combat, value, movement);
    }

    public ActionProgress withMovement(int value) {
        return new ActionProgress(combat, mining, value);
    }

    public static ActionProgress empty() {
        return new ActionProgress(0, 0, 0);
    }
}
