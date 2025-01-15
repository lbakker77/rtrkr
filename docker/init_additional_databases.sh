#!/bin/bash

set -e
echo "Starting custom database generation script..."

for i in {1..5}
do

   postgres_db="POSTGRES_DB"$i
   postgres_user="POSTGRES_USER"$i
   postgres_pw="POSTGRES_PASSWORD"$i

   echo db ${!postgres_db}
   echo user ${!postgres_user}
   echo pw ${!postgres_pw}

   if ! [ -z ${!postgres_db} ] && ! [ -z ${!postgres_user} ] && ! [ -z ${!postgres_pw} ]
   then
        echo "Found additional database config with suffix $i"
        psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
         CREATE USER "${!postgres_user}" WITH PASSWORD '${!postgres_pw}';
         CREATE DATABASE "${!postgres_db}" WITH OWNER "${!postgres_user}";
EOSQL

   else
       echo "No variables found with suffix $i. exiting..."
       break
   fi

done





