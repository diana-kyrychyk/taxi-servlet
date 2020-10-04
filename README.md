# taxi-servlet
Web Application for taxi service operators and passengers.

#### Run
To run the application please build war and deploy to Tomcat webserver.

#### DB connection - Data Source
Connection to database is carried out by lookup DataSource provided by Tomcat server.

##### Configure apache tomcatserver:
Add to $Catalina/lib folder library for postgresql (postgresql-42.2.16.jar)


#### Database
1. For creation database and user please run please the script from `resources/sql/create-user-and-db.sql` folder.
2. For creation tables please run the script from the `resources/sql/create-tables.sql`.
3. For filling database starting demonstrate data, please run the script from `resources/sql/insert-demo-data.sql`.
