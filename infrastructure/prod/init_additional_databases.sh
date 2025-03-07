#!/bin/bash

set -e
echo "Starting custom database generation script..."

for i in {1..5}
do

   postgres_db="POSTGRES_DB"$i
   postgres_user="POSTGRES_USER"$i
   postgres_pw="POSTGRES_PASSWORD"$i#
   postgres_pw_file="POSTGRES_PASSWORD_FILE"$i

   if ! [ -z ${postgres_pw_file} ] &&  [ -e ${!postgres_pw_file} ]
   then
      local_postgres_pw=$(cat ${!postgres_pw_file})
   else
      local_postgres_pw=${!postgres_pw}
   fi 

   if ! [ -z ${!postgres_db} ] && ! [ -z ${!postgres_user} ] &&  ! [ -z ${local_postgres_pw} ] 
   then
        local_postgres_user=${!postgres_user}
        local_postgres_db=${!postgres_db}

        echo "Creating database $i..."
        echo "Found additional database config with suffix $i"
        psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
         CREATE USER "${local_postgres_user}" WITH PASSWORD '${local_postgres_pw}';
         CREATE DATABASE "${local_postgres_db}" WITH OWNER "${local_postgres_user}" encoding 'UTF8';

EOSQL

   else
       echo "No variables found with suffix $i. exiting..."
       break
   fi

done





