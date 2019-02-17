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
 * ---------------------
 * ObjectUtilitiess.java
 * ---------------------
 * (C) Copyright 2003-2013, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 25-Mar-2003 : Version 1 (DG);
 * 15-Sep-2003 : Fixed bug in clone(List) method (DG);
 * 25-Nov-2004 : Modified clone(Object) method to fail with objects that
 *               cannot be cloned, added new deepClone(Collection) method.
 *               Renamed ObjectUtils --> ObjectUtilities (DG);
 * 11-Jan-2005 : Removed deprecated code in preparation for 1.0.0 release (DG);
 * 18-Aug-2005 : Added casts to suppress compiler warnings, as suggested in
 *               patch 1260622 (DG);
 * 16-Jun-2012 : Moved from JCommon to JFreeChart (DG);
 *
 */

package org.jfree.chart.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

/**
 * A collection of useful static utility methods for handling classes and object
 * instantiation.
 */
public final class ObjectUtilities {

    /**
     * Default constructor - private.
     */
    private ObjectUtilities() {
    }

    /**
     * Returns <code>true</code> if the two objects are equal OR both
     * <code>null</code>.
     *
     * @param o1 object 1 (<code>null</code> permitted).
     * @param o2 object 2 (<code>null</code> permitted).
     * @return <code>true</code> or <code>false</code>.
     */
    public static boolean equal(final Object o1, final Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 != null) {
            return o1.equals(o2);
        }
        else {
            return false;
        }
    }

    /**
     * Returns a hash code for an object, or zero if the object is
     * <code>null</code>.
     *
     * @param object the object (<code>null</code> permitted).
     * @return The object's hash code (or zero if the object is
     *         <code>null</code>).
     */
    public static int hashCode(final Object object) {
        int result = 0;
        if (object != null) {
            result = object.hashCode();
        }
        return result;
    }

    /**
     * Returns a clone of the specified object, if it can be cloned, otherwise
     * throws a CloneNotSupportedException.
     *
     * @param object the object to clone (<code>null</code> not permitted).
     * @return A clone of the specified object.
     * @throws CloneNotSupportedException if the object cannot be cloned.
     */
    public static <T> T clone(final T object)
        throws CloneNotSupportedException {
        if (object == null) {
            throw new IllegalArgumentException("Null 'object' argument.");
        }
        if (object instanceof PublicCloneable) {
            final PublicCloneable pc = (PublicCloneable) object;
            return (T) pc.clone();
        }
        else {
            try {
                final Method method = object.getClass().getMethod("clone");
                if (Modifier.isPublic(method.getModifiers())) {
                    return (T) method.invoke(object);
                }
            }
            catch (NoSuchMethodException e) {
                //Log.warn("Object without clone() method is impossible.");
            }
            catch (IllegalAccessException e) {
                //Log.warn("Object.clone(): unable to call method.");
            }
            catch (InvocationTargetException e) {
                //Log.warn("Object without clone() method is impossible.");
            }
        }
        throw new CloneNotSupportedException("Failed to clone.");
    }

    /**
     * Returns a new collection containing clones of all the items in the
     * specified collection.
     *
     * @param collection the collection (<code>null</code> not permitted).
     * @return A new collection containing clones of all the items in the
     *         specified collection.
     * @throws CloneNotSupportedException if any of the items in the collection
     *                                    cannot be cloned.
     */
    public static <T, C extends Collection<T>> C deepClone(final C collection)
        throws CloneNotSupportedException {

        if (collection == null) {
            throw new IllegalArgumentException("Null 'collection' argument.");
        }
        // all JDK-Collections are cloneable ...
        // and if the collection is not clonable, then we should throw
        // a CloneNotSupportedException anyway ...
        final C result = ObjectUtilities.clone(collection);
        result.clear();
        for (T item : collection) {
            if (item == null) {
                result.add(null);
            } else {
                result.add(clone(item));
            }
        }
        return result;
    }

}
