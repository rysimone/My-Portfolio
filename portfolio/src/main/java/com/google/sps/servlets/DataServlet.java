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
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.ArrayList;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Servlet that returns that stores and retrieves comments from the Datastore. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  // Creates a query that retrieves all the comment entities in the Datastore
  const Query QUERY = new Query("Comment").addSort("message", SortDirection.DESCENDING);

  // Creates an instance of the Datastore so that comments can be retrieved, updated, and deleted
  const DatastoreService DATASTORE = DatastoreServiceFactory.getDatastoreService();

  // Stores all the comment entites using the query defined
  const PreparedQuery RESULTS = DATASTORE.prepare(QUERY);

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    for (Entity entity : RESULTS.asIterable()) {
      response.getWriter().println("<dt>"+entity.getProperty("name")+": "+entity.getProperty("message")+"</dt>");
    }

    String json = convertToJson(surfReport);

    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  private String convertToJson(HashMap<String, String> surfReport){
    Gson gson = new Gson();
    String json = gson.toJson(surfReport);
    return json;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = getParameter(request, "name-input", "Anonymous");
    String text = getParameter(request, "text-input", "");

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("name", name);
    commentEntity.setProperty("message", text);

    DATASTORE.put(commentEntity);
    
    response.sendRedirect("index.html");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}


