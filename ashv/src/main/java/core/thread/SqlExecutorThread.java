package core.thread;

import core.CurrrentState;
import core.StateMachine;
import core.StateTransitionListener;
import core.processing.GetFromRemoteAndStore;
import lombok.extern.slf4j.Slf4j;
import store.OlapCacheManager;
import store.StoreManager;

import javax.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SqlExecutorThread implements StateTransitionListener {

    private StateMachine stateMachine;
    private StoreManager storeManager;
    private GetFromRemoteAndStore getFromRemoteAndStore;
    private OlapCacheManager olapCacheManager;

    private ScheduledExecutorService service;

    private boolean isRunning;

    @Inject
    public SqlExecutorThread(StateMachine stateMachine,
                             GetFromRemoteAndStore getFromRemoteAndStore,
                             StoreManager storeManager,
                             OlapCacheManager olapCacheManager){
        this.stateMachine = stateMachine;
        this.getFromRemoteAndStore = getFromRemoteAndStore;
        this.storeManager = storeManager;
        this.olapCacheManager = olapCacheManager;

        this.isRunning = true;

        this.stateMachine.addTransitionListener(this);
    }

    public void startIt(){
        service = Executors.newSingleThreadScheduledExecutor();
        service.submit(taskThatFinishesEarlyOnInterruption());
    }

    public void stopIt(){
        try {
            service.shutdownNow();
            service.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            log.error("InterruptedException while stopping service", ie);
        }
    }

    private Runnable taskThatFinishesEarlyOnInterruption() {
        return () -> {
            while (isRunning) {

                getFromRemoteAndStore.loadDataFromRemoteToLocalStore();

                try {
                    Thread.sleep(this.olapCacheManager.getIProfile().getInterval());
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                    break;
                }
            }
        };
    }

    @Override
    public void transitionTo(CurrrentState state) { }

}
