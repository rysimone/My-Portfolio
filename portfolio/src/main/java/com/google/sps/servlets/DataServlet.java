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

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.util.HashMap;


/** Servlet that returns some example content. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private ArrayList<String> comments = new ArrayList<>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Query query = new Query("Comment").addSort("message", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      response.setContentType("text/html");
      response.getWriter().println("<dt>"+entity.getProperty("name")+": "+entity.getProperty("message")+"</dt>");
    }
  }


  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = getParameter(request, "name-input", "Anonymous");
    String text = getParameter(request, "text-input", "");

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("name", name);
    commentEntity.setProperty("message", text);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
    
    response.sendRedirect("index.html");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  private String convertToJson(HashMap<String, String> surfReport){
    Gson gson = new Gson();
    String json = gson.toJson(surfReport);
    return json;

  }
}


