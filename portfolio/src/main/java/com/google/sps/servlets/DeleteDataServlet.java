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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Key;


/** Servlet that deletes comments in Datastore. */
@WebServlet("/delete-data")
public class DeleteDataServlet extends HttpServlet {

  // Creates a query that retrieves all the comment entities in the Datastore
  private static final Query QUERY = new Query("Comment");

  // Creates an instance of the Datastore so that comments can be retrieved, updated, and deleted
  private static final DatastoreService DATASTORE = DatastoreServiceFactory.getDatastoreService();

  // Gives access to all comment entities
  private static final PreparedQuery RESULTS = DATASTORE.prepare(QUERY);

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    for (Entity entity : RESULTS.asIterable()) {
      Key commentKey = entity.getKey();
      DATASTORE.delete(commentKey);
    }
  }
}