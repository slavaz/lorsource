/*
 * Copyright 1998-2012 Linux.org.ru
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package ru.org.linux.tracker;

import junit.framework.Assert;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.org.linux.site.Template;
import ru.org.linux.user.User;
import ru.org.linux.user.UserErrorException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Тесты для TrackerService.
 */
@RunWith(JUnitParamsRunner.class)
public class TrackerServiceTest {

  private TrackerDao trackerDao;
  private TrackerService trackerService;
  private Template template;

  @Before
  public void setUp() throws Exception {
    trackerDao = mock(TrackerDao.class);
    trackerService = new TrackerService(trackerDao);
    template = mock(Template.class, RETURNS_DEEP_STUBS);
  }

  @Test(expected = UserErrorException.class)
  public void getShouldRaiseException() throws UserErrorException {
    // given
    when(template.isSessionAuthorized()).thenReturn(false);

    // when
    trackerService.get(template, 1, TrackerFilterEnum.MINE);

    // then
  }

  @Test
  public void getShouldCheckUserIfTypeIsMINE() throws UserErrorException {
    // given
    when(template.isSessionAuthorized()).thenReturn(true);

    List<TrackerItem> expectedValue = prepareForGetTest(TrackerFilterEnum.MINE);

    // when
    List<TrackerItem> actualValue = trackerService.get(template, 1, TrackerFilterEnum.MINE);

    // then
    verify(template).isSessionAuthorized();
    checkGetTestResults(TrackerFilterEnum.MINE, expectedValue, actualValue);
  }

  @Test
  @Parameters(method = "getShouldReturnValueDataSource")
  public void getShouldReturnValue(
          TrackerFilterEnum inputTrackerFilterEnum
  ) throws UserErrorException {
    // given
    List<TrackerItem> expectedValue = prepareForGetTest(inputTrackerFilterEnum);

    // when
    List<TrackerItem> actualValue = trackerService.get(template, 1, inputTrackerFilterEnum);

    // then
    checkGetTestResults(inputTrackerFilterEnum, expectedValue, actualValue);
  }

  private Object[] getShouldReturnValueDataSource() {
    return new Object[]{
            new Object[]{TrackerFilterEnum.ALL},
            new Object[]{TrackerFilterEnum.NOTALKS},
            new Object[]{TrackerFilterEnum.TECH},
            new Object[]{TrackerFilterEnum.ZERO},
    };
  }

  private void checkGetTestResults(TrackerFilterEnum inputTrackerFilterEnum, List<TrackerItem> expectedValue, List<TrackerItem> actualValue) {
    Assert.assertSame(expectedValue, actualValue);
    verify(template).getCurrentUser();
    verify(template, atLeastOnce()).getProf();
    verify(trackerDao).getTrackAll(eq(inputTrackerFilterEnum), (User) anyObject(), (Timestamp) anyObject(),
            eq(67890), eq(1), eq(12345));
    verifyNoMoreInteractions(
            template,
            trackerDao
    );
    verifyZeroInteractions(template);
  }

  private List<TrackerItem> prepareForGetTest(TrackerFilterEnum inputTrackerFilterEnum) {
    when(template.getProf().getMessages()).thenReturn(12345);
    when(template.getProf().getTopics()).thenReturn(67890);
    when(template.getCurrentUser()).thenReturn(mock(User.class));
    List<TrackerItem> expectedValue = new ArrayList<TrackerItem>();
    when(trackerDao.getTrackAll(eq(inputTrackerFilterEnum), (User) anyObject(), (Timestamp) anyObject(),
            eq(67890), eq(1), eq(12345)))
            .thenReturn(expectedValue);
    return expectedValue;
  }
}
