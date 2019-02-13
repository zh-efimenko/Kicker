# ZenKicker

[![Screenshots](docs/screenshots/animation.webp)](http://kicker.zensoft.by)

## Kicker:  is a table-top game that is loosely based on football

The aim of the game is to use the control knobs moving the ball into the 
opponent’s goal. There are no unified rules for playing the game, in the 
sense which rules vary in different countries and even in the cities, and sometimes
between different clubs in the same city.

## For what?

The main idea of this is to distract the guys from working as well as to let feel relaxed in 
free time.

## What s there?

In order to do the game more interesting the rating system was invented. Employees 
register new games and compete between each other. 

## Running

To run application, you will need:
 1) Download application from repository.
 2) Download docker and install it in system.
 3) Build application by gradle.
 4) Create a docker image from the application and name it as `kicker`.
 5) Run docker script `{root_project}/backend/docker/deploy.sh`.

## Prerequisites

The **_Kicker application_** uses several environment variables. All variables are required.

### PostgresSQL service

* `POSTGRES_HOST`

Configure the **POSTGRES_HOST** environment variable to point to the host where 
you are running your PostgresSQL server. This is required to connect your 
application to the database.

* `POSTGRES_DB`

Add the name of your PostgreSQL database to the **POSTGRES_DB** environment 
variable. It lets the application know which database to be connected to.

* `POSTGRES_USER` & `POSTGRES_PASSWORD`

Set the **POSTGRES_USER** environment variable with the username you want to 
use to connect to the database.  Set the **POSTGRES_PASSWORD** environment 
variable which is user's password. Using environment variables to configure user 
authentication to your database is more secure than placing this configuration 
in your code. **NOTE:** The user you specify in these two environment variables
should be a superuser, otherwise you will run into problems.

### Front

* `SERVER_HOST`

Set the **SERVER_HOST** environment variable so that **Frontend** knows where the requests for the icons should be sent to. 

### Static Data
After starting application static the data will be saved in the folder `/root/data` in the file system.
In case the application is stopped or deployed, the static data will 
remain in place.

## Swagger
In order to feel free to develop the frontend or microservices the swagger 
was bolted.

Get Kicker API can be obtained from the following url:

`http://{host}/swagger-ui.html`