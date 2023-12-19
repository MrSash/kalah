# Kalah (aka Mancala) Game

## Navigation

* [Description](#description)
* [The Game Rules](#the-game-rules)
    * [Board Setup](#board-setup)
    * [Game Play](#game-play)
    * [Capturing Stones](#capturing-stones)
    * [The Game Ends](#the-game-ends)
* [Build / Run / Test](#build--run--test)
    * [Option 1: Docker script](#option-1-docker-script)
    * [Option 2: Maven script](#option-2-maven-script)
    * [Option 3: Manually](#option-3-manually)
    * [Request script](#request-script)
* [Using](#using)
* [Technical approaches](#technical-approaches)
* [TO-DO List](#to-do-list)

## Description

Kalah is a two-player board game.
Current project written with Java (17 version), Spring Framework, and maven.
For storing players and games, this project uses an H2 SQL database.
Also, it has swagger and metric endpoints.

## The Game Rules

### Board Setup

Each of the two players has his six pits in front of him.
To the right of the six pits,
Each player has a larger pit.
At the start of the game, there are six stones in each of the six round pits.

### Game Play

The player who begins with the first move picks up all the stones in any of his own six pits,
and sows the stones on to the right, one in each of the following pits, including his own big pit.
No stones are put in the opponents' big pit.
If the player's last stone lands in his own big pit, he gets another turn.
This can be repeated several times before it's the other player's turn.

### Capturing Stones

During the game, the pits are emptied on both sides.
When the last stone lands in an own empty pit,
the player always captures his own stone and all stones in the opposite pit
(the other player’s pit) and puts them in his own big pit.

### The Game Ends

The game is over as soon as one of the sides runs out of stones.
The player who still has stones in his pits keeps them and puts them in his big pit.
The winner of the game is the player who has the most stones in his big pit.

## Build / Run / Test

This project has three options to install it: docker script, maven script, and manually.
All runner scripts (except third) are contained in the "runner" folder.
Besides this, they use the "PORT" variable from the config file which is contained in the "config" folder (default is
8080).
This folder uses it only for a lightweight running demonstration, not for a real project, especially not for running in
a production.

### Option 1: Docker script

#### Requirements

- Docker

#### Steps

Run a shell script "run.sh" to launch the project. For tests launching use "run_test.sh" script.

### Option 2: Maven script

#### Requirements

- Java (recommend at least 17)

#### Steps

Run a shell script "run.sh" to launch the project.
For test launching use "run_test.sh" script.
It uses a saved maven wrapper to run it, so you don't need to have a maven.

### Option 3: Manually

#### Requirements

- Java (recommend at least 17)
- Maven (recommend 3.9.*)

#### Steps

Install this project with the usual maven install (mvn clean package) and run it with "java -jar"
command or using the IDE.
For tests use IDE or "mvn tests" command.

### Request script

#### Requirements

- jq command-line

#### Steps

Also, "runner" folder contains "request"
folder with a shell script which will send requests in all endpoints (except actuator endpoints).
It uses a port from config as well.

## Using

- Default API URL - http://localhost:8080/api
- Swagger URL (also contains actuator endpoints) - http://localhost:8080/api/swagger-ui/index.html#/
- H2 SQL DB managing - http://localhost:8080/api/h2-console

## Technical approaches

- for this project, I refused to use authorization because I decided to focus only on a game logic for a demo.
- currently it works only in offline mode, but it is possible to expand it for multiplayer.
- this project was started with using a PostgreSQL database, but I decided to change it to an H2 SQL database for a demo
  and make the project more lightweight.
  In real projects,
  I would prefer to use PostgreSQL for relational (player as example)
  data and NoSQL for non-relational (game records/moving as example) data.
- the project was started as a mono repository with a client and backend side, but I preferred to concentrate only on
  the backend side.
- it contains unit and integration tests.
  They have different ways to run and different abstraction classes.
  I tried to make unit tests as lightweight as I can,
  and they don't use Spring context and concentrate only on the logic of one class.

## TO-DO List

*  [x] initialize server side
*  [x] initialize test runner
*  [x] add player logic
    *  [x] create a player
    *  [x] create login stub
    *  [x] get player state
*  [x] add swagger
*  [x] add game logic
    *  [x] get all player games
    *  [x] create a new offline game
    *  [x] get game state
    *  [x] add movement logic
*  [x] add monitoring
*  [x] create a simple runner.
    *  [x] add docker in server
    *  [x] create executive file
*  [x] complete README