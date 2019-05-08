package core;

public enum CurrrentState {
    START,
    STOP;
    /**
     * Transitions the state to the next one.
     * Note: not all states have the default next state;
     */
    CurrrentState next() {
        switch (this) {
            case START: return STOP;
            case STOP: return START;
            default: return null;
        }
    }
}
