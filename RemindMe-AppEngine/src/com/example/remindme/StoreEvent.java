package com.example.remindme;
import java.util.Date;

import com.google.appengine.api.datastore.Key;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StoreEvent {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Key key;
   private String Group;
   private String EventName;
   private String Description;
   private String EventTime;
   private String EventDate;
   private String EventLocation;
   private String EventID;
   public Key getKey() {
       return key;
   }
   public String getEventID() {
       return EventID;
   }
   public void setEventID(String eventID) {
       this.EventID = eventID;
   }

   public String getGroup() {
       return Group;
   }
   public void setGroup(String group) {
       this.Group = group;
   }

   public String getEventName() {
       return EventName;
   }
   public void setEventName(String name) {
       this.EventName = name;
   }
   public String getEventTime() {
       return EventTime;
   }
   public void setEventTime(String time) {
       this.EventTime = time;
   }
   public String getEventDate() {
       return EventDate;
   }
   public void setEventDate(String date) {
       this.EventDate = date;
   }
   public String getDescription() {
       return Description;
   }
   public void setDescription(String description) {
	   this.Description=description;
   }
   public String getLocation() {
       return EventLocation;
   }
   public void setLocation(String eventlocation) {
	   this.EventLocation=eventlocation;
   }
} 
