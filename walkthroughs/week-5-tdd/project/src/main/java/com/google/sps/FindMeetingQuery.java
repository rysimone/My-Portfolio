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
     * If yes, search for a time where both optional and non-optional attendees can attend.
     * If no such time exists, priority goes to non-optional attendees
     */
    if(request.checkForOptionalAttendees()){
      ArrayList<TimeRange> availableTimes = findAvailableMeetingTimes(events, request, true);
      if(!request.checkForAttendees() || !availableTimes.isEmpty()){
          return availableTimes;
      }
    }
    return findAvailableMeetingTimes(events, request, false);
  }

  /* Given a list of {@code events}, findAvailableMeetingTimes() searches for all the possible times the new meeting
   * {@code request} can take place. 
   * 
   * @param events: list of scheduled events for attendees/optional attendees
   * @param request: the new meeting that is to be scheduled
   * @param hasOptionalAttendees: boolean value that informs the function of whether there are optional attendees for the new meeting
   *
   * @return: a sorted list of available meeting times in chronological order
   */
  private ArrayList<TimeRange> findAvailableMeetingTimes(Collection<Event> events, MeetingRequest request, boolean hasOptionalAttendees){

    // Creates a list of all the time ranges in a day to check for conflicts with {@code events}
    ArrayList<TimeRange> availableMeetingTimes = new ArrayList<TimeRange>();
    availableMeetingTimes.add(TimeRange.WHOLE_DAY);

    // Iterates through all the scheduled events to find any conflicts
    for(Event event : events){
      TimeRange eventTime = event.getWhen();
      
      /* Checks if the {@ code event} has any attendees that are also an attendee for the new meeting {@code request}
       * If the two collections are disjoint, skip this event
       */
      if((Collections.disjoint(event.getAttendees(), request.getAttendees())) && (!hasOptionalAttendees || Collections.disjoint(event.getAttendees(), request.getOptionalAttendees()))){
          continue;
      }
      // List of all times that the meeting can take place
      ArrayList<TimeRange> addTimes = new ArrayList<TimeRange>();
      // List of all the times the meeting cannot take place
      ArrayList<TimeRange> removeTimes = new ArrayList<TimeRange>();

      for(TimeRange time: availableMeetingTimes){
          if(time.overlaps(eventTime)) {
            timesAroundEvent(addTimes, removeTimes, request.getDuration(), time, eventTime);
          }
      }
      availableMeetingTimes.removeAll(removeTimes);
      availableMeetingTimes.addAll(addTimes);
      }
      return sortArray(availableMeetingTimes);
    }
  
  /*
   * Resolves conflicting event times by checking before and after the {@code eventTime} for
   * available times with appropriate {@code newMeetingDuration}
   *
   * @param addTimes: list of all the available time ranges for the new meeting
   * @param removeTimes: list of all the time ranges where the meeting cannot be held
   * @param newMeetingDuration: the duration of requested meeting
   * @param time: The conflicting time that the function is checking around
   * @param eventTime: the time of the event 
   */ 
  private void timesAroundEvent(ArrayList<TimeRange> addTimes, ArrayList<TimeRange> removeTimes, long newMeetingDuration, TimeRange time, TimeRange eventTime) {
    TimeRange beforeConflict = TimeRange.fromStartEnd(time.start(), eventTime.start(), false);
    TimeRange afterConflict = TimeRange.fromStartEnd(eventTime.end(), time.end(), false);
    if(beforeConflict.duration() >= newMeetingDuration){
      addTimes.add(beforeConflict);
    }
    if(afterConflict.duration() >= newMeetingDuration){
      addTimes.add(afterConflict);
    }
    removeTimes.add(time);
  }

  /*
   * Returns a sorted array in chronological order
   */
  private ArrayList<TimeRange> sortArray(Collection<TimeRange> arr){
    ArrayList<TimeRange> sortedArr = new ArrayList<>(arr);
    Collections.sort(sortedArr, new Comparator<TimeRange>(){
      @Override
      public int compare(TimeRange t1, TimeRange t2){
        return Long.compare(t1.start(), t2.start());
      }
    });
    return sortedArr;
  }
}