/*
 * Copyright 2011 The Rabbit Eclipse Plug-in Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package rabbit.tracking;

import org.joda.time.Duration;
import org.joda.time.Instant;

/**
 * Represents an event happened at a particular time.
 * 
 * @since 2.0
 */
public interface IEvent {

  /**
   * Gets the duration of this event.
   * 
   * @return the duration of this event, not null
   */
  Duration getDuration();

  /**
   * Gets the start time of this event.
   * 
   * @return the start time of this event, not null
   */
  Instant getInstant();
}