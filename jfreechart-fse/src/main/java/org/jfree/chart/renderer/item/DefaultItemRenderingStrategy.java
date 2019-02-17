package org.jfree.chart.renderer.item;

import java.io.Serializable;

import org.jfree.chart.renderer.AbstractRenderer;

/*
 * if necessary this can be improved with respect to speed by implementing the
 * default cases direct in the abstract renderer.
 */

/**
 * A default item rendering strategy defines certain properties e.g. Shape, Paint, ... for individual items.
 * It should be used with a specific renderer because it is based on its Shape, Paint, ... per Series properties.
 * 
 * @author zinsmaie
 */
public abstract class DefaultItemRenderingStrategy implements Serializable {

    /** generated serial id     */
    private static final long serialVersionUID = -3637783770791118009L;
    
    /** the renderer that uses the strategy. This is necessary to get access to the
     *  get Property Series methods etc. */
    protected final AbstractRenderer renderer;

    /**
     * creates a new Rendering strategy for the submitted renderer.
     * @param renderer
     */
    public DefaultItemRenderingStrategy(AbstractRenderer renderer) {
        this.renderer = renderer;
    }
    
}
