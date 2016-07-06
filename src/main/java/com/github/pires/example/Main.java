/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.pires.example;

import com.github.pires.example.dao.JSONBEntityDao;
import com.github.pires.example.model.JSONBEntity;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.persistence.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main extends HttpServlet {

  private static EntityManagerFactory entityManagerFactory;
  private static EntityManager entityManager;

  public Main() {
    Map<String, String> env = System.getenv();
    Map<String, Object> configOverrides = new HashMap<String, Object>();
    for (String envName : env.keySet()) {
      if (envName.contains("JDBC_DATABASE_URL")) {
        configOverrides.put("javax.persistence.jdbc.url", env.get(envName));
      }
    }

    // provision persistence manager
    entityManagerFactory = Persistence.createEntityManagerFactory("prod", configOverrides);
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.setFlushMode(FlushModeType.COMMIT);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    resp.getWriter().print("Hello from the Hibernate JSONB example!");
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String json = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

    EntityTransaction tx = entityManager.getTransaction();

    JSONBEntityDao dao = new JSONBEntityDao();
    dao.setEntityManager(entityManager);

    tx.begin();
    JSONBEntity entity = new JSONBEntity();
    entity.document(json);
    dao.persist(entity);
    tx.commit();
  }

  public static void main(String[] args) throws Exception{
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new Main()),"/*");
    server.start();
    server.join();
  }
}