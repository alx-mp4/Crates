package io.github.aleksandarharalanov.crates.api;

import io.github.aleksandarharalanov.crates.api.event.CrateOpenEvent;
import io.github.aleksandarharalanov.crates.api.event.CrateRewardEvent;
import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

/**
 * <b>Bridges Bukkit 1060 CUSTOM_EVENT to typed CrateListener callbacks.</b>
 * <p>Required for: {@code pm.registerEvent(Type.CUSTOM_EVENT, new CratesEventBridge(...), ...)}.</p>
 */
public final class CratesEventBridge extends CustomEventListener {

    private final CrateListener delegate;

    public CratesEventBridge(CrateListener delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onCustomEvent(Event event) {
        if (event instanceof CrateOpenEvent) {
            delegate.onCrateOpen((CrateOpenEvent) event);
        } else if (event instanceof CrateRewardEvent) {
            delegate.onCrateReward((CrateRewardEvent) event);
        }
    }
}
