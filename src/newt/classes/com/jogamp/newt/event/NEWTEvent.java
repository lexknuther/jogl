/*
 * Copyright (c) 2008 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 */

package com.jogamp.newt.event;

import com.jogamp.newt.*;
import java.util.*;

/**
 * NEWT events are provided for notification purposes ONLY;<br>
 * The NEWT will automatically handle the event semantics internally, regardless of whether a program is receiving these events or not.<br>
 * The actual event semantic is processed before the event is send.<br>
 *
 * Event type registry:<br>
 * <ul>
 *   <li> WindowEvent <code>100..10x</code></li>
 *   <li> MouseEvent  <code>200..20x</code></li>
 *   <li> KeyEvent    <code>300..30x</code></li>
 * </ul><br>
 */
public class NEWTEvent extends java.util.EventObject {
    private boolean isSystemEvent;
    private int eventType;
    private long when;
    private Object attachment;

    static final boolean DEBUG = false;

    //  0: NEWTEvent.java
    //  1:   InputEvent.java
    //  2:       KeyEvent.java  
    //  3:          com.jogamp.newt.Window
    //  3:          com.jogamp.newt.event.awt.AWTNewtEventFactory
    //  2:       MouseEvent.java  
    //  3:          com.jogamp.newt.Window
    //  3:          com.jogamp.newt.event.awt.AWTNewtEventFactory
    //  1:   WindowEvent.java
    //  2:       com.jogamp.newt.Window
    //  2:       com.jogamp.newt.event.awt.AWTNewtEventFactory
    //
    // FIXME: verify the isSystemEvent evaluation
    //
    static final String WindowClazzName = "com.jogamp.newt.Window" ;
    static final String AWTNewtEventFactoryClazzName = "com.jogamp.newt.event.awt.AWTNewtEventFactory" ;

    /**
    static final boolean evaluateIsSystemEvent(NEWTEvent event, Throwable t) {
        StackTraceElement[] stack = t.getStackTrace();
        if(stack.length==0 || null==stack[0]) {
            return false;
        }
        if(DEBUG) {
            for (int i = 0; i < stack.length && i<5; i++) {
             System.err.println(i+": " + stack[i].getClassName()+ "." + stack[i].getMethodName());
            }
        }

        String clazzName = null;

        if( event instanceof com.jogamp.newt.event.WindowEvent ) {
            if ( stack.length > 2 ) {
                clazzName = stack[2].getClassName();
            }
        } else if( (event instanceof com.jogamp.newt.event.MouseEvent) ||
                   (event instanceof com.jogamp.newt.event.KeyEvent) ) {
            if ( stack.length > 3 ) {
                clazzName = stack[3].getClassName();
            }
        }

        boolean res = null!=clazzName && (
                        clazzName.equals(WindowClazzName) || 
                        clazzName.equals(AWTNewtEventFactoryClazzName) ) ;
        if(DEBUG) {
            System.err.println("system: "+res);
        }
        return res;
    } */

    protected NEWTEvent(int eventType, Object source, long when) {
        super(source);
        // this.isSystemEvent = evaluateIsSystemEvent(this, new Throwable());
        this.isSystemEvent = false; // FIXME: Need a more efficient way to determine system events
        this.eventType = eventType;
        this.when = when;
        this.attachment=null;
    }

    /** Indicates whether this event was produced by the system or
        generated by user code. */
    public final boolean isSystemEvent() {
        return isSystemEvent;
    }

    /** Returns the event type of this event. */
    public final int getEventType() {
        return eventType;
    }

    /** Returns the timestamp, in milliseconds, of this event. */
    public final long getWhen()  {
        return when;
    }

    /** 
     * Attach the passed object to this event.<br>
     * If an object was previously attached, it will be replaced.<br>
     * Attachments to NEWT events allow users to pass on information
     * from one custom listener to another, ie custom listener to listener 
     * communication.
     * @param attachment User application specific object
     */
    public final void setAttachment(Object attachment) {
        this.attachment=attachment;
    }

    /** 
     * @return The user application specific attachment, or null
     */
    public final Object getAttachment() {
        return attachment;
    }

    public String toString() {
        return "NEWTEvent[sys:"+isSystemEvent()+", source:"+getSource().getClass().getName()+", when:"+getWhen()+" d "+(System.currentTimeMillis()-getWhen())+"ms]";
    }

    public static String toHexString(int hex) {
        return "0x" + Integer.toHexString(hex);
    }

    public static String toHexString(long hex) {
        return "0x" + Long.toHexString(hex);
    }

}
