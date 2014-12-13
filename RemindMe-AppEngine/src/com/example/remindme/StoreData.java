package com.example.remindme;
import java.util.Date;

import com.google.appengine.api.datastore.Key;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StoreData {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Key key;
   private String group;
   private String groupEmail;
   private String Event;
   
   public Key getKey() {
       return key;
   }

   public String getGroup() {
       return group;
   }
   public void setGroup(String group) {
       this.group = group;
   }

   public String getGroupEmail() {
       return groupEmail;
   }
   public void setGroupEmail(String groupEmail) {
       this.groupEmail = groupEmail;
   }

   public String getEvent() {
       return Event;
   }
   public void setEvent(String Event) {
	   this.Event=Event;
   }
} 
