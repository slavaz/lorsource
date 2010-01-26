/*
 * Copyright 1998-2010 Linux.org.ru
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

package ru.org.linux.spring;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.net.URLEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import ru.org.linux.site.LorDataSource;
import ru.org.linux.site.User;
import ru.org.linux.site.UserNotFoundException;

@Controller
public class WhoisController {
  @RequestMapping("/people/{nick}/profile")
  public ModelAndView getInfoNew(@PathVariable String nick) throws Exception {
    Connection db = null;
    try {
      db = LorDataSource.getConnection();

      User user = User.getUser(db, nick);

      return new ModelAndView("whois", Collections.singletonMap("user", user));
    } finally {
      if (db!=null) {
        db.close();
      }
    }
  }

  @RequestMapping("/whois.jsp")
  public View getInfo(@RequestParam("nick") String nick) throws SQLException, UserNotFoundException {
    return new RedirectView("/people/"+ URLEncoder.encode(nick)+"/profile");
  }
}
