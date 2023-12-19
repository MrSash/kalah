#! /bin/bash

source ../config/config.sh
player_name=$(uuidgen | head -c 20)

echo "MESSAGE: Try to create a new player (player_name = $player_name)"
curl -s --location --request POST "localhost:$PORT/api/v1/players" \
--header 'Content-Type: application/json' \
--data-raw "{
    \"name\":\"$player_name\"
}"
echo
echo

echo "MESSAGE: Try to login player (player_name = $player_name)"
login_response=$(curl -s --location --request GET "localhost:$PORT/api/v1/players/login?name=$player_name")
player_id=$(jq -r '.playerId' <<< "$login_response")
echo "$login_response"
echo

echo "MESSAGE: Try to get player state (player_id = $player_id, player_name = $player_name)"
curl -s --location --location --request GET "localhost:$PORT/api/v1/players/$player_id"
echo
echo

echo "MESSAGE: Try to create a first new game (player_id = $player_id, player_name = $player_name)"
create_game_response=$(curl -s --location --request POST "localhost:$PORT/api/v1/games" \
--header 'Content-Type: application/json' \
--data-raw "{
    \"ownerId\":\"$player_id\"
}")
game1_id=$( jq -r '.id' <<< "$create_game_response")
echo "$create_game_response"
echo

echo "MESSAGE: Try to create a second new game (player_id = $player_id, player_name = $player_name)"
curl -s --location --request POST "localhost:$PORT/api/v1/games" \
--header 'Content-Type: application/json' \
--data-raw "{
    \"ownerId\":\"$player_id\"
}"
echo
echo

echo "MESSAGE: Try to create a third new game (player_id = $player_id, player_name = $player_name)"
curl -s --location --request POST "localhost:$PORT/api/v1/games" \
--header 'Content-Type: application/json' \
--data-raw "{
    \"ownerId\":\"$player_id\"
}"
echo
echo

echo "MESSAGE: Try to get the first game state (game_id = $game1_id)"
curl -s --location --location --request GET "localhost:$PORT/api/v1/games/$game1_id"
echo
echo

echo "MESSAGE: Try to get all player games (player_id = $player_id, player_name = $player_name)"
curl -s --location --location --request GET "localhost:$PORT/api/v1/games/players/$player_id"
echo
echo

echo "MESSAGE: Try to make first move in the first game (game_id = $game1_id)"
curl -s --location --location --request POST "localhost:$PORT/api/v1/games/$game1_id/move" \
--header 'Content-Type: application/json' \
--data-raw "{
    \"position\":0,
    \"turn\":\"PLAYER_1\"
}"
echo
echo

echo "MESSAGE: Try to make second move in the first game (game_id = $game1_id)"
curl -s --location --location --request POST "localhost:$PORT/api/v1/games/$game1_id/move" \
--header 'Content-Type: application/json' \
--data-raw "{
    \"position\":1,
    \"turn\":\"PLAYER_1\"
}"
echo
echo

echo "MESSAGE: Try to make third move in the first game (game_id = $game1_id)"
curl -s --location --location --request POST "localhost:$PORT/api/v1/games/$game1_id/move" \
--header 'Content-Type: application/json' \
--data-raw "{
    \"position\":7,
    \"turn\":\"PLAYER_2\"
}"
echo