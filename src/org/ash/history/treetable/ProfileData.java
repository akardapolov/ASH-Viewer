/*
 *-------------------
 * The ProfileData.java is part of ASH Viewer
 *-------------------
 * 
 * ASH Viewer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASH Viewer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ASH Viewer.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (c) 2009, Alex Kardapolov, All rights reserved.
 *
 */
package org.ash.history.treetable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * The userObject of the node. 
 * Top level class.
 * 
 * NR: Add transferable
 */
public class ProfileData implements Comparable<ProfileData>, Transferable {
    private String name = "";
    private String size = "";
    private String directory = "";
    private Double beginDouble = 0.0;
    private Double endDbouble = 0.0;

    public ProfileData() {
    }

    public ProfileData(String name, String size, 
            String directory, Double beginDouble, Double endDouble) {
        super();
        this.name = name;
        this.size = size;
        this.directory = directory;
        this.beginDouble = beginDouble;
        this.endDbouble = endDouble;
    }


    /**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @return the directory
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * @return the beginDouble
	 */
	public Double getBeginDouble() {
		return beginDouble;
	}

	/**
	 * @return the endDbouble
	 */
	public Double getEndDbouble() {
		return endDbouble;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	/**
	 * @param beginDouble the beginDouble to set
	 */
	public void setBeginDouble(Double beginDouble) {
		this.beginDouble = beginDouble;
	}

	/**
	 * @param endDbouble the endDbouble to set
	 */
	public void setEndDbouble(Double endDbouble) {
		this.endDbouble = endDbouble;
	}

	@Override
    public String toString() {
        return name + " " + size + " " + directory;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result
                + ((name == null) ? 0 : name.hashCode());
        result = PRIME * result
                + ((size == null) ? 0 : size.hashCode());
        result = PRIME * result
                + ((directory == null) ? 0 : directory.hashCode());
        result = PRIME * result
        		+ ((beginDouble == null) ? 0 : beginDouble.hashCode());
        result = PRIME * result
        		+ ((endDbouble == null) ? 0 : endDbouble.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ProfileData other = (ProfileData) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (size == null) {
            if (other.size != null)
                return false;
        } else if (!size.equals(other.size))
            return false;
        if (directory == null) {
            if (other.directory != null)
                return false;
        } else if (!directory.equals(other.directory))
            return false;
        if (beginDouble == null) {
            if (other.beginDouble != null)
                return false;
        } else if (!beginDouble.equals(other.beginDouble))
            return false;
        if (endDbouble == null) {
            if (other.endDbouble != null)
                return false;
        } else if (!endDbouble.equals(other.endDbouble))
            return false;
        return true;
    }

    public int compareTo(ProfileData otherObject) {
        int res = 0;
        res = otherObject.getName().compareTo(getName());
        if (0 == res) {
            res = otherObject.getDirectory().compareTo(getDirectory());
        }
        return res;
    }

    /* (non-Javadoc)
     * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
     */
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            return this;
        }
        throw new UnsupportedFlavorException(flavor);
    }

    /* (non-Javadoc)
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { new DataFlavor(ProfileData.class, "ProfileData") };
    }

    /* (non-Javadoc)
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(new DataFlavor(ProfileData.class, "ProfileData"));
    }
}
