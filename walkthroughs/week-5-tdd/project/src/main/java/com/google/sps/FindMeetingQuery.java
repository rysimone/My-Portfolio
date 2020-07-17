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

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    throw new UnsupportedOperationException("TODO: Implement this method.");
    //create an empty Collection<TimeRange> for available times for the requested meeting
    //check if the duration of the event is more than a day or if the collection of attendees is empty
    //if yes, return the empty Collection
    //if no, begin looking for appropriate time slots
    //Iterate through the day by checking each appropriate time duration ex. if meeting duration is 1hr 8-9, 8:30-9:30, ...
    //for each time duration check if there is an event present and if anyone is already attending that meeting
    //If the time is accepted, add it to the collection
    //return the collection of times


  }
}
