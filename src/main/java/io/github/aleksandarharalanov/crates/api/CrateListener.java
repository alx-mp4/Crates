package io.github.aleksandarharalanov.crates.api;

import io.github.aleksandarharalanov.crates.api.event.CrateOpenEndEvent;
import io.github.aleksandarharalanov.crates.api.event.CrateOpenStartEvent;
import io.github.aleksandarharalanov.crates.api.event.CrateRewardChosenEvent;
import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class CrateListener extends CustomEventListener implements Listener {

    public CrateListener() {}

    public void onCrateOpenEnd(CrateOpenEndEvent event) {}

    public void onCrateOpenStart(CrateOpenStartEvent event) {}

    public void onCrateRewardChosen(CrateRewardChosenEvent event) {}

    @Override
    public void onCustomEvent(Event event) {
        if (event instanceof CrateOpenEndEvent) {
            this.onCrateOpenEnd((CrateOpenEndEvent) event);
        } else if (event instanceof CrateOpenStartEvent) {
            this.onCrateOpenStart((CrateOpenStartEvent) event);
        } else if (event instanceof CrateRewardChosenEvent) {
            this.onCrateRewardChosen((CrateRewardChosenEvent) event);
        }
    }
}
