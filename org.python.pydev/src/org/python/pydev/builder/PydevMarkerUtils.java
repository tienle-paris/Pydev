/*
 * Created on 11/09/2005
 */
package org.python.pydev.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.python.pydev.plugin.PydevPlugin;

public class PydevMarkerUtils {

    public static IMarker markerExists(IResource resource, String message, int charStart, int charEnd, String type) {
        return markerExists(resource, message, charStart, charEnd, type, null);
    }
    /**
     * Checks pre-existance of marker.
     */
    public static IMarker markerExists(IResource resource, String message, int charStart, int charEnd, String type, List<IMarker> existingMarkers) {
        existingMarkers = checkExistingMarkers(resource, type, existingMarkers);
        
        try {
            for (IMarker task : existingMarkers) {
                Object msg = task.getAttribute(IMarker.MESSAGE);
                Object start = task.getAttribute(IMarker.CHAR_START);
                Object end = task.getAttribute(IMarker.CHAR_END);


                if(msg == null || start == null || end == null || message == null){
                	return null;
                }
                boolean eqMessage = msg.equals(message);
                boolean eqCharStart = (Integer) start == charStart;
				boolean eqCharEnd = (Integer) end == charEnd;

                if (eqMessage && eqCharStart && eqCharEnd) {
                    return task;
                }
            }
        } catch (Exception e) {
            PydevPlugin.log(e);
        }
        return null;
    }

    public static IMarker markerExists(IResource resource, String message, int lineNumber, String type) {
        return markerExists(resource, message, lineNumber, lineNumber, type, null);
    }
    
    /**
     * Checks pre-existance of marker.
     * 
     * @param resource resource in wich marker will searched
     * @param message message for marker
     * @param lineNumber line number where marker should exist
     * @return pre-existance of marker
     */
    public static IMarker markerExists(IResource resource, String message, int lineNumber, String type, List<IMarker> existingMarkers) {
        existingMarkers = checkExistingMarkers(resource, type, existingMarkers);
        
        try {
            for (IMarker task : existingMarkers) {
                boolean eqLineNumber = (Integer)task.getAttribute(IMarker.LINE_NUMBER) == lineNumber;
                boolean eqMessage = task.getAttribute(IMarker.MESSAGE).equals(message);
                if (eqLineNumber && eqMessage){
                    return task;
                }
            }
        } catch (CoreException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public static void createMarker(IResource resource, IDocument doc, String message, 
            int lineStart, int colStart, int lineEnd, int colEnd, 
            String markerType, int severity) throws BadLocationException {
        createMarker(resource, doc, message, lineStart, colStart, lineEnd, colEnd, markerType, severity, null);
    }

    public static IMarker createMarker(IResource resource, IDocument doc, String message, 
            int lineStart, int colStart, int lineEnd, int colEnd, 
            String markerType, int severity, Map<String, Object> additionalInfo) throws BadLocationException {
        return createMarker(resource, doc, message, lineStart, colStart, lineEnd, colEnd, markerType, severity, additionalInfo, null);
    }
    
    public static IMarker createMarker(IResource resource, IDocument doc, String message, 
            int lineStart, int colStart, int lineEnd, int colEnd, 
            String markerType, int severity, Map<String, Object> additionalInfo, List<IMarker> existingMarkers) throws BadLocationException {
    	synchronized (resource) {

	        existingMarkers = checkExistingMarkers(resource, markerType, existingMarkers);
	
	        if(lineStart < 0){
	            lineStart = 0;
	        }
	        
	        int startAbsolute;
	        int endAbsolute;
	        
	        try {
	            IRegion start = doc.getLineInformation(lineStart);
	            startAbsolute = start.getOffset() + colStart;
	            if (lineEnd >= 0 && colEnd >= 0) {
	                IRegion end = doc.getLineInformation(lineEnd);
	                endAbsolute = end.getOffset() + colEnd;
	            } else {
	                //ok, we have to calculate it based on the line contents...
	                String line = doc.get(start.getOffset(), start.getLength());
	                int i;
	                StringBuffer buffer;
	                if((i = line.indexOf('#')) != -1){
	                    buffer = new StringBuffer(line.substring(0, i));
	                }else{
	                    buffer = new StringBuffer(line);
	                }
	                while(buffer.length() > 0 && Character.isWhitespace(buffer.charAt(buffer.length() - 1))){
	                    buffer.deleteCharAt(buffer.length() -1);
	                }
	                endAbsolute = start.getOffset() + buffer.length();
	            }
	        } catch (BadLocationException e) {
                throw e;
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    
	        IMarker marker = markerExists(resource, message, startAbsolute, endAbsolute, markerType, existingMarkers);
	        if (marker == null) {
	            try {
	                
	                
	                HashMap<String, Object> map = new HashMap<String, Object>();
	                map.put(IMarker.MESSAGE, message);
	                map.put(IMarker.LINE_NUMBER, lineStart);
	                map.put(IMarker.CHAR_START, startAbsolute);
	                map.put(IMarker.CHAR_END, endAbsolute);
	                map.put(IMarker.SEVERITY, severity);
	                
	                //add the additional info
	                if(additionalInfo != null){
		                for (Map.Entry<String, Object> entry : additionalInfo.entrySet()) {
		                    map.put(entry.getKey(), entry.getValue());
		                }
	                }
	                
	                MarkerUtilities.createMarker(resource, map, markerType);
	            } catch (Exception e) {
	                PydevPlugin.log(e);
	            }
	        }else{
	        	//to check if it exists, we don't check all attributes, so, let's update those that we don't check (if needed).
	        	try {
	        		final Object lN = marker.getAttribute(IMarker.LINE_NUMBER);
					if(lN == null || ((Integer)lN) != lineStart){
	        			marker.setAttribute(IMarker.LINE_NUMBER, new Integer(lineStart));
	        		}
					
	        		final Object mS = marker.getAttribute(IMarker.SEVERITY);
					if(mS == null || ((Integer)mS) != severity){
	        			marker.setAttribute(IMarker.SEVERITY, severity);
	        		}
					
				} catch (Exception e) {
					PydevPlugin.log(e);
				}
	            existingMarkers.remove(marker);
	        }
	        return marker;
    	}
    }
    /**
     * @param resource
     * @param markerType
     * @param existingMarkers
     * @return
     */
    private static List<IMarker> checkExistingMarkers(IResource resource, String markerType, List<IMarker> existingMarkers) {
    	synchronized (resource) {
	        if(existingMarkers == null){
	            try {
	                existingMarkers = new ArrayList<IMarker>();
	                IMarker[] markers = resource.findMarkers(markerType, true, IResource.DEPTH_ZERO);
	                for (IMarker marker : markers) {
	                    existingMarkers.add(marker);
	                }
	            } catch (CoreException e) {
	            	existingMarkers = new ArrayList<IMarker>();
	                PydevPlugin.log(e);
	            }
	        }
	        return existingMarkers;
    	}
    }
    


    public static IMarker createMarker(IResource resource, IDocument doc, String message, int lineNumber, String markerType, int severity, boolean userEditable, boolean istransient, List<IMarker> existingMarkers) throws BadLocationException {
    	synchronized (resource) {
	    	HashMap<String, Object> map = new HashMap<String, Object>();
	    	map.put(IMarker.USER_EDITABLE, userEditable);
	    	map.put(IMarker.TRANSIENT, istransient);
	        return createMarker(resource, doc, message, lineNumber, 0, lineNumber, 0, markerType, severity, map, existingMarkers);
    	}
    }

}
