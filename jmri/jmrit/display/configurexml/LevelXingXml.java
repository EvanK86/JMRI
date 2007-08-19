// jmri.jmrit.display.configurexml.LevelXingXml.java

package jmri.jmrit.display.configurexml;

import jmri.configurexml.XmlAdapter;
import jmri.jmrit.display.LayoutEditor;
import jmri.jmrit.display.PositionablePoint;
import jmri.jmrit.display.LevelXing;
import jmri.jmrit.display.TrackSegment;
import jmri.Sensor;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;
import java.util.List;
import java.awt.Color;
import java.awt.geom.*;

/**
 * This module handles configuration for display.LevelXing objects for a LayoutEditor.
 *
 * @author David Duchamp Copyright (c) 2007
 * @version $Revision: 1.1 $
 */
public class LevelXingXml implements XmlAdapter {

    public LevelXingXml() {
    }

    /**
     * Default implementation for storing the contents of a
     * LevelXing
     * @param o Object to store, of type LevelXing
     * @return Element containing the complete info
     */
    public Element store(Object o) {

        LevelXing p = (LevelXing)o;

        Element element = new Element("levelxing");

        // include attributes
        element.setAttribute("ident", p.getID());
		if (p.getBlockNameAC().length()>0) {
			element.setAttribute("blocknameac", p.getBlockNameAC());
		}
		if (p.getBlockNameBD().length()>0) {
			element.setAttribute("blocknamebd", p.getBlockNameBD());
		}
		if (p.getConnectA()!=null) {
			element.setAttribute("connectaname", ((TrackSegment)p.getConnectA()).getID());
		}
		if (p.getConnectB()!=null) {
			element.setAttribute("connectbname", ((TrackSegment)p.getConnectB()).getID());
		}
		if (p.getConnectC()!=null) {
			element.setAttribute("connectcname", ((TrackSegment)p.getConnectC()).getID());
		}
		if (p.getConnectD()!=null) {
			element.setAttribute("connectdname", ((TrackSegment)p.getConnectD()).getID());
		}
		Point2D coords = p.getCoordsCenter();
		element.setAttribute("xcen", ""+coords.getX());
		element.setAttribute("ycen", ""+coords.getY());
		coords = p.getCoordsA();
		element.setAttribute("xa", ""+coords.getX());
		element.setAttribute("ya", ""+coords.getY());
		coords = p.getCoordsB();
		element.setAttribute("xb", ""+coords.getX());
		element.setAttribute("yb", ""+coords.getY());		

        element.setAttribute("class", "jmri.jmrit.display.configurexml.LevelXingXml");
        return element;
    }

    public void load(Element element) {
        log.error("Invalid method called");
    }

    /**
     * Load, starting with the levelxing element, then
     * all the other data
     * @param element Top level Element to unpack.
     * @param o  LayoutEditor as an Object
     */
    public void load(Element element, Object o) {
        // create the objects
        LayoutEditor p = (LayoutEditor)o;
		
		// get center point
        String name = element.getAttribute("ident").getValue();
		double x = 0.0;
		double y = 0.0;
		try {
			x = element.getAttribute("xcen").getFloatValue();
			y = element.getAttribute("ycen").getFloatValue();
		} catch (org.jdom.DataConversionException e) {
            log.error("failed to convert levelxing center  attribute");
        }
		
		// create the new LevelXing
        LevelXing l = new LevelXing(name,new Point2D.Double(x,y),p);
		
		// get remaining attributes
		Attribute a = element.getAttribute("blocknameac");
		if (a != null) {
			l.tBlockNameAC = a.getValue();
		}
		a = element.getAttribute("blocknamebd");
		if (a != null) {
			l.tBlockNameBD = a.getValue();
		}
		a = element.getAttribute("connectaname");
		if (a != null) {
			l.connectAName = a.getValue();
		}
		a = element.getAttribute("connectbname");
		if (a != null) {
			l.connectBName = a.getValue();
		}
		a = element.getAttribute("connectcname");
		if (a != null) {
			l.connectCName = a.getValue();
		}
		a = element.getAttribute("connectdname");
		if (a != null) {
			l.connectDName = a.getValue();
		}
		try {
			x = element.getAttribute("xa").getFloatValue();
			y = element.getAttribute("ya").getFloatValue();
		} catch (org.jdom.DataConversionException e) {
            log.error("failed to convert levelxing a coords attribute");
        }
		l.setCoordsA(new Point2D.Double(x,y));
		try {
			x = element.getAttribute("xb").getFloatValue();
			y = element.getAttribute("yb").getFloatValue();
		} catch (org.jdom.DataConversionException e) {
            log.error("failed to convert levelxing b coords attribute");
        }
		l.setCoordsB(new Point2D.Double(x,y));


		p.xingList.add(l);
    }

    static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(LevelXingXml.class.getName());
}