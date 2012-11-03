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
package ru.org.linux.gallery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@ContextConfiguration("integration-tests-context.xml")
public class ImageDaoIntegrationTest extends AbstractTestNGSpringContextTests {

  @Autowired
  private ImageDao imageDao;

  /**
   * Проверка galleryDao.getGalleryItems().
   * Проверяется, что метод вернёт 3 объекта GalleryDto.
   */
  @Test
  public void getGalleryItemsTest() {
    List<GalleryItem> galleryDtoList = imageDao.getGalleryItems(3);
    Assert.assertEquals(3, galleryDtoList.size());
  }
}
