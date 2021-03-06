/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 * For questions related to commercial use licensing, please contact sales@telestax.com.
 *
 */

package org.restcomm.app.utillib.DataObjects;


import android.net.Uri;

/**
 * This class represents an event couple. Almost all events in the application occur in
 * couples, so it made sense to encapsulate them into a class. For example, the "lost data"
 * event is always coupled with the "regained data" event.
 * 
 * This class allows the user to trigger the startEvent segment of the event couple and 
 * similarly to trigger the stopEvent segment of the couple.
 * @author abhin
 *
 */
public class EventCouple {
	/*
	 * private variables
	 */
	private EventObj startEvent;
	private EventObj stopEvent;
	private EventType startEventType, stopEventType;
	
	private boolean isComplete;	//whether the event has been completed
	
	
	private Uri uri;	//the uri of the row in the database table for event couples
	
	/*
	 * constructors
	 */
	/**
	 * The constructor initializes the event type and starts the event.
	 */
	public EventCouple(EventType startEventType, EventType stopEventType, Uri eventCoupleUri, Uri startEventUri)
	{
		this(startEventType, stopEventType, eventCoupleUri, startEventUri, System.currentTimeMillis());
	}
	public EventCouple(EventType startEventType, EventType stopEventType, Uri eventCoupleUri, Uri startEventUri, long time)
	{
		
		this.startEvent = new EventObj(startEventType, startEventUri, time);
		this.stopEvent = new EventObj(stopEventType);
		this.startEventType = startEventType;
		this.stopEventType = stopEventType;
		this.isComplete = false;
		
		this.uri = eventCoupleUri;
	}
	/*
	 * Getters and Setters
	 */
	public EventType getStartEventType(){
		return this.startEventType;
	}
	
	public EventType getStopEventType(){
		return this.stopEventType;
	}
	
	public EventObj getStartEvent(){
		return this.startEvent;
	}
	
	public EventObj getStopEvent(){
		return this.stopEvent;
	}
	
	public long getStartDuration(){
		return this.startEvent.getDuration();
	}
	
	public long getStopDuration(){
		return this.stopEvent.getDuration();
	}
	
	public boolean isEventComplete(){
		return this.isComplete;
	}
	
	public Uri getUri() {
		return uri;
	}
	public void setUri(Uri uri) {
		this.uri = uri;
	}
	
	/**
	 * Sometimes the stop event type needs to be changed after creating the event couple.
	 * A good example is the call state. When a call starts, an event couple is created
	 * using EVT_CONNECT and EVT_DROPPED as the start and stop events. But if the call ends
	 * unexpectedly, then the stop event type is replaced by EVT_FAIL to symbolize the fact
	 * that the call ended abruptly.
	 * @param stopEventType
	 */
	public void setStopEventType(EventType stopEventType){
		this.stopEventType = stopEventType;
		this.stopEvent.setEventType(stopEventType);
	}
	
	/*
	 * Public Methods
	 */
	public void triggerStartEvent(EventType eventType){
		this.triggerStartEvent();
		this.startEvent.setEventType(eventType);
	}
	
	public void triggerStartEvent(){
		this.startEvent.setEventTimestampToNow();
		this.isComplete = false;
	}
	
	public void triggerStopEvent(){
		this.stopEvent.setEventTimestampToNow();
		this.isComplete = true;
	}
	
	public void triggerStopEvent(EventType eventType){
		this.triggerStopEvent();
		this.stopEvent.setEventType(eventType);
	}
	
	public void triggerStopEvent(long duration){
		this.triggerStopEvent();
		this.stopEvent.setDuration(duration);
	}
	
	public void triggerStopEvent(EventType eventType, long duration){
		this.triggerStopEvent(eventType);
		this.stopEvent.setDuration(duration);
	}
}
