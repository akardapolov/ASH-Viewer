/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2013, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ----------------------------
 * DatasetExtensionManager.java
 * ----------------------------
 * (C) Copyright 2013, by Michael Zinsmaier.
 *
 * Original Author:  Michael Zinsmaier;
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 17-Sep-2013 : Version 1 (MZ);
 *
 */

package org.jfree.data.extension.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jfree.data.extension.DatasetExtension;
import org.jfree.data.general.Dataset;

/**
 * Allows the handling of separate {@link DatasetExtension}. Pairs a dataset 
 * and a DatasetExtension together and provides unified access to 
 * DatasetExtensions regardless of their implementation (in a dataset or 
 * separate).
 * 
 * @author zinsmaie
 */
public class DatasetExtensionManager implements Serializable {

    /** a generated serial id */
    private static final long serialVersionUID = 3727659792806462637L;
    
    /**
     * all separate extensions have to be registered here
     */
    private HashMap<Dataset, List<DatasetExtension>> registeredExtensions 
            = new HashMap<Dataset, List<DatasetExtension>>(); 
    
    /**
     * Registers a separate dataset extension at the extension manager (the 
     * extension is automatically paired with its dataset).
     * 
     * @param extension 
     */
    public void registerDatasetExtension(DatasetExtension extension) {
        List<DatasetExtension> extensionList = registeredExtensions.get(
                extension.getDataset()); 
        if (extensionList != null) {
            extensionList.add(extension);
        } else {
            extensionList = new LinkedList<DatasetExtension>();
            extensionList.add(extension);
            registeredExtensions.put(extension.getDataset(), extensionList);
        }
    }

    /**
     * @param dataset 
     * @param interfaceClass
     * @return true if a.) the dataset implements the interface class or b.) a 
     *     separate object that implements the interface for the dataset has 
     *     been registered. a is always checked before b
     */
    public boolean supports(Dataset dataset, Class<? extends DatasetExtension> 
            interfaceClass) {
        return getExtension(dataset, interfaceClass) != null;
    }
    
    /**
     * @param dataset
     * @param interfaceClass
     * @return the implementation of the interfaceClass for the specified 
     *     dataset or null if no supporting implementation could be found i.e. 
     *     the dataset does not implement the interface itself and there no 
     *     separate implementation has been registered for the dataset
     */
    public <T extends DatasetExtension> T getExtension(Dataset dataset, 
            Class<T> interfaceClass) {        
        if (interfaceClass.isAssignableFrom(dataset.getClass())) {
            //the dataset supports the interface
            return interfaceClass.cast(dataset);
        } else {
            List<DatasetExtension> extensionList 
                    = registeredExtensions.get(dataset);
            if (extensionList != null) {
                for (DatasetExtension extension : extensionList) {
                    if (interfaceClass.isAssignableFrom(extension.getClass())) {
                        //the dataset does not support the extension but
                        //a matching helper object is registered for the dataset
                        return interfaceClass.cast(extension);
                    }
                }
            }
        }
                
        return null;        
    }


}
