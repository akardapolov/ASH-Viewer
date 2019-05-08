package core;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Singleton
public class StateMachine {

    private volatile CurrrentState state = CurrrentState.STOP;

    public CurrrentState getState() {
        return state;
    }

    private ReentrantReadWriteLock listenersLock = new ReentrantReadWriteLock();
    private List<StateTransitionListener> transitionListeners = new ArrayList<StateTransitionListener>();

    @Inject
    public StateMachine(){}

    /**
     * Registers state transition listener.
     */
    public void addTransitionListener(StateTransitionListener listener) {
        try {
            listenersLock.writeLock().lock();
            transitionListeners.add(listener);
        }
        finally {
            listenersLock.writeLock().unlock();
        }
    }

    /**
     * Unregisters the listener
     */
    public void removeTransitionListener(StateTransitionListener listener) {
        try {
            listenersLock.writeLock().lock();
            transitionListeners.remove(listener);
        }
        finally {
            listenersLock.writeLock().unlock();
        }
    }

    public void transitionToNext() {
        transitionTo(state.next());
    }

    /**
     * Transitions to the specified state, notifying all listeners.
     * Note: this method is intentionally not public, use specific methods to make desired transitions.
     */
    void transitionTo(CurrrentState newState) {
        if (state != newState) {
            state = newState;
            notifyAboutTransition();
        }
    }

    protected void notifyAboutTransition() {
        try {
            listenersLock.readLock().lock();
            for (StateTransitionListener listener : transitionListeners) {
                listener.transitionTo(state);
            }
        }
        finally {
            listenersLock.readLock().unlock();
        }
    }

    public void startScanning() {
        if (state == CurrrentState.START) {
            transitionTo(CurrrentState.START);
        }
        else {
            throw new IllegalStateException("Attempt to go scanning from " + state);
        }
    }

    /**
     * Transitions to the stopping state
     */
    public void stopScanning() {
        if (state == CurrrentState.STOP) {
            transitionTo(CurrrentState.STOP);
        }
        else {
            throw new IllegalStateException("Attempt to stopScanning from " + state);
        }
    }

}
