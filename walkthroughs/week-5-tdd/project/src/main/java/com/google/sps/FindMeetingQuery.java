// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;

public final class FindMeetingQuery {

  /*
   * Given a list of scheduled {@code events}, query() returns a list of all available times for the 
   * new meeting {@code request}.
   *
   * @param events: list of scheduled events for attendees/optional attendees
   * @param request: the new meeting that is to be scheduled 
   *
   * @return: a list of all available times the meeting can be held
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    
    /* Checks if the new meeting {@code request} is longer than a day.
     * If yes, return an empty list;
     */
    if(request.getDuration() > TimeRange.WHOLE_DAY.duration()){
      return new ArrayList<>();
    }

    /* Checks if there are optional attendees for the {@code request}
     * If yes, search for times where both optional and non-optional attendees can attend.
     * If no such time exists, priority goes to non-optional attendees
     */
    if(request.checkForOptionalAttendees()){
      ArrayList<TimeRange> availableTimes = findAvailableMeetingTimes(events, request, /* optionalAttendees= */true);
      if(!availableTimes.isEmpty()){
          return availableTimes;
      }
    }
    return findAvailableMeetingTimes(events, request, /* optionalAttendees= */false);
  }

  /* Given a list of {@code events}, findAvailableMeetingTimes() searches for all the possible times the new meeting
   * {@code request} can take place. 
   * 
   * @param events: list of scheduled events for attendees/optional attendees
   * @param request: the new meeting that is to be scheduled
   * @param optionalAttendees: boolean value that signals whether or not to consider optional attendees from the {@code request}
   *
   * @return: a sorted list of available meeting times in chronological order
   */
  private ArrayList<TimeRange> findAvailableMeetingTimes(Collection<Event> events, MeetingRequest request, boolean optionalAttendees){

    // Creates a list of all the time ranges in a day to check for conflicts with {@code events}
    ArrayList<TimeRange> availableMeetingTimes = new ArrayList<TimeRange>();
    availableMeetingTimes.add(TimeRange.WHOLE_DAY);

    // Iterates through all the scheduled events to find any conflicts
    for(Event event : events){
      TimeRange eventTime = event.getWhen();
      
      /* Checks if the {@code event} has any attendees that are also an attendee for the new meeting {@code request}
       * If the two collections are disjoint, skip this event
       */
      if(checkIfAttendeesAreDisjoint(event, request, optionalAttendees)){
          continue;
      }
      // List to hold any newly-created {@link TimeRange}s from {@code timesAroundEvent}
      ArrayList<TimeRange> addTimes = new ArrayList<TimeRange>();
      // List to hold any {@link TimeRange}s from {@code timesAroundEvent} in which the meeting cannot be held
      ArrayList<TimeRange> removeTimes = new ArrayList<TimeRange>();

      // Checks if the {@code eventTime} overlaps with the current {@code time}
      for(TimeRange time: availableMeetingTimes){
        if(time.overlaps(eventTime)) {
          removeTimes.add(time);
          addTimes.addAll(timesAroundEvent(request.getDuration(), time, eventTime));
        }
      }
      availableMeetingTimes.removeAll(removeTimes);
      availableMeetingTimes.addAll(addTimes);
      }
      return sortArray(availableMeetingTimes);
    }
  
  /*
   * Resolves conflicting event times by checking before and after the {@code eventTime} for
   * available times with appropriate {@code newMeetingDuration}. If a new available {@code time} is found, it is added to the {@code newTimes} list.
   *
   * @param newMeetingDuration: the duration of requested meeting
   * @param time: The possible new meeting {@code request} time, where an overlap in time was found with a pre-existing event
   * @param eventTime: the time of the pre-existing event 
   * 
   * @return: a {@code newTimes} list that consists of the available times found around the pre-existing event time
   */ 
  private ArrayList<TimeRange> timesAroundEvent(long newMeetingDuration, TimeRange time, TimeRange eventTime) {
    // Creates a list to store the new available meeting times
    ArrayList<TimeRange> newTimes = new ArrayList<TimeRange>();
    // Create a Time Range for before the {@code eventTime}
    TimeRange beforeConflict = TimeRange.fromStartEnd(time.start(), eventTime.start(), false);
    // Create a Time Range for after teh {@code eventTime}
    TimeRange afterConflict = TimeRange.fromStartEnd(eventTime.end(), time.end(), false);

    // Checks if the new Time Ranges created are longer than the {@code newMeetingDuration}
    if(beforeConflict.duration() >= newMeetingDuration){
      newTimes.add(beforeConflict);
    }
    if(afterConflict.duration() >= newMeetingDuration){
      newTimes.add(afterConflict);
    }
    return newTimes;
  }

  /*
   * Returns a sorted array in chronological order
   */
  private ArrayList<TimeRange> sortArray(Collection<TimeRange> arr){
    ArrayList<TimeRange> sortedArr = new ArrayList<>(arr);
    Collections.sort(sortedArr, TimeRange.ORDER_BY_START);
    return sortedArr;
  }

  /* Checks if the {@code event} and the {@code request} are disjointed in their attendees and optional attendees
   * Optional attendees are only checked if the {@param hasOptionalAttendees} is true
   * @param events: list of scheduled events
   * @param request: the meeting being requested
   * @param considerOptionalAttendees: true if the {@code request} has optional attendees
   * @return: true if the events are dijointed in attendees, false otherwise
   */
  private boolean checkIfAttendeesAreDisjoint(Event event, MeetingRequest request, boolean considerOptionalAttendees){
    return Collections.disjoint(event.getAttendees(), request.getAttendees()) &&
     (!considerOptionalAttendees ||
       Collections.disjoint(event.getAttendees(), request.getOptionalAttendees()));
  }
}