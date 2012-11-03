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

package ru.org.linux.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.bind.WebDataBinder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ContextConfiguration("unit-tests-context.xml")
public class TagServiceTest extends AbstractTestNGSpringContextTests {
  @Autowired
  TagService tagService;

  @Autowired
  TagDao tagDao;

  WebDataBinder binder;

  @BeforeMethod
  public void resetTagDaoMock() {
    reset(tagDao);
  }

  private void prepareChangeDataBinder() {
    TagRequest.Change tagRequestChange = new TagRequest.Change();
    binder = new WebDataBinder(tagRequestChange);
  }

  private void prepareDeleteDataBinder() {
    TagRequest.Delete tagRequestDelete = new TagRequest.Delete();
    binder = new WebDataBinder(tagRequestDelete);
  }

  @Test
  public void changeTest()
    throws Exception {
    when(tagDao.getTagId("testTag")).thenReturn(123);
    when(tagDao.getTagId("testNewTag")).thenReturn(456);
    when(tagDao.getTagId("InvalidTestTag")).thenThrow(new TagNotFoundException("TagNotFoundException"));

    prepareChangeDataBinder();
    tagService.change("InvalidTestTag", "testNewTag", binder.getBindingResult());
    Assert.assertTrue(binder.getBindingResult().hasErrors());

    prepareChangeDataBinder();
    tagService.change("testTag", "#$%@@#%$", binder.getBindingResult());
    Assert.assertTrue(binder.getBindingResult().hasErrors());

    prepareChangeDataBinder();
    tagService.change("#$%@@#%$", "testNewTag", binder.getBindingResult());
    Assert.assertTrue(binder.getBindingResult().hasErrors());

    prepareChangeDataBinder();
    tagService.change("testTag", "testNewTag", binder.getBindingResult());
    Assert.assertTrue(binder.getBindingResult().hasErrors());

    when(tagDao.getTagId("testNewTag")).thenThrow(new TagNotFoundException("TagNotFoundException"));
    prepareChangeDataBinder();
    tagService.change("testTag", "testNewTag", binder.getBindingResult());
    Assert.assertFalse(binder.getBindingResult().hasErrors());
  }
  @Test
  public void deleteTest()
    throws Exception {

    when(tagDao.getTagId("InvalidTestTag")).thenThrow(new TagNotFoundException("TagNotFoundException"));

    prepareDeleteDataBinder();
    tagService.delete("InvalidTestTag", "testNewTag", binder.getBindingResult());
    Assert.assertTrue(binder.getBindingResult().hasErrors());

    prepareDeleteDataBinder();
    tagService.delete("testTag", "#$%@@#%$", binder.getBindingResult());
    Assert.assertTrue(binder.getBindingResult().hasErrors());

    prepareDeleteDataBinder();
    tagService.delete("#$%@@#%$", "testNewTag", binder.getBindingResult());
    Assert.assertTrue(binder.getBindingResult().hasErrors());

    prepareDeleteDataBinder();
    tagService.delete("testTag", "testTag", binder.getBindingResult());
    Assert.assertTrue(binder.getBindingResult().hasErrors());

    prepareDeleteDataBinder();
    tagService.delete("testTag", "testNewTag", binder.getBindingResult());
    Assert.assertFalse(binder.getBindingResult().hasErrors());
  }
}
