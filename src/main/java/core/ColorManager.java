package core;

import lombok.extern.slf4j.Slf4j;
import org.rtv.Options;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Singleton
public class ColorManager {
    private int min = 0;
    private int max = 255;
    private ThreadLocalRandom threadLocalRandom;

    @Inject
    public ColorManager() {
        this.threadLocalRandom = ThreadLocalRandom.current();
    }

    public Color getColor(){
        return new Color(getRandR(), getRandG(), getRandB());
    }

    public Color getColor(String eventName){
        return Options.getInstance().getColor(eventName);
    }

    public int getRandR() { return this.threadLocalRandom.nextInt(min,max); }
    public int getRandG() { return this.threadLocalRandom.nextInt(min,max); }
    public int getRandB() {
        return this.threadLocalRandom.nextInt(min,max);
    }


}
